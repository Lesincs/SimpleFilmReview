package august.lesincs.simplefilmreview.bean

/**
 * Created by Administrator on 2017/11/4.
 */

data class CastInfoBean(
        val website: String,
        val mobile_url: String,
        val aka_en: List<String>,
        val name: String,
        val works: List<Work>,
        val name_en: String,
        val gender: String,
        val professions: List<String>,
        val avatars: Avatars,
        val summary: String,
        val photos: List<Photo>,
        val birthday: String,
        val aka: List<String>,
        val alt: String,
        val born_place: String,
        val constellation: String,
        val id: String
)


data class Photo(
        val thumb: String,
        val image: String,
        val cover: String,
        val alt: String,
        val id: String,
        val icon: String
)

data class Work(
        val roles: List<String>,
        val subject: Subject
)





