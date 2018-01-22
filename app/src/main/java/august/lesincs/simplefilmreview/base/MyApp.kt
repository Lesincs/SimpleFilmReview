package august.lesincs.simplefilmreview.base

import android.app.Application
import android.content.Context
import org.litepal.LitePal


/**
 * Created by Administrator on 2017/11/2.
 */


class MyApp() : Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        LitePal.initialize(context)
    }

    companion object {
        lateinit var context:Context
    }

}