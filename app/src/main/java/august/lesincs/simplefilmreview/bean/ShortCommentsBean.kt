package august.lesincs.simplefilmreview.bean

import java.io.Serializable

/**
 * Created by Administrator on 2017/10/14.
 */

data class ShortCommentsBean(
        val count: Int,
        val comments: List<Comment>,
        val start: Int,
        val total: Int,
        val next_start: Int,
        val subject: Subject1
)

data class Subject1(
        val rating: Rating,
        val genres: List<String>,
        val title: String,
        val casts: List<Cast>,
        val durations: List<String>,
        val collect_count: Int,
        val mainland_pubdate: String,
        val has_video: Boolean,
        val original_title: String,
        val subtype: String,
        val directors: List<Director>,
        val pubdates: List<String>,
        val year: String,
        val images: Images,
        val alt: String,
        val id: String
) : Serializable


data class Comment(
        val rating: RattingPersonal,
        val useful_count: Int,
        val author: Author,
        val subject_id: String,
        val content: String,
        val created_at: String,
        val id: String
) : Serializable


data class RattingPersonal(
        val max: Int,
        val value: Double,
        val min: Int
) : Serializable



