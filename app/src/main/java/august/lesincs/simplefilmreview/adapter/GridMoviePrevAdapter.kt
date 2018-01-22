package august.lesincs.simplefilmreview.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.bean.Subject
import august.lesincs.simplefilmreview.moviecomment.CommentActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie_prev_grid.view.*


/**
 * Created by Administrator on 2017/9/3.
 */

const val MOVIE_ID = "MOVIE_ID"
const val MOVIE_SUBJECT = "MOVIE_SUBJECT"

class MoviePrevAdapter(val subjects: ArrayList<Subject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    private val ITEM_TYPE_MOVIE = 1
    private val ITEM_TYPE_PROGRESSBAR = 2
    var isShowProgressBar = true

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is NormalViewHolder) {
            val movie = subjects[position]
            with(holder.itemView) {
                Glide.with(context).load(movie.images.large)
                        .placeholder(R.drawable.img_movie_prev_place_holder)
                        .crossFade()
                        .into(ivMoviePosterIMPG)
                tvMovieNameIMPG.text = movie.title
                tvMovieRateIMPG.text = movie.rating.average.toString()
                rbMovieRateIMPG.rating = movie.rating.average.toFloat() / 2
            }
        } else {
            val p = holder!!.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            p.isFullSpan = true
            if (isShowProgressBar) {
                holder.itemView.layoutParams.height = -2
            } else {
                holder.itemView.layoutParams.height = 0
            }
            if (itemCount == 1) {
                holder.itemView.layoutParams.height = 0
            }
        }
    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra(MOVIE_ID, subjects[adapterPosition].id)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, itemView.ivMoviePosterIMPG as View, "movie_poster");
                intent.putExtra(MOVIE_SUBJECT, subjects[adapterPosition])
                (context as Activity).startActivity(intent, options.toBundle())
            }
        }
    }

    inner class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        context = parent!!.context
        return when (viewType) {
            ITEM_TYPE_MOVIE -> NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie_prev_grid, parent, false))
            else -> ProgressBarViewHolder(LayoutInflater.from(context).inflate(R.layout.item_progress_bar, parent, false))
        }
    }

    override fun getItemCount(): Int = subjects.size + 1

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> ITEM_TYPE_PROGRESSBAR
            else -> ITEM_TYPE_MOVIE
        }
    }

}



