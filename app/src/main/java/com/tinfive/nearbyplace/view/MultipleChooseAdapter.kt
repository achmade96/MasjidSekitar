package com.tinfive.nearbyplace.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.model.MosqueFacilities
import kotlinx.android.synthetic.main.item_bottom_sheet.view.*

class MultipleChooseAdapter(var categories: MutableList<Fasilitas>) : RecyclerView.Adapter<MultipleChooseAdapter.MultipleChooseViewHolder>() {
    private var context : Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultipleChooseViewHolder {
        context = parent.context
        val mView = LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet,parent,false)
        return MultipleChooseViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }
    override fun onBindViewHolder(holder: MultipleChooseViewHolder, position: Int) {
        holder.clear()
        holder.onBind(position)
    }

    fun updateFacility(falItem: List<Fasilitas>) {
        this.categories.clear()
        this.categories.addAll(falItem)
        notifyDataSetChanged()
    }

    inner class MultipleChooseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun clear() {
        }

        fun onBind(position: Int) {
            val(
            id: Int,
            name: String,
            mosque: MosqueFacilities) = categories[position]

            inflateData(
                id,
                name,
                mosque)
        }

        fun inflateData(id: Int, name: String, mosque: MosqueFacilities) {
            name.let{
                itemView.ckb_option.text = it
                itemView.ckb_option.isChecked
            }
            /*itemView.ckb_option.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked){
                    println("DATA ${itemView.ckb_option.text}")
                }
            }*/
        }

    }


}