package august.lesincs.simplefilmreview.search

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.LinearMoviePrevAdapter
import august.lesincs.simplefilmreview.bean.Subject
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.movie_flag.*
import java.util.*

class SearchActivity : SearchActivityContract.View, AppCompatActivity(), View.OnClickListener {

    override fun onClick(p0: View?) {
        onTagClick((p0 as TextView).text.toString())
    }

    lateinit var mLinearLayoutManager: LinearLayoutManager
    lateinit var mAdapter: LinearMoviePrevAdapter
    val presenter by lazyOf(SearchActivityPresenter(this))
    private val subjects = ArrayList<Subject>()
    private var currentInputText: String = ""
    var currentItemCount = 20
    var currentDisposable: Disposable? = null
    var isTag = false
    var currentTag: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initView()
        initTagView()
    }

    private fun onTagClick(tag: String) {

        isTag = true
        currentTag = tag
        presenter.loadFirstTagMovies(currentTag)
        hideSoftKeyboard()
        etSearchContentAS.setText(tag)
        etSearchContentAS.setSelection(tag.length)
        currentItemCount = 20
    }

    private fun initTagView() {
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_America)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_Mainland)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_action)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_affection)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_biographical)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_black_humor)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_classic)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_comedy)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_crime)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_dracula)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_encouragement)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_family)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_feature)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_literary)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_music)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_science_fiction)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_suspense)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_war)).setOnClickListener(this)
        tag_box.findViewWithTag<TextView>(getString(R.string.tag_youth)).setOnClickListener(this)

    }

    private fun initView() {
        setSupportActionBar(toolbarAS)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
            setMessage(resources.getString(R.string.loading))
        }
        mLinearLayoutManager = LinearLayoutManager(this)
        mAdapter = LinearMoviePrevAdapter(subjects)
        recyclerViewAS.layoutManager = mLinearLayoutManager
        recyclerViewAS.adapter = mAdapter
        recyclerViewAS.addOnScrollListener(onScrollListener)
        etSearchContentAS.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                currentInputText = etSearchContentAS.text.toString()
                if (!currentInputText.isBlank()) {
                    (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(etSearchContentAS.windowToken, 0)
                    isTag = false
                    presenter.loadFirstQMovies(currentInputText)
                    currentItemCount = 20
                }
            }
            true
        }
        ibClearText.setOnClickListener({
            currentItemCount = 20
            etSearchContentAS.text.clear()
            currentDisposable?.dispose()
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(etSearchContentAS, 0)
            showTagBox()
            clearAllItem()
            disableOnScrollListener()
            hideNetworkError()
            hideProgressBar()
            hideNoSearchResult()
            hideServerError()

        })
        etSearchContentAS.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0?.isBlank()!!) {
                    ibClearText.visibility = View.VISIBLE
                    ibClearText.isClickable = true
                } else {

                    ibClearText.visibility = View.INVISIBLE
                    ibClearText.isClickable = false
                    clearAllItem()
                    showTagBox()
                    hideServerError()
                    hideNetworkError()
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

    }


    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        var isWorking = false
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (isWorking) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    val lastItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition()
                    val totalCount = mLinearLayoutManager.itemCount

                    if (lastItemPosition + 1 >= totalCount - 1) {
                        if (isTag) {
                            presenter.loadMoreTagMovies(currentTag, currentItemCount)
                            currentItemCount += 20
                        } else {
                            presenter.loadMoreQMovies(currentInputText, currentItemCount)
                            currentItemCount += 20
                        }
                    }
                }
            }

        }
    }

    lateinit var progressDialog: ProgressDialog


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showTagBox() {
        tag_box.visibility = View.VISIBLE
    }

    override fun hideTagBox() {
        tag_box.visibility = View.INVISIBLE
    }

    override fun showNoSearchResult() {
        tvNoSearchResultFS.visibility = View.VISIBLE
    }

    override fun hideNoSearchResult() {
        tvNoSearchResultFS.visibility = View.INVISIBLE
    }

    override fun showClearButton() {
        ibClearText.visibility = View.VISIBLE
    }

    override fun hideClearButton() {
        ibClearText.visibility = View.INVISIBLE
    }

    override fun showNetworkError() {
        tvNetworkErrorAS.visibility = View.VISIBLE
    }

    override fun hideNetworkError() {
        tvNetworkErrorAS.visibility = View.INVISIBLE
    }

    override fun showServerError() {
        tvServerErrorAS.visibility = View.VISIBLE
    }

    override fun hideServerError() {
        tvServerErrorAS.visibility = View.INVISIBLE
    }

    override fun snackNetworkError() {
        Snackbar.make(recyclerViewAS, R.string.network_connecting_error, Snackbar.LENGTH_LONG).show()
    }

    override fun snackServerError() {
        Snackbar.make(recyclerViewAS, R.string.server_error, Snackbar.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        mAdapter.isShowProgressBar = true
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
    }

    override fun hideProgressBar() {
        mAdapter.isShowProgressBar = false
        mAdapter.notifyItemChanged(mAdapter.itemCount - 1)
    }

    override fun showNewestSearchResult(subjects: List<Subject>) {
        this.subjects.clear()
        this.subjects.addAll(subjects)
        mAdapter.notifyDataSetChanged()
    }

    override fun showMoreSearchResult(subjects: List<Subject>) {

        if (tag_box.visibility == View.INVISIBLE) {
            this.subjects.addAll(subjects)
            mAdapter.notifyDataSetChanged()
        }

    }


    override fun enableOnScrollListener() {
        onScrollListener.isWorking = true
    }

    override fun disableOnScrollListener() {
        onScrollListener.isWorking = false
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    override fun snackNoMoreFilm() {
        Snackbar.make(recyclerViewAS, getString(R.string.no_more_movie), Snackbar.LENGTH_SHORT).show()
    }

    override fun clearAllItem() {
        this.subjects.clear()
        mAdapter.notifyDataSetChanged()
    }

    override fun hideSoftKeyboard() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(etSearchContentAS.windowToken, 0)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
