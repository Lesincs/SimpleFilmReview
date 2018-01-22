package august.lesincs.simplefilmreview.castinfo

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
import august.lesincs.simplefilmreview.adapter.CAST_AVATARS
import august.lesincs.simplefilmreview.adapter.CAST_ID
import august.lesincs.simplefilmreview.adapter.CAST_NAME
import august.lesincs.simplefilmreview.adapter.LinearMoviePrevAdapter
import august.lesincs.simplefilmreview.bean.Subject
import august.lesincs.simplefilmreview.retrofit.CastInfoService
import august.lesincs.simplefilmreview.retrofit.DoubanRetrofit
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cast_info.*
import kotlinx.android.synthetic.main.common_toolbar.*

const val ROLE_ACTOR = "主演"
const val ROLE_WRITER = "编剧"
const val ROLE_DIRECTOR = "导演"

class CastInfoActivity : AppCompatActivity() {
    val subjects = ArrayList<Subject>()
    val adapter by lazyOf(LinearMoviePrevAdapter(subjects))
    val progressDialog by lazy { ProgressDialog(this).apply { setCancelable(false);setMessage(getString(R.string.loading)) } }
     var url: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cast_info)

        initView()
        handleIntentData()
        getRemoteData()

    }

    private fun handleIntentData() {
        supportActionBar?.title = intent.getStringExtra(CAST_NAME)
        Glide.with(this).load(intent.getStringExtra(CAST_AVATARS)).into(ivCastPicACI)
    }


    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewACI.isNestedScrollingEnabled = false
        recyclerViewACI.adapter = adapter
        adapter.isShowProgressBar = false
        recyclerViewACI.layoutManager = linearLayoutManager
    }

    private fun getRemoteData() {
       progressDialog.show()
        DoubanRetrofit.create(CastInfoService::class.java).getCastInfo(intent.getStringExtra(CAST_ID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    tvCastNameACI.append(it.name)
                    tvEnglishNameACI.append(it.name_en)
                    tvConstellationACI.append(it.constellation)
                    tvProfessionACI.append(it.professions.joinToString("/"))
                    tvBirthdayACI.append(it.birthday)
                    tvBirthAreaACI.append(it.born_place)
                    tvCastSummary.text = it.summary
                    it.works.forEach {

                        subjects.add(it.subject)
                    }
                    adapter.notifyDataSetChanged()
                    url = it.alt
                    progressDialog.dismiss()

                }, {progressDialog.dismiss()
                    Snackbar.make(toolbar,R.string.server_error, Snackbar.LENGTH_SHORT).show()})
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_open_with_browser, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onBackPressed() {
        finish()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            android.R.id.home -> {overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)}
            R.id.action_open_with_browser -> {

                url?.let {  val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.data = Uri.parse(url)
                    startActivity(intent) }

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
