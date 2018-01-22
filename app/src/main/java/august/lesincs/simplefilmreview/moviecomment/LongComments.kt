package august.lesincs.simplefilmreview.moviecomment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.LongCommentsAdapter
import august.lesincs.simplefilmreview.adapter.MOVIE_ID
import august.lesincs.simplefilmreview.base.LazyInitFragment
import august.lesincs.simplefilmreview.bean.LongCommentsBean
import august.lesincs.simplefilmreview.bean.Review
import august.lesincs.simplefilmreview.retrofit.*
import august.lesincs.simplefilmreview.util.MyObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.frag_long_comments.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Administrator on 2017/10/14.
 */
class LongCommentsFrag : LongCommentsContract.View, LazyInitFragment() {

    val mPresenter: LongCommentsContract.Presenter = LongCommentsPresenter(this)
    lateinit var mAdapter: LongCommentsAdapter
    lateinit var mLinearlayoutManager: LinearLayoutManager
    private val reviews = ArrayList<Review>()

    override fun showNetworkError() {
        l_tv_network_error.visibility = View.VISIBLE
    }

    override fun snackNetworkError() {
        Snackbar.make(mRecyclerViewLongComments, R.string.network_connecting_error, Snackbar.LENGTH_LONG).show()
    }

    override fun snackNoMoreLongCommentsMsg() {
        Snackbar.make(mRecyclerViewLongComments, R.string.no_more_long_comments, Snackbar.LENGTH_LONG).show()
    }

    override fun snackServerError() {
        Snackbar.make(mRecyclerViewLongComments, R.string.server_error, Snackbar.LENGTH_LONG).show()
    }

    override fun clearOnScrollListener() {
        onScrollListener.isWorking = false
    }

    override fun addOnScrollListener() {
        onScrollListener.isWorking = true
    }

    override fun showNoComments() {
        l_tv_no_comments.visibility = View.VISIBLE
    }

    override fun showServerError() {
        l_tv_server_error.visibility = View.VISIBLE
    }

    override fun showProgressBar() {
        mAdapter.isShowProgressBar = true
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
    }

    override fun hideProgressBar() {
        mAdapter.isShowProgressBar = false
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
    }

    override fun loadData() {
        mPresenter.onStart()
    }

    override fun showNewestLongComments(reviews: List<Review>) {
        this.reviews.clear()
        this.reviews.addAll(reviews)
        mAdapter.notifyDataSetChanged()
    }

    override fun showMoreLongComments(reviews: List<Review>) {
        val position = this.reviews.size
        this.reviews.addAll(reviews)
        mAdapter.notifyItemRangeChanged(position, reviews.size)
    }

    override fun getCurrentLongCommentCount(): Int {
        return mLinearlayoutManager.itemCount - 1
    }

    override fun getMovieId(): String {
        return activity.intent.getStringExtra(MOVIE_ID)
    }

    override fun getLayoutId(): Int = R.layout.frag_long_comments

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = LongCommentsAdapter(reviews)
        mLinearlayoutManager = LinearLayoutManager(context)
        mRecyclerViewLongComments.adapter = mAdapter
        mRecyclerViewLongComments.layoutManager = mLinearlayoutManager
        mRecyclerViewLongComments.itemAnimator = DefaultItemAnimator()
        mRecyclerViewLongComments.addOnScrollListener(onScrollListener)
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        var isWorking = true
        var isPullUp = false
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val lastCompletelyPosition = mLinearlayoutManager.findLastCompletelyVisibleItemPosition()
            val totalCount = mLinearlayoutManager.itemCount
            if (lastCompletelyPosition >= totalCount - 4 && isPullUp && isWorking) {
                mPresenter.loadMoreLongComments()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isPullUp = dy > 0
        }
    }
}


interface LongCommentsContract {
    interface Model {
        fun getLongCommentsBean(id: String, start: Int): Observable<LongCommentsBean>
        fun getReviews(longCommentsBean: LongCommentsBean): List<Review>
    }

    interface View {
        fun showNewestLongComments(reviews: List<Review>)
        fun showMoreLongComments(reviews: List<Review>)
        fun getCurrentLongCommentCount(): Int
        fun getMovieId(): String
        fun showServerError()
        fun showProgressBar()
        fun hideProgressBar()
        fun showNoComments()
        fun addOnScrollListener()
        fun clearOnScrollListener()
        fun snackNoMoreLongCommentsMsg()
        fun showNetworkError()
        fun snackServerError()
        fun snackNetworkError()
    }

    interface Presenter {
        fun loadMoreLongComments()
        fun onStart()
    }

}

class LongCommentsModel : LongCommentsContract.Model {
    override fun getLongCommentsBean(id: String, start: Int): Observable<LongCommentsBean> {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(DOUBAN_BASE_URL)
                .build()
        return retrofit.create(LongCommentService::class.java).getLongComments(id, start, DOUBAN_API_KEY, DOUBAN_CLIENT, DOUBAN_UDID)
    }

    override fun getReviews(longCommentsBean: LongCommentsBean): List<Review> = longCommentsBean.reviews

}

class LongCommentsPresenter(val view: LongCommentsContract.View) : LongCommentsContract.Presenter {

    private var isLoading = false
    private val model: LongCommentsContract.Model by lazyOf(LongCommentsModel())

    override fun onStart() {
        model.getLongCommentsBean(view.getMovieId(), view.getCurrentLongCommentCount())
                .map { model.getReviews(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyObserver<List<Review>>() {
                    override fun onNext(t: List<Review>) {
                        if (t.isEmpty()) {
                            view.showNoComments()
                            view.hideProgressBar()
                            view.clearOnScrollListener()
                        } else if (t.size < 10) {
                            view.hideProgressBar()
                            view.clearOnScrollListener()
                            view.showNewestLongComments(t)
                        } else {
                            view.showNewestLongComments(t)
                            view.addOnScrollListener()
                        }
                    }

                    override fun onError(e: Throwable) {
                        view.snackServerError()
                        view.hideProgressBar()
                        view.clearOnScrollListener()
                        view.showServerError()
                    }

                    override fun onNetWorkNotAvailable() {
                        view.snackNetworkError()
                        view.hideProgressBar()
                        view.showNetworkError()
                        view.clearOnScrollListener()
                    }
                })
    }

    override fun loadMoreLongComments() {
        if (!isLoading) {
            isLoading = true
            model.getLongCommentsBean(view.getMovieId(), view.getCurrentLongCommentCount())
                    .map { model.getReviews(it) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(object : MyObserver<List<Review>>() {
                        override fun onNext(t: List<Review>) {
                            isLoading = false
                            if (t.isEmpty()) {
                                view.hideProgressBar()
                                view.clearOnScrollListener()
                                view.snackNoMoreLongCommentsMsg()
                            } else {
                                view.showMoreLongComments(t)
                            }
                        }
                        override fun onError(e: Throwable) {
                            isLoading = false
                            view.snackServerError()
                            view.hideProgressBar()
                            view.clearOnScrollListener()
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

