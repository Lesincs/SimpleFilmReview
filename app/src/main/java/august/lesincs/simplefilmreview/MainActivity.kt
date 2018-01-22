package august.lesincs.simplefilmreview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import august.lesincs.simplefilmreview.adapter.FragAdapter
import august.lesincs.simplefilmreview.collection.CollectionActivity
import august.lesincs.simplefilmreview.appinfo.InfoActivity
import august.lesincs.simplefilmreview.movieprev.MOVIE_PREV_TYPE
import august.lesincs.simplefilmreview.movieprev.MoviePrevFrag
import august.lesincs.simplefilmreview.movieprev.MoviePrevType
import august.lesincs.simplefilmreview.search.SearchActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var lastOnBackPressTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbarAM)
        supportActionBar?.title = getText(R.string.app_name)
        viewPageAM.offscreenPageLimit = 2
        val titles = arrayListOf<String>(getString(R.string.tab_douban250), getString(R.string.tab_trending), getString(R.string.tab_coming_soon))

        //初始化frag的时候设置参数 以加载不同类别电影
        val frags = arrayListOf<Fragment>(
                MoviePrevFrag().apply { arguments = Bundle().apply { putSerializable(MOVIE_PREV_TYPE, MoviePrevType.TOP250) } },
                MoviePrevFrag().apply { arguments = Bundle().apply { putSerializable(MOVIE_PREV_TYPE, MoviePrevType.INTHRATERS) } },
                MoviePrevFrag().apply { arguments = Bundle().apply { putSerializable(MOVIE_PREV_TYPE, MoviePrevType.COMINGSOON) } }
        )
        viewPageAM.adapter = FragAdapter(supportFragmentManager, titles, frags)
        tabLayoutAM.setupWithViewPager(viewPageAM, true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, SearchActivity::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
            R.id.action_collection -> startActivity(Intent(this, CollectionActivity::class.java))
            R.id.action_about -> startActivity(Intent(this, InfoActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val currentOnBackPressTime = System.currentTimeMillis()
        if (currentOnBackPressTime - lastOnBackPressTime < 2000) {
            super.onBackPressed()
        } else {
            Snackbar.make(viewPageAM, R.string.retry_to_quit_app, Snackbar.LENGTH_SHORT).show()
            lastOnBackPressTime = currentOnBackPressTime
        }
    }
}


//扩展函数 方便toast
fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

