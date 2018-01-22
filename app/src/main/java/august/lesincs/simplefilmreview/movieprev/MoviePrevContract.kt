package august.lesincs.simplefilmreview.movieprev

import august.lesincs.simplefilmreview.bean.MoviePrevBean
import august.lesincs.simplefilmreview.bean.Subject
import io.reactivex.Observable

/**
 * Created by Administrator on 2017/9/18.
 */


interface MoviePrevContract {
    interface Model {
        fun getMoviePrevBean(start: Int, moviePrevType: MoviePrevType): Observable<MoviePrevBean>
        fun getSubjects(moviePrevBean: MoviePrevBean): List<Subject>
    }

    interface View {
        fun showNewestMovies(subjects: List<Subject>)
        fun showMoreMovies(subjects: List<Subject>)
        fun getCurrentMovieCount(): Int
        fun getMoviePrevType(): MoviePrevType
        fun hideProgressBar()
        fun showProgressBar()
        fun snackNetworkError()
        fun snackServerError()
        fun showServerError()
        fun hideServerError()
        fun showRefreshIndicator()
        fun clearAllMovie()
        fun hideNetworkError()
        fun snackNoMoreMovie()
        fun showNetworkError()
        fun hideRefreshIndicator()
        fun addOnScrollListener()
        fun clearOnScrollListener()
    }

    interface Presenter {
        fun loadMoreMovies()
        fun onStart()
    }
}

