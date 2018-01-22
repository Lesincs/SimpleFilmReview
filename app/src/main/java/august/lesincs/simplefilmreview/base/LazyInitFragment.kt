package august.lesincs.simplefilmreview.base

import android.os.Bundle
import android.view.View

/**
 * Created by Administrator on 2017/10/17.
 */
abstract class LazyInitFragment : BaseFrag() {
    abstract override fun getLayoutId(): Int
    protected var isPrepareView = false
    private var isVisibleToUser = false
    protected var isInitData = false
    abstract fun loadData()
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepareView = true

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        lazyInitData()
    }

    protected fun lazyInitData() {

        if (isPrepareView && this.isVisibleToUser && !isInitData) {
            isInitData = true
            loadData()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        lazyInitData()
    }
}