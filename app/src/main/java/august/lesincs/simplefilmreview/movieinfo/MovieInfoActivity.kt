package august.lesincs.simplefilmreview.movieinfo

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.CastAdapter
import august.lesincs.simplefilmreview.adapter.MOVIE_ID
import august.lesincs.simplefilmreview.bean.Cast
import august.lesincs.simplefilmreview.castinfo.ROLE_DIRECTOR
import august.lesincs.simplefilmreview.castinfo.ROLE_WRITER
import august.lesincs.simplefilmreview.moviecomment.MOVIE_NAME
import august.lesincs.simplefilmreview.moviecomment.MOVIE_RATING
import august.lesincs.simplefilmreview.retrofit.DoubanRetrofit
import august.lesincs.simplefilmreview.retrofit.MovieDetailService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_movie_info.*
import kotlinx.android.synthetic.main.common_toolbar.*

class MovieInfoActivity : AppCompatActivity() {
    private val casts = ArrayList<Cast>()
    private val adpter = CastAdapter(casts)
    var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_info)
        initView()
        getRemoteData()
    }


    private fun getRemoteData() {

        val progressDialog = ProgressDialog(this).apply { setCancelable(false);setMessage(getString(R.string.loading)) }
        progressDialog.show()

        DoubanRetrofit.create(MovieDetailService::class.java).getMovieDetail(intent.getStringExtra(MOVIE_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    it.directors.forEach {
                        val cast = Cast(it.alt, it.avatars, it.name, it.id, ROLE_DIRECTOR)
                        casts.add(cast)
                    }
                    it.writers.forEach {
                        val cast = Cast(it.alt, it.avatars, it.name, it.id, ROLE_WRITER)
                        casts.add(cast)
                    }
                    casts.addAll(it.casts)
                    adpter.notifyDataSetChanged()
                    toolbar.title = it.title //标题
                    tvSummary.text = it.summary  //摘要

                    val set = HashSet<String>(it.genres)
                    set.forEachIndexed { index, s ->
                        if (index != set.size - 1) {
                            tvType.append(s + "·")
                        } else {
                            tvType.append(s)
                        }
                    }  //类型
                    tvYear.append(it.pubdates.joinToString())
                    tvArea.append(it.countries.joinToString("/"))
                    tvOriginalName.append(it.original_title)  //原名
                    tvOtherTransName.append(it.aka.joinToString("/"))
                    tvRatingCount.text = it.ratings_count.toString() + "人评分"//评分人数
                    tvMovieDuration.append(it.durations.joinToString())
                    tvWatchRecord.append(it.collect_count.toString() + "人看过" + " " + "·" + " " + it.wish_count.toString() + "人想看")
                    url = it.alt
                    progressDialog.dismiss()
                }, {
                    progressDialog.dismiss()
                    Snackbar.make(toolbar, R.string.server_error, Snackbar.LENGTH_SHORT).show()
                }
                )
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        toolbar.title = intent.getStringExtra(MOVIE_NAME)
        tvRaing.text = intent.getFloatExtra(MOVIE_RATING, 0f).toString()
        mrbRating.rating = intent.getFloatExtra(MOVIE_RATING, 0f) / 2
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewAMI.isNestedScrollingEnabled = false
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerViewAMI.layoutManager = linearLayoutManager
        recyclerViewAMI.adapter = adpter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_open_with_browser, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_open_with_browser -> {
                url?.let {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}


fun List<String>.joinToString(separator: String): String {
    return StringBuilder().apply {
        forEachIndexed { index: Int, s: String ->
            if (index == size - 1) {
                append(s)
            } else {
                append(s)
                append(separator)
            }
        }
    }.toString()

}




