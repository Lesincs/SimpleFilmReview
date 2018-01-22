package august.lesincs.simplefilmreview.bean

import java.io.Serializable

/**
 * Created by Administrator on 2017/9/3.
 */

data class MoviePrevBean(
        var count: Int,
        var start: Int,
        var total: Int,
        var subjects: List<Subject>,
        var title: String
) : Serializable

data class Subject(
        var rating: Rating,
        var genres: List<String>,
        var title: String,
        var casts: List<Cast>,
        var collect_count: Int,
        var original_title: String,
        var subtype: String,
        var directors: List<Director>,
        var year: String,
        var images: Images,
        var alt: String,
        var id: String
) : Serializable











