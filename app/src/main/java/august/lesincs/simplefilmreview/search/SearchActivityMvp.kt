package august.lesincs.simplefilmreview.search

import august.lesincs.simplefilmreview.bean.MoviePrevBean
import august.lesincs.simplefilmreview.bean.Subject
import august.lesincs.simplefilmreview.retrofit.DoubanRetrofit
import august.lesincs.simplefilmreview.retrofit.MovieSearchService
import august.lesincs.simplefilmreview.retrofit.MovieTagSearchService
import august.lesincs.simplefilmreview.util.MyObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Administrator on 2017/11/2.
 */

class SearchActivityModel : SearchActivityContract.Model {
    override fun getMovePrevBeanByTag(tag: String, start: Int): Observable<MoviePrevBean> {
        return DoubanRetrofit.create(MovieTagSearchService::class.java).getTagMovie(tag, start)

    }

    override fun getMovePrevBeanByQ(q: String, start: Int): Observable<MoviePrevBean> {
        return DoubanRetrofit.create(MovieSearchService::class.java).getSearchMovie(q, start)
    }


}

class SearchActivityPresenter(private val view: SearchActivityContract.View) : SearchActivityContract.Presenter {

    private val model: SearchActivityContract.Model by lazyOf(SearchActivityModel())

    fun loadFirstTagMovies(tag: String) {

        view.showProgressDialog()
        view.hideTagBox()
        model.getMovePrevBeanByTag(tag, 0)
                .map { it.subjects }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyObserver<List<Subject>>() {
                    override fun onNext(t: List<Subject>) {
                        view.hideProgressDialog()
                        view.showMoreSearchResult(t)
                        view.enableOnScrollListener()
                        view.showProgressBar()
                    }

                    override fun onError(e: Throwable) {

                        view.hideProgressDialog()
                        view.showServerError()
                    }

                    override fun onNetWorkNotAvailable() {
                        view.hideProgressDialog()
                        view.showNetworkError()

                    }

                })
    }

    fun loadMoreTagMovies(tag: String, start: Int) {
        model.getMovePrevBeanByTag(tag, start)
                .map { it.subjects }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyObserver<List<Subject>>() {
                    override fun onNext(t: List<Subject>) {

                        view.hideProgressDialog()
                        if (t.isEmpty()) {
                            view.hideProgressDialog()
                            view.hideProgressBar()
                            view.snackNoMoreFilm()
                            view.disableOnScrollListener()
                        } else {
                            view.showMoreSearchResult(t)
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.hideProgressDialog()
                        view.hideProgressBar()
                        view.disableOnScrollListener()
                    }

                    override fun onNetWorkNotAvailable() {
                        view.hideProgressDialog()
                        view.hideProgressBar()
                        view.disableOnScrollListener()
                    }

                })
    }

    fun loadFirstQMovies(q: String) {
        if (!q.isBlank()) {
            view.showProgressDialog()
            view.hideTagBox()
            view.hideNetworkError()
            view.hideServerError()
            view.hideNoSearchResult()
            view.enableOnScrollListener()
            model.getMovePrevBeanByQ(q, 0)
                    .map { it.subjects }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyObserver<List<Subject>>() {
                        override fun onNext(t: List<Subject>) {
                            view.hideProgressDialog()
                            if (t.isEmpty()) {
                                view.showNoSearchResult()
                                view.disableOnScrollListener()
                                view.hideProgressBar()
                            } else if (t.size < 10) {
                                view.showNewestSearchResult(t)
                                view.hideProgressBar()
                                view.disableOnScrollListener()

                            } else {
                                view.showNewestSearchResult(t)
                                view.showProgressBar()
                                view.enableOnScrollListener()
                            }
                        }

                        override fun onError(e: Throwable) {
                            view.hideProgressDialog()
                            view.clearAllItem()
                            view.hideProgressBar()
                            view.showServerError()
                            view.disableOnScrollListener()
                        }

                        override fun onNetWorkNotAvailable() {
                            view.hideProgressDialog()
                            view.clearAllItem()
                            view.hideProgressBar()
                            view.disableOnScrollListener()
                            view.showNetworkError()
                        }
                    })
        }

    }

    fun loadMoreQMovies(q: String, start: Int) {

        if (!q.isBlank()) {

            model.getMovePrevBeanByQ(q, start)
                    .map { it.subjects }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyObserver<List<Subject>>() {
                        override fun onNext(t: List<Subject>) {
                            view.hideProgressDialog()
                            if (t.isEmpty()) {

                                view.snackNoMoreFilm()
                                view.disableOnScrollListener()
                                view.hideProgressBar()
                            } else {
                                view.showMoreSearchResult(t)
                            }
                        }

                        override fun onError(e: Throwable) {
                            view.hideProgressDialog()
                            view.snackServerError()
                            view.hideProgressBar()
                            view.disableOnScrollListener()
                        }

                        override fun onNetWorkNotAvailable() {
                            view.hideProgressDialog()
                            view.snackNetworkError()
                            view.hideProgressBar()
                            view.disableOnScrollListener()
                        }

                    })
        }
    }


}