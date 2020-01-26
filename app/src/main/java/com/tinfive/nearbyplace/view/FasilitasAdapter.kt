package com.tinfive.nearbyplace.view

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.Fasilitas
import kotlinx.android.synthetic.main.item_bottom_sheet.view.*


class FasilitasAdapter(var fasilitasList: MutableList<Fasilitas>) :
    RecyclerView.Adapter<FasilitasAdapter.FasilitasViewHolder>() {


    private val limit: Int = 4
    private var context: Context? = null
    var itemStateArray = SparseBooleanArray()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FasilitasViewHolder {
        context = parent.context
        val layoutInflate =
            LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet, parent, false)
        return FasilitasViewHolder(layoutInflate)
    }

    override fun getItemCount(): Int {
        if (fasilitasList.size > limit) {
            return limit
        } else {
            return fasilitasList.size
        }
    }

    override fun onBindViewHolder(holder: FasilitasViewHolder, position: Int) {
        holder.clear()
        holder.onBind(position)
    }

    fun updateListFasilitas(falItem: List<Fasilitas>) {
        this.fasilitasList.clear()
        this.fasilitasList.addAll(falItem)
        notifyDataSetChanged()
    }

    inner class FasilitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        override fun onClick(v: View?) {
            if (!itemStateArray.get(adapterPosition, false)) {
                itemView.ckb_option.isChecked = true;
                itemStateArray.put(adapterPosition, true)
                println("DATAC ${itemView.ckb_option.isChecked}")
            }
            else  {
                itemView.ckb_option.isChecked = false;
                itemStateArray.put(adapterPosition, false)
                println("DATAC ${itemView.ckb_option.isChecked}")
            }
        }

        fun clear() {
        }

        fun onBind(position: Int) {
            val (
                id: Int,
                name: String)
                    = fasilitasList[position]
            inflateData(
                id,
                name
            )
        }

        private fun inflateData(id: Int, name: String) {
            name.let {
                itemView.ckb_option.text = it
                itemView.ckb_option.isChecked = itemStateArray.get(adapterPosition, false)


            }
            itemView.setOnClickListener(this)



        }


    }


}
