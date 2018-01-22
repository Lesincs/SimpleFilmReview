package august.lesincs.simplefilmreview.adapter

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.bean.Review
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_long_comments_detail.view.*
import kotlinx.android.synthetic.main.item_long_comments.view.*

/**
 * Created by Administrator on 2017/10/21.
 */


class LongCommentsAdapter(val reviews: ArrayList<Review>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_TYPE_COMMENT = 1
    val ITEM_TYPE_PROGRESSBAR = 2
    lateinit var context: Context
    var isShowProgressBar = true


    inner class NormalHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    inner class ProgressHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = reviews.size + 1
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        if (holder is NormalHolder) {
            val review = reviews[position]
            with(holder.itemView) {
                tvCommentContentILC.text = review.summary
                tvAuthorNameILC.text = review.author.name
                tvCreateAtILC.text = review.created_at
                Glide.with(context).load(review.author.avatar).into(ivAuthorAvatarILC)
                tvCommentTitleILC.text = review.title
                rbRateILC.rating = review.rating.value.toFloat()
                tvThumbUpILC.text = review.useful_count.toString()
                tvThumbDownILC.text = review.useless_count.toString()
                setOnClickListener {
                    val bottomSheetFrag = BottomSheetDialog(context)
                    val view = LayoutInflater.from(context).inflate(R.layout.dialog_long_comments_detail, null)
                    with(view){
                        tvCommentTitleDLCD.text = review.title
                        ivDismissDialogDLCD.setOnClickListener { bottomSheetFrag.dismiss()}
                        tvFullCommentDLCD.text=review.content
                    }
                    bottomSheetFrag.setContentView(view)
                    val behavior = BottomSheetBehavior.from(view.parent as View)
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                    bottomSheetFrag.show()
                }
            }
        } else {
            if (isShowProgressBar) {
                holder?.itemView?.layoutParams?.height = -2
            } else {
                holder?.itemView?.layoutParams?.height = 0
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        context = parent?.context!!
        return when (viewType) {
            ITEM_TYPE_COMMENT -> NormalHolder(LayoutInflater.from(context).inflate(R.layout.item_long_comments, parent, false))
            else -> ProgressHolder(LayoutInflater.from(context).inflate(R.layout.item_progress_bar, parent, false))
        }
    }


    override fun getItemViewType(position: Int): Int {

        return when (position) {
            itemCount - 1 -> ITEM_TYPE_PROGRESSBAR
            else -> {
                ITEM_TYPE_COMMENT
            }
        }
    }

}