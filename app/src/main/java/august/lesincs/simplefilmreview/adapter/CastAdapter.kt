package august.lesincs.simplefilmreview.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import august.lesincs.simplefilmreview.R
import august.lesincs.simplefilmreview.bean.Cast
import august.lesincs.simplefilmreview.castinfo.CastInfoActivity
import august.lesincs.simplefilmreview.castinfo.ROLE_ACTOR
import august.lesincs.simplefilmreview.castinfo.ROLE_DIRECTOR
import august.lesincs.simplefilmreview.castinfo.ROLE_WRITER
import august.lesincs.simplefilmreview.util.NetWorkUtil
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_cast.view.*

/**
 * Created by Administrator on 2017/11/3.
 */
const val CAST_ID = "CAST_ID"
const val CAST_AVATARS = "CAST_AVATARS"
const val CAST_NAME = "CAST_NAME"

class CastAdapter(private val casts: List<Cast>) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        with(holder!!.itemView) {
            val cast = casts[position]
            tvCastNameIC.text = cast.name

            when (cast.tag) {
                ROLE_DIRECTOR -> tvRoleIC.text = ROLE_DIRECTOR
                null -> tvRoleIC.text = ROLE_ACTOR
                ROLE_WRITER -> tvRoleIC.text = ROLE_WRITER
            }


            Glide.with(context).load(cast.avatars.large).centerCrop().into(ivCastPicIC)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        context = parent?.context!!
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cast, parent, false))
    }

    lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {

                if (NetWorkUtil.isNetWorkAvailable())
                {
                    (context as Activity).startActivity(Intent(context, CastInfoActivity::class.java)
                            .putExtra(CAST_ID, casts[adapterPosition].id)
                            .putExtra(CAST_AVATARS, casts[adapterPosition].avatars.large)
                            .putExtra(CAST_NAME, casts[adapterPosition].name))
                    (context as Activity).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return casts.size
    }
}