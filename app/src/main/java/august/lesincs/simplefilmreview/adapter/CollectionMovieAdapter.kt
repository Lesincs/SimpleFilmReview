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
import august.lesincs.simplefilmreview.bean.DBSubject
import august.lesincs.simplefilmreview.moviecomment.CommentActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_movie_prev_grid.view.*


/**
 * Created by Administrator on 2017/10/28.
 */
class CollectionMovieAdapter(val dbSubjects: List<DBSubject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        context = parent?.context!!
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movie_prev_grid, parent, false))
    }

    inner private class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener({
                val intent = Intent(context, CommentActivity::class.java)
                intent.putExtra(MOVIE_ID, dbSubjects[adapterPosition].movieId)
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context as Activity, itemView.ivMoviePosterIMPG as View, "movie_poster");
                intent.putExtra(MOVIE_SUBJECT, dbSubjects[adapterPosition])
                (context as Activity).startActivity(intent, options.toBundle())
            })

        }
    }

    override fun getItemCount(): Int = dbSubjects.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {

        with(holder!!.itemView) {
            val dbSubject = dbSubjects[position]
            Glide.with(context).load(dbSubject.imageUrl).into(ivMoviePosterIMPG)
            tvMovieNameIMPG.text = dbSubject.movieName
            tvMovieRateIMPG.text = dbSubject.rating.toString()
            rbMovieRateIMPG.rating = dbSubject.rating / 2
        }
    }


}