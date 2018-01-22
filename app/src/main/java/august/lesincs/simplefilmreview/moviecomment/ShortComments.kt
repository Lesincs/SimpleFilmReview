package august.lesincs.simplefilmreview.moviecomment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.MOVIE_ID
import august.lesincs.simplefilmreview.adapter.ShortCommentsAdapter
import august.lesincs.simplefilmreview.base.LazyInitFragment
import august.lesincs.simplefilmreview.bean.Comment
import august.lesincs.simplefilmreview.bean.ShortCommentsBean
import august.lesincs.simplefilmreview.retrofit.DOUBAN_BASE_URL
import august.lesincs.simplefilmreview.retrofit.ShortsCommentService
import august.lesincs.simplefilmreview.util.MyObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_short_comments.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Administrator on 2017/10/14.
 */
class ShortCommentsFrag : ShortCommentsContract.View, LazyInitFragment() {
    override fun showNetWorkError() {
        s_tv_network_error.visibility = View.VISIBLE
    }

    override fun snackNetworkError() {
        Snackbar.make(mRecyclerViewShortComments, R.string.network_connecting_error, Snackbar.LENGTH_LONG).show()
    }

    override fun showServerError() {
        s_tv_server_error.visibility = View.VISIBLE
    }

    override fun showProgressBar() {
        mAdaper.isShowProgressBar = true
        mAdaper.notifyItemChanged(mAdaper.itemCount - 1)
    }

    override fun hideProgressBar() {
        mAdaper.isShowProgressBar = false
        mAdaper.notifyItemChanged(mAdaper.itemCount - 1)
    }

    override fun showNoComments() {
        s_tv_no_comments.visibility = View.VISIBLE
    }

    override fun addOnScrollListener() {
        onScrollListener.isWorking = true
    }

    override fun clearOnScrollListener() {
        onScrollListener.isWorking = false
    }

    override fun snackNoMoreShortCommentsMsg() {
        Snackbar.make(mRecyclerViewShortComments, R.string.no_more_short_comments, Snackbar.LENGTH_LONG).show()
    }

    override fun snackServerError() {
        Snackbar.make(mRecyclerViewShortComments, R.string.server_error, Snackbar.LENGTH_LONG).show()
    }

    override fun loadData() {
        mPresenter.onStart()
    }


    val comments = ArrayList<Comment>()
    lateinit var mAdaper: ShortCommentsAdapter
    lateinit var mLinearLayoutManager: LinearLayoutManager
    val mPresenter: ShortCommentsContract.Presenter by lazyOf(ShortCommentsPresenter(this))

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mAdaper = ShortCommentsAdapter(comments)
        mLinearLayoutManager = LinearLayoutManager(context)
        mRecyclerViewShortComments.layoutManager = mLinearLayoutManager
        mRecyclerViewShortComments.adapter = mAdaper
        mRecyclerViewShortComments.itemAnimator = DefaultItemAnimator()
        mRecyclerViewShortComments.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        var isWorking = true
        var isPullUp = false
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val lastCompletelyPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition()
            val itemTotalCount = mLinearLayoutManager.itemCount
            if (lastCompletelyPosition >= itemTotalCount - 5 && isPullUp && isWorking) {
                mPresenter.loadMoreShortComments()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isPullUp = dy > 0
        }
    }

    override fun showNewestShortComments(comments: List<Comment>) {
        this.comments.clear()
        this.comments.addAll(comments)
        mAdaper.notifyDataSetChanged()


    }

    override fun showMoreShortComments(comments: List<Comment>) {

        val position = this.comments.size
        this.comments.addAll(comments)
        mAdaper.notifyItemRangeChanged(position, comments.size)


    }

    override fun getCurrentShortCommentCount(): Int {
        return mLinearLayoutManager.itemCount - 1
    }

    override fun getMovieId(): String {
        return activity.intent.getStringExtra(MOVIE_ID)
    }


    override fun getLayoutId(): Int = R.layout.frag_short_comments
}


interface ShortCommentsContract {

    interface Model {
        fun getShortCommentsBean(id: String, start: Int): Observable<ShortCommentsBean>
        fun getComments(shortCommentsBean: ShortCommentsBean): List<Comment>
    }

    interface View {
        fun showNewestShortComments(comments: List<Comment>)
        fun showMoreShortComments(comments: List<Comment>)
        fun getCurrentShortCommentCount(): Int
        fun getMovieId(): String
        fun showServerError()
        fun showProgressBar()
        fun hideProgressBar()
        fun showNoComments()
        fun showNetWorkError()
        fun addOnScrollListener()
        fun clearOnScrollListener()
        fun snackNoMoreShortCommentsMsg()
        fun snackServerError()
        fun snackNetworkError()
    }

    interface Presenter {
        fun loadMoreShortComments()
        fun onStart()
    }
}

class ShortCommentsModel : ShortCommentsContract.Model {
    override fun getShortCommentsBean(id: String, start: Int): Observable<ShortCommentsBean> {
        val retrofit = Retrofit.Builder()
                .baseUrl(DOUBAN_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        return retrofit.create(ShortsCommentService::class.java).getShortComments(id, start)
    }

    override fun getComments(shortCommentsBean: ShortCommentsBean): List<Comment> {
        return shortCommentsBean.comments
    }
}

class ShortCommentsPresenter(val view: ShortCommentsContract.View) : ShortCommentsContract.Presenter {

    val model: ShortCommentsContract.Model by lazyOf(ShortCommentsModel())
    private var isLoading = false
    override fun onStart() {
        model.getShortCommentsBean(view.getMovieId(), view.getCurrentShortCommentCount())
                .map { model.getComments(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyObserver<List<Comment>>() {
                    override fun onNext(t: List<Comment>) {


                        if (t.isEmpty()) {
                            view.showNoComments()
                            view.clearOnScrollListener()
                            view.hideProgressBar()
                        } else if (t.size < 20) {
                            view.hideProgressBar()
                            view.clearOnScrollListener()

                        } else {
                            view.showNewestShortComments(t)
                            view.addOnScrollListener()
                        }

                    }

                    override fun onError(e: Throwable) {
                        view.snackServerError()
                        view.clearOnScrollListener()
                        view.hideProgressBar()
                        view.showServerError()
                    }

                    override fun onNetWorkNotAvailable() {
                        view.snackNetworkError()
                        view.clearOnScrollListener()
                        view.hideProgressBar()
                        view.showNetWorkError()
                    }


                })

    }

    override fun loadMoreShortComments() {

        if (!isLoading) {
            isLoading = true
            model.getShortCommentsBean(view.getMovieId(), view.getCurrentShortCommentCount())
                    .map { model.getComments(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyObserver<List<Comment>>() {
                        override fun onNext(t: List<Comment>) {
                            isLoading = false
                            if (t.isEmpty()) {
                                view.snackNoMoreShortCommentsMsg()
                                view.hideProgressBar()
                                view.clearOnScrollListener()
                            } else {
                                view.showMoreShortComments(t)
                            }

                        }

                        override fun onError(e: Throwable) {
                            isLoading = false
                            view.snackNoMoreShortCommentsMsg()
                            view.clearOnScrollListener()
                            view.hideProgressBar()
                        }

                        override fun onNetWorkNotAvailable() {
                            isLoading = false
                            view.snackNetworkError()
                            view.hideProgressBar()
                            view.clearOnScrollListener()
                        }


                    })
        }
    }

}