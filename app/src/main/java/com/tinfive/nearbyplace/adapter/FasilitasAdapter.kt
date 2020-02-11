package com.tinfive.nearbyplace.adapter

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.Fasilitas


class FasilitasAdapter(internal var context: Context,
                       internal var categories:List<Fasilitas>) : RecyclerView.Adapter<FasilitasAdapter.ChooseViewHolder>(){

    private val itemStateArray = SparseBooleanArray()
    internal var selected_category:MutableList<Fasilitas> = ArrayList()

    inner class ChooseViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        internal var ckb_options : CheckBox
        init {
            ckb_options = itemView.findViewById(R.id.ckb_option) as CheckBox
            ckb_options.setOnCheckedChangeListener{ _, b ->
                itemStateArray.put(adapterPosition,b)
                if(b)
                    selected_category.add(categories[adapterPosition])
                else
                    selected_category.remove(categories[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet,parent,false)
        return ChooseViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: ChooseViewHolder, position: Int) {
        holder.ckb_options.text = categories[position].namaFasilitas
        holder.ckb_options.isChecked = itemStateArray.get(position)
    }

    val filterArray:String
        get(){
            val id_selected = ArrayList<Int>()
            for(fasilitas in selected_category)
                id_selected.add(fasilitas.id)
            return Gson().toJson(id_selected)
        }
}