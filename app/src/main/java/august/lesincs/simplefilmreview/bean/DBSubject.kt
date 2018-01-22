package august.lesincs.simplefilmreview.bean

import org.litepal.crud.DataSupport
import java.io.Serializable

/**
 * Created by Administrator on 2017/10/25.
 */
data class DBSubject(var movieId: String = "",
                     var movieName: String = "",
                     var imageUrl: String = "",
                     var rating: Float = 0f) : Serializable, DataSupport()

