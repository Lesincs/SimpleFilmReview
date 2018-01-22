package august.lesincs.simplefilmreview.util

import android.content.Context
import android.net.ConnectivityManager
import august.lesincs.simplefilmreview.base.MyApp

/**
 * Created by Administrator on 2017/11/2.
 */
class NetWorkUtil {

    companion object {
        fun isNetWorkAvailable(): Boolean {
            val manager = MyApp.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return when (manager.activeNetworkInfo) {
                null -> false
                else -> manager.activeNetworkInfo.isAvailable
            }
        }
    }

}