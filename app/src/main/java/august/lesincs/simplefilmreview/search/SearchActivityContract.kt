package august.lesincs.simplefilmreview.search

import august.lesincs.simplefilmreview.bean.MoviePrevBean
import august.lesincs.simplefilmreview.bean.Subject
import io.reactivex.Observable

/**
 * Created by Administrator on 2017/11/2.
 */

interface SearchActivityContract {


    interface View {

        fun showTagBox()
        fun hideTagBox()
        fun showNoSearchResult()
        fun hideNoSearchResult()
        fun showClearButton()
        fun hideClearButton()
        fun showNetworkError()
        fun hideNetworkError()
        fun showServerError()
        fun hideServerError()
        fun snackNetworkError()
        fun snackServerError()
        fun snackNoMoreFilm()
        fun showProgressBar()
        fun hideProgressBar()
        fun showNewestSearchResult(subjects: List<Subject>)
        fun showMoreSearchResult(subjects: List<Subject>)
        fun clearAllItem()
        fun enableOnScrollListener()
        fun disableOnScrollListener()
        fun showProgressDialog()
        fun hideProgressDialog()
        fun hideSoftKeyboard()

    }

    interface Model{
        fun getMovePrevBeanByTag(tag:String,start:Int):Observable<MoviePrevBean>
        fun getMovePrevBeanByQ(q:String,start:Int):Observable<MoviePrevBean>
    }

    interface Presenter
}