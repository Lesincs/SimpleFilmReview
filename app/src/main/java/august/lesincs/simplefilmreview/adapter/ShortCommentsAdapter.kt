package august.lesincs.simplefilmreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.bean.Comment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_short_comments.view.*

/**
 * Created by Administrator on 2017/10/21.
 */
class ShortCommentsAdapter(val comments: List<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_TYPE_COMMENT = 1
    private val ITEM_TYPE_PROGRESSBAR = 2
    var isShowProgressBar = true
    lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> ITEM_TYPE_PROGRESSBAR
            else -> ITEM_TYPE_COMMENT
        }
    }

    override fun getItemCount(): Int = comments.size + 1

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        context = parent!!.context
        return when (viewType) {
            ITEM_TYPE_COMMENT -> NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_short_comments, parent, false))
            else -> ProgressBarViewHolder(LayoutInflater.from(context).inflate(R.layout.item_progress_bar, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NormalViewHolder) {
            val comment = comments[position]
            with(holder.itemView) {
                if (comment.rating.value.toFloat() == 0f) {
                    rbRateISC.visibility = View.INVISIBLE
                } else {
                    rbRateISC.visibility = View.VISIBLE
                }
                tvCommentISC.text = comment.content
                tvCreateAtISC.text = comment.created_at
                tvAuthorNameISC.text = comment.author.name
                tvThumbUpISC.text = comment.useful_count.toString()
                rbRateISC.rating = comment.rating.value.toFloat()
                Glide.with(context).load(comment.author.avatar).into(ivAuthorAvatarISC)
            }
        } else {
            if (isShowProgressBar) {
                holder.itemView?.layoutParams?.height = -2
            } else {
                holder.itemView?.layoutParams?.height = 0
            }
        }
    }

}