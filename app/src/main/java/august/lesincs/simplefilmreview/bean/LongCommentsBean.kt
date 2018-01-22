package august.lesincs.simplefilmreview.bean

import java.io.Serializable

/**
 * Created by Administrator on 2017/10/13.
 */
data class LongCommentsBean(
        val count: Int,
        val reviews: List<Review>,
        val start: Int,
        val total: Int,
        val next_start: Int,
        val subject: Subject1
) : Serializable

data class Review(
        var rating: RattingPersonal,
        var useful_count: Int,
        var author: Author,
        var created_at: String,
        var title: String,
        var updated_at: String,
        var share_url: String,
        var summary: String,
        var content: String,
        var useless_count: Int,
        var comment_count: Int,
        var ait: String,
        var id: String,
        var subject_id: String

) : Serializable


data class Author(
        var uid: String,
        var avatar: String,
        var signature: String,
        var ait: String,
        var id: String,
        var name: String
) : Serializable

