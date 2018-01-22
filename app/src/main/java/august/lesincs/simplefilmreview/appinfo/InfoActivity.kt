package august.lesincs.simplefilmreview.appinfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import august.lesincs.simplefilmreview.R
import kotlinx.android.synthetic.main.common_toolbar.*

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        setSupportActionBar(toolbar)
        toolbar.title = getText(R.string.toolbar_title_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
