package com.tinfive.nearbyplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.*
import com.tinfive.nearbyplace.networks.EndPoint
import com.tinfive.nearbyplace.utils.getProgressDrawable
import com.tinfive.nearbyplace.utils.loadImage
import kotlinx.android.synthetic.main.cardview_masjidsekitar.view.*


class ListMasjidAdapter(var masjid: MutableList<Masjid>) :
    RecyclerView.Adapter<ListMasjidAdapter.MasjidViewHolder>(), Filterable {

    private var context: Context? = null
    private var masjidListFilter: MutableList<Masjid>

    init {
        masjidListFilter = masjid
    }

    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemSelected(masjides: Masjid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MasjidViewHolder {
        context = parent.context
        val layoutInflate = LayoutInflater.from(context).inflate(R.layout.cardview_masjidsekitar, parent, false)
        return MasjidViewHolder(layoutInflate)
    }

    override fun getItemCount(): Int = masjidListFilter.size


    override fun onBindViewHolder(holder: MasjidViewHolder, position: Int) {
        holder.clear()
        holder.bind(position)
    }

    fun updateMasjid(masjidApiUpdate: List<Masjid>) {
        this.masjid.clear()
        this.masjid.addAll(masjidApiUpdate)
        notifyDataSetChanged()

    }

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
            itemView.descTiming.text = "otw"
            itemView.iconIv.setImageDrawable(null)
        }

        fun bind(position: Int) {
            val (
                mosqueId,
                mosqueType,
                mosqueCode,
                mosqueName,
                mosqueIdentity,
                surfaceArea,
                buildingArea,
                kelId,
                los,
                since,
                rek,
                address,
                latitude,
                longitude,
                estimate,
                estimateDate,
                pic,
                description,
                bank,
                province,
                regency,
                district,
                village
            ) = masjidListFilter[position]
            inflateData(
                mosqueId,
                mosqueType,
                mosqueCode,
                mosqueName,
                mosqueIdentity,
                surfaceArea,
                buildingArea,
                kelId,
                los,
                since,
                rek,
                address,
                latitude,
                longitude,
                estimate,
                estimateDate,
                pic,
                description,
                bank,
                province,
                regency,
                district,
                village
            )
        }

        private fun inflateData(
            mosqueId: Int,
            mosqueType: String,
            mosqueCode: String,
            mosqueName: String,
            mosqueIdentity: String,
            surfaceArea: String,
            buildingArea: String,
            kelId: String,
            los: String,
            since: String,
            rek: String,
            address: String,
            latitude: String,
            longitude: String,
            estimate: String,
            estimateDate: String,
            pic: String,
            description: String,
            bank: Bank,
            province: Province,
            regency: Regency,
            district: District,
            village: String
        ) {
            val progressDrawable: CircularProgressDrawable = getProgressDrawable(itemView.context)
            val imgTarget = EndPoint.imageUrlPath

            mosqueName.let {
                itemView.titleTv.text = it
            }
            address.let {
                itemView.descTv.text = it
//                    String.format("%s, %s, %s", it, province.provinceName, regency.regencyName, district.districtName)
            }
            pic.let {
                itemView.iconIv.loadImage(imgTarget + it, progressDrawable)
            }

            itemView.setOnClickListener (this)
        }
    }

    //GET MAsJID FOR SEARCH OPTIONS
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    masjidListFilter = masjid
                } else {
                    val filteredList: MutableList<Masjid> = ArrayList()
//                    println("DATA ${listOf(filteredList)}")
                    for (row in masjid) {
                        if (row.mosqueName.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    masjidListFilter = filteredList
                }
                val filterResults = FilterResults()

                filterResults.values = masjidListFilter
//                println("DATA ADAPTER ${filterResults}")
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                masjidListFilter = results.values as ArrayList<Masjid>
                notifyDataSetChanged()
            }
        }
    }

}


