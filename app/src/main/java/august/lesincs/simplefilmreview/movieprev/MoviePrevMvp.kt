package august.lesincs.simplefilmreview.movieprev

import august.lesincs.simplefilmreview.bean.MoviePrevBean
import august.lesincs.simplefilmreview.bean.Subject
import august.lesincs.simplefilmreview.retrofit.ComingSoonService
import august.lesincs.simplefilmreview.retrofit.DOUBAN_BASE_URL
import august.lesincs.simplefilmreview.retrofit.InTheatersService
import august.lesincs.simplefilmreview.retrofit.Top250Service
import august.lesincs.simplefilmreview.util.MyObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Administrator on 2017/10/21.
 */
class MoviePrevModel : MoviePrevContract.Model {

    override fun getMoviePrevBean(start: Int, moviePrevType: MoviePrevType): Observable<MoviePrevBean> {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(DOUBAN_BASE_URL)
                .build()
        return when (moviePrevType) {
            MoviePrevType.TOP250 -> retrofit.create(Top250Service::class.java).getTop250Movies(start)
            MoviePrevType.INTHRATERS -> retrofit.create(InTheatersService::class.java).getInTheaterMovies(start)
            MoviePrevType.COMINGSOON -> retrofit.create(ComingSoonService::class.java).getComingSoonMovies(start)
        }
    }

    override fun getSubjects(moviePrevBean: MoviePrevBean): List<Subject> {
        return moviePrevBean.subjects
    }

}


class MoviePrevPresenter(val view: MoviePrevContract.View) : MoviePrevContract.Presenter {

     var isLoading = false
    val model: MoviePrevContract.Model by lazyOf(MoviePrevModel())

     override fun onStart() {
        view.showRefreshIndicator()
        view.hideNetworkError()
        view.hideServerError()
        view.showProgressBar()
        model.getMoviePrevBean(0, view.getMoviePrevType())
                .map { model.getSubjects(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyObserver<List<Subject>>() {
                    override fun onNext(t: List<Subject>) {
                        view.showNewestMovies(t)
                        view.hideRefreshIndicator()
                        view.addOnScrollListener()
                    }

                    override fun onError(e: Throwable) {
                        view.showServerError()
                        view.clearAllMovie()
                        view.clearOnScrollListener()
                        view.hideProgressBar()
                        view.snackServerError()
                        view.hideRefreshIndicator()
                    }

                    override fun onNetWorkNotAvailable() {
                        view.clearOnScrollListener()
                        view.hideProgressBar()
                        view.clearAllMovie()
                        view.showNetworkError()
                        view.snackNetworkError()
                        view.hideRefreshIndicator()
                    }
                })
    }

    override fun loadMoreMovies() {

        if (!isLoading){

            isLoading =true
            model.getMoviePrevBean(view.getCurrentMovieCount(), view.getMoviePrevType())
                    .map { model.getSubjects(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyObserver<List<Subject>>() {
                        override fun onNext(t: List<Subject>) {
                            isLoading =false
                            if (t.isEmpty()) {
                                view.snackNoMoreMovie()
                                view.hideProgressBar()
                                view.clearOnScrollListener()
                            } else {
                                view.showMoreMovies(t)
                            }
                        }

                        override fun onError(e: Throwable) {
                            isLoading =false
                            view.clearOnScrollListener()
                            view.hideProgressBar()
                            view.snackServerError()
                        }

                        override fun onNetWorkNotAvailable() {
                            isLoading =false
                            view.clearOnScrollListener()
                            view.hideProgressBar()
                            view.snackNetworkError()
                        }
                    })
        }


    }

}