package august.lesincs.simplefilmreview.movieprev

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.MoviePrevAdapter
import august.lesincs.simplefilmreview.base.LazyInitFragment
import august.lesincs.simplefilmreview.bean.Subject
import kotlinx.android.synthetic.main.frag_movie_prev.*

/**
 * Created by Administrator on 2017/10/21.
 */

const val MOVIE_PREV_TYPE = "MOVIE_PREV_TYPE"

class MoviePrevFrag : MoviePrevContract.View, LazyInitFragment() {

    val mPresenter: MoviePrevContract.Presenter  by lazyOf(MoviePrevPresenter(this))
    lateinit var mAdapter: MoviePrevAdapter
    lateinit var mStaggeredGridLayoutManager: StaggeredGridLayoutManager
    val subjects: ArrayList<Subject> by lazyOf(ArrayList())

    override fun clearAllMovie() {
        subjects.clear()
        mAdapter.notifyDataSetChanged()
    }

    override fun showRefreshIndicator() {
        mSwipeRefreshLayout.isRefreshing = true
    }

    override fun hideServerError() {
        tv_server_error.visibility = View.INVISIBLE
    }

    override fun hideNetworkError() {
        tv_network_error.visibility = View.INVISIBLE
    }

    override fun hideRefreshIndicator() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    override fun showNetworkError() {
        tv_network_error.visibility = View.VISIBLE
    }

    override fun showServerError() {
        tv_server_error.visibility = View.VISIBLE
    }

    override fun snackNoMoreMovie() {
        Snackbar.make(mRecyclerView, R.string.no_more_movie, Snackbar.LENGTH_LONG).show()
    }

    override fun hideProgressBar() {
        mAdapter.isShowProgressBar = false
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
    }

    override fun showProgressBar() {
        mAdapter.isShowProgressBar = true
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
    }

    override fun snackServerError() {
        Snackbar.make(mRecyclerView, R.string.server_error, Snackbar.LENGTH_LONG).show()
    }

    override fun snackNetworkError() {
        Snackbar.make(mRecyclerView, R.string.network_connecting_error, Snackbar.LENGTH_LONG).show()
    }


    override fun addOnScrollListener() {
        onScrollListener.isWorking = true
    }

    override fun clearOnScrollListener() {
        onScrollListener.isWorking = false
    }

    override fun getLayoutId(): Int = R.layout.frag_movie_prev

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = MoviePrevAdapter(subjects)
        mRecyclerView.adapter = mAdapter
        mStaggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView.layoutManager = mStaggeredGridLayoutManager
        mRecyclerView.addOnScrollListener(onScrollListener)
        mSwipeRefreshLayout.setColorSchemeColors(resources.getColor(android.R.color.holo_red_light))
        mSwipeRefreshLayout.setOnRefreshListener { mPresenter.onStart() }

    }

    override fun loadData() {
        mPresenter.onStart()
    }

    override fun showNewestMovies(subjects: List<Subject>) {
        this.subjects.clear()
        this.subjects.addAll(subjects)
        mAdapter.notifyDataSetChanged()
    }

    override fun showMoreMovies(subjects: List<Subject>) {
        val position = this.subjects.size
        this.subjects.addAll(subjects)
        mAdapter.notifyItemRangeChanged(position, subjects.size)
    }

    override fun getCurrentMovieCount(): Int = mAdapter.itemCount - 1

    override fun getMoviePrevType(): MoviePrevType {
        return arguments[MOVIE_PREV_TYPE] as MoviePrevType
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        var isWorking = true
        var isPullUp = false
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val lastVisibleItem = mStaggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(kotlin.IntArray(3))[2]
            //在加载三组之后可以加载更多
            if (lastVisibleItem + 1 >= mAdapter.itemCount - 1 - 6 && isPullUp && isWorking) {
                mPresenter.loadMoreMovies()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            isPullUp = dy > 0
        }
    }
}