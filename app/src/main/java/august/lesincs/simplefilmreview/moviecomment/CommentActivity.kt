package august.lesincs.simplefilmreview.moviecomment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.view.Menu
import android.view.MenuItem
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.FragAdapter
import august.lesincs.simplefilmreview.adapter.MOVIE_ID
import august.lesincs.simplefilmreview.adapter.MOVIE_SUBJECT
import august.lesincs.simplefilmreview.bean.DBSubject
import august.lesincs.simplefilmreview.bean.Subject
import august.lesincs.simplefilmreview.movieinfo.MovieInfoActivity
import august.lesincs.simplefilmreview.util.NetWorkUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import kotlinx.android.synthetic.main.activity_comment.*
import org.litepal.crud.DataSupport

const val MOVIE_NAME = "MOVIE_NAME"
const val MOVIE_RATING = "MOVIE_RATING"

class CommentActivity : AppCompatActivity() {
    lateinit var movieImageUrl: String
    lateinit var movieName: String
    lateinit var menu: Menu
    var movieRating: Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        initView()
        handleIntentData()

    }

    private fun handleIntentData() {
        val serializable = intent.getSerializableExtra(MOVIE_SUBJECT)

        if (serializable is DBSubject) {
            movieImageUrl = serializable.imageUrl
            movieName = serializable.movieName
            movieRating = serializable.rating
        } else if (serializable is Subject) {

            movieImageUrl = serializable.images.large
            movieName = serializable.title
            movieRating = serializable.rating.average.toFloat()
        }


        Glide.with(this).load(movieImageUrl).asBitmap().into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                ivMoviePosterAR.setImageBitmap(resource)
                Palette.from(resource).generate({
                    val color = it.getMutedColor(resources.getColor(R.color.default_background))
                    ctlAR.setContentScrimColor(color)
                    ctlAR.setStatusBarScrimColor(color)
                    ctlAR.setBackgroundColor(color)
                    tabLayoutAR.setBackgroundColor(color)
                })
            }
        })
        ctlAR.title = movieName
        rbMovieRateAR.rating = movieRating / 2
        tvMovieRateAR.text = movieRating.toString()

    }

    private fun initView() {
        setSupportActionBar(toolbarAR)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPagerAR.adapter = FragAdapter(supportFragmentManager, listOf(getString(R.string.long_comments), getString(R.string.short_comments)), listOf<android.support.v4.app.Fragment>(LongCommentsFrag(), ShortCommentsFrag()))
        tabLayoutAR.setupWithViewPager(viewPagerAR)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        this.menu = menu

        val list = DataSupport.where("movieId=?", intent.getStringExtra(MOVIE_ID)).find(DBSubject::class.java)
        if (!list.isEmpty()) {
            menuInflater.inflate(R.menu.menu_activity_comment_collected, menu)
        } else {
            menuInflater.inflate(R.menu.menu_activity_comment_not_collect, menu)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_collect -> {
                Snackbar.make(toolbarAR, getText(R.string.collect_success), Snackbar.LENGTH_SHORT).show()
                val dbSubject = DBSubject(intent.getStringExtra(MOVIE_ID), movieName, movieImageUrl, movieRating)
                dbSubject.save()
                menu.clear()
                menuInflater.inflate(R.menu.menu_activity_comment_collected, menu)
            }

            R.id.action_cancel_collection -> {
                Snackbar.make(toolbarAR, getText(R.string.cancel_collect_success), Snackbar.LENGTH_SHORT).show()
                DataSupport.deleteAll(DBSubject::class.java, "movieId=?", intent.getStringExtra(MOVIE_ID))
                menu.clear()
                menuInflater.inflate(R.menu.menu_activity_comment_not_collect, menu)
            }
            R.id.action_details -> if (NetWorkUtil.isNetWorkAvailable()) {
                startActivity(Intent(this, MovieInfoActivity::class.java).putExtra(MOVIE_ID, intent.getStringExtra(MOVIE_ID)).putExtra(MOVIE_NAME, movieName).putExtra(MOVIE_RATING, movieRating))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {

                Snackbar.make(viewPagerAR, R.string.network_connecting_error, Snackbar.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
