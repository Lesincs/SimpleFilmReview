package august.lesincs.simplefilmreview.util

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Administrator on 2017/11/2.
 */
abstract class MyObserver<T> : Observer<T> {

    abstract override fun onNext(t: T)

    abstract override fun onError(e: Throwable)

    override fun onSubscribe(d: Disposable) {
        if (!NetWorkUtil.isNetWorkAvailable()) {
            onNetWorkNotAvailable()
            d.dispose()
        }
    }

    abstract fun onNetWorkNotAvailable()

    override fun onComplete() {}

}