package august.lesincs.simplefilmreview.collection

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MenuItem
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.adapter.CollectionMovieAdapter
import august.lesincs.simplefilmreview.bean.DBSubject
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.common_toolbar.*
import org.litepal.crud.DataSupport

class CollectionActivity : AppCompatActivity() {

    private var collectionMovieCount = 0
    private val dbSubjects = ArrayList<DBSubject>()
    private val adapter = CollectionMovieAdapter(dbSubjects)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection)
        initView()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        toolbar.title = getText(R.string.toolbar_title_collection)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mRecyclerViewAC.adapter = adapter
        mRecyclerViewAC.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onResume() {
        super.onResume()
        val list = DataSupport.findAll(DBSubject::class.java)
        if (list.size != collectionMovieCount) {
            dbSubjects.clear()
            dbSubjects.addAll(list)
            adapter.notifyDataSetChanged()
            collectionMovieCount = list.size
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
