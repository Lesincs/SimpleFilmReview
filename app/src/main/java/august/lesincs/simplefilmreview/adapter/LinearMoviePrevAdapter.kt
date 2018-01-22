package august.lesincs.simplefilmreview.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.bean.Cast
import august.lesincs.simplefilmreview.bean.Subject
import august.lesincs.simplefilmreview.moviecomment.CommentActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie_prev_linear.view.*


/**
 * Created by Administrator on 2017/10/25.
 */
class LinearMoviePrevAdapter(val subjects: ArrayList<Subject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    private val ITEM_TYPE_MOVIE = 1
    private val ITEM_TYPE_PROGRESSBAR = 2
    var isShowProgressBar = true

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is NormalViewHolder) {
            val movie = subjects[position]
            with(holder.itemView) {
                //防止回收复用
                tvMovieTypeIMPL.text = context.getText(R.string.movie_type_base)
                tvMovieActorsIMPL.text = context.getText(R.string.movie_actors_base)
                Glide.with(context).load(movie.images.large)
                        .placeholder(R.drawable.img_movie_prev_place_holder)
                        .crossFade()
                        .into(ivMoviePosterIMPL)
                tvMovieNameIMPL.text = movie.title
                tvMovieRateIMPL.text = movie.rating.average.toString()
                rbMovieRateIMPL.rating = movie.rating.average.toFloat() / 2
                val set = HashSet<String>(movie.genres)
                set.forEachIndexed { index, s ->
                    if (index != set.size - 1) {
                        tvMovieTypeIMPL.append(s + "·")
                    } else {
                        tvMovieTypeIMPL.append(s)
                    }
                }
                val set2 = HashSet<Cast>(movie.casts)
                set2.forEachIndexed { index, cast ->
                    if (index != set2.size - 1) {
                        tvMovieActorsIMPL.append(cast.name + "/")
                    } else {
                        tvMovieActorsIMPL.append(cast.name)
                    }
                }
            }
        } else {
            if (!isShowProgressBar || itemCount == 1) {
                holder?.itemView?.layoutParams?.height = 0
            } else {
                holder?.itemView?.layoutParams?.height = -2
            }
        }

    }

    inner class NormalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra(MOVIE_ID, subjects[adapterPosition].id)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, itemView.ivMoviePosterIMPL as View, "movie_poster");
                intent.putExtra(MOVIE_SUBJECT, subjects[adapterPosition])
                (context as Activity).startActivity(intent, options.toBundle())
            }
        }
    }

    inner class ProgressBarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        context = parent!!.context
        return when (viewType) {
            ITEM_TYPE_MOVIE -> NormalViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie_prev_linear, parent, false))
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