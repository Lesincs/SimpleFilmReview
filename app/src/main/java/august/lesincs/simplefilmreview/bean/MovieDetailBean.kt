package august.lesincs.simplefilmreview.bean

import august.lesincs.simplefilmreview.castinfo.ROLE_ACTOR
import org.litepal.crud.DataSupport
import java.io.Serializable

/**
 * Created by Administrator on 2017/9/22.
 */

data class MovieDetailBean(
        val rating: Rating,
        val reviews_count: Int,
        val wish_count: Int,
        val douban_site: String,
        val year: String,
        val images: Images,
        val alt: String,
        val id: String,
        val mobile_url: String,
        val title: String,
        val do_count: String,
        val share_url: String,
        val seasons_count: String,
        val schedule_url: String,
        val episodes_count: String,
        val countries: List<String>,
        val genres: List<String>,
        val collect_count: Int,
        val casts: List<Cast>,
        val current_season: String,
        val original_title: String,
        val summary: String,
        val subtype: String,
        val directors: List<Director>,
        val comments_count: Int,
        val ratings_count: Int,
        val aka: List<String>,
        val durations: List<String>,
        val pubdates: List<String>,
        val writers: List<Writer>
) : Serializable

data class Images(
        val small: String,
        val large: String,
        val medium: String
) : Serializable

data class Director(
        val alt: String,
        val avatars: Avatars,
        val name: String,
        val id: String
) : Serializable


data class Rating(
        val max: Int, //10
        val average: Double, //7.4
        val stars: String, //40
        val min: Int //0
) : Serializable, DataSupport()

data class Cast(
        val alt: String, //https://movie.douban.com/celebrity/1054395/
        val avatars: Avatars,
        val name: String, //伊利亚·伍德
        val id: String,  //1054395
        val tag: String? = ROLE_ACTOR
) : Serializable, DataSupport() {


}

data class Avatars(
        val small: String, //http://img3.doubanio.com/img/celebrity/small/51597.jpg
        val large: String, //http://img3.doubanio.com/img/celebrity/large/51597.jpg
        val medium: String //http://img3.doubanio.com/img/celebrity/medium/51597.jpg
) : Serializable

data class Writer(
        val avatars: Avatars,
        val name_en: String, //Pamela Pettler
        val name: String, //帕米拉·帕特勒
        val alt: String, //https://movie.douban.com/celebrity/1301900/
        val id: String //1301900
) : Serializable