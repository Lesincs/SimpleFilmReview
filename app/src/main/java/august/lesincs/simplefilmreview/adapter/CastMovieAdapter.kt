package august.lesincs.simplefilmreview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.bean.Subject
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cast_movie.view.*


/**
 * Created by Administrator on 2017/11/4.
 */
class CastMovieAdapter(val subjects: List<Subject>) : RecyclerView.Adapter<CastMovieAdapter.ViewHolder>() {

    lateinit var context: Context
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            val subject = subjects[position]
            tvRatingICM.text = subject.rating.average.toString()+"评分"
            Glide.with(context).load(subject.images.large).into(ivMoviePosterICM)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        context = parent!!.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cast_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return subjects.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}