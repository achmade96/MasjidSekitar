package com.tinfive.nearbyplace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.MosqueFacilities

class FasilitasAdapter(var fasilitasList: MutableList<Fasilitas>) :
    RecyclerView.Adapter<FasilitasAdapter.FasilitasViewHolder>() {

    private lateinit var masjidFasilitas: MutableList<FasilitasFragment>
    private var context: Context? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FasilitasViewHolder {
        context = parent.context
        val layoutInflate =
            LayoutInflater.from(context).inflate(R.layout.bottom_sheet, parent, false)
        return FasilitasViewHolder(layoutInflate)
    }

    override fun getItemCount(): Int = masjidFasilitas.size

    override fun onBindViewHolder(holder: FasilitasViewHolder, position: Int) {
        holder.clear()
        holder.onBind(position)
    }

    fun updateListFasilitas(falItem: List<Fasilitas>) {
        this.fasilitasList.clear()
        this.fasilitasList.addAll(falItem)
        notifyDataSetChanged()
    }

    inner class FasilitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun clear() {
        }

        fun onBind(position: Int) {
            val (
                id: Int,
                name: String,
                mosque: MosqueFacilities) = fasilitasList[position]
            inflateData(
                id,
                name,
                mosque
            )
        }
    }

    fun inflateData(id: Int, name: String, mosque: MosqueFacilities) {
        name.let {
        }
    }
}
