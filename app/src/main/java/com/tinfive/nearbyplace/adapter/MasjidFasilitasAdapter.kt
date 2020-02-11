package com.tinfive.nearbyplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.Masjid
import com.tinfive.nearbyplace.networks.EndPoint
import com.tinfive.nearbyplace.utils.getProgressDrawable
import com.tinfive.nearbyplace.utils.loadImage
import kotlinx.android.synthetic.main.cardview_masjidsekitar.view.*

class MasjidFasilitasAdapter(
    var context: Context,
    var masjid: List<Masjid>
) :
    RecyclerView.Adapter<MasjidFasilitasAdapter.MasjidViewHolder>() {

    //    private var context: Context? = null
    private var masjidListFilter: List<Masjid> = masjid

    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemSelected(masjides: Masjid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasjidViewHolder {
        context = parent.context
        val layoutInflate =
            LayoutInflater.from(context).inflate(R.layout.cardview_masjidsekitar, parent, false)
        return MasjidViewHolder(layoutInflate)
    }

    override fun getItemCount(): Int = masjidListFilter.size

    internal fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    inner class MasjidViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(v: View?) {
            mOnItemClickListener?.onItemSelected(masjidListFilter[adapterPosition])
        }


        fun clear() {
            itemView.titleTv.text = ""
            itemView.descTv.text = ""
            itemView.descTiming.text = "fasilitas"
            itemView.iconIv.setImageDrawable(null)
        }

        fun bind(position: Int) {
            val (
                mosqueId,
                mosqueType,
                mosqueCode,
                mosqueName,
                mosqueAddress,
                mosqueLat,
                mosqueLng,
                mosquePict
            ) = masjidListFilter[position]
            inflateData(
                mosqueId,
                mosqueType,
                mosqueCode,
                mosqueName,
                mosqueAddress,
                mosqueLat,
                mosqueLng,
                mosquePict
            )
        }

        private fun inflateData(
            mosqueId: Int,
            mosqueType: String,
            mosqueCode: String,
            mosqueName: String,
            mosqueAddress: String,
            mosqueLat: String,
            mosqueLng: String,
            mosquePict: String
        ) {
            val progressDrawable: CircularProgressDrawable = getProgressDrawable(itemView.context)
            val imgTarget = EndPoint.imageUrlPath

            mosqueName.let {
                itemView.titleTv.text = it
            }
            mosqueAddress.let {
                itemView.descTv.text = it
//                    String.format("%s, %s, %s", it, province.provinceName, regency.regencyName, district.districtName)
            }
            mosquePict.let {
                itemView.iconIv.loadImage(imgTarget + it, progressDrawable)
            }

            itemView.setOnClickListener(this)
        }
    }

    override fun onBindViewHolder(holder: MasjidFasilitasAdapter.MasjidViewHolder, position: Int) {
        holder.clear()
        holder.bind(position)
    }

}


