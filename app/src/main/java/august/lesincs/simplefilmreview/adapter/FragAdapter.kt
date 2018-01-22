package august.lesincs.simplefilmreview.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Administrator on 2017/9/2.
 */
class FragAdapter(fm: FragmentManager?, private val titles: List<String>, val frags: List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) = frags[position]
    override fun getCount() = frags.size
    override fun getPageTitle(position: Int) = titles[position % titles.size]
}

