package com.tinfive.nearbyplace.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.model.FasilitasString
import kotlinx.android.synthetic.main.item_bottom_sheet.view.*


class FasilitasAdapter(var fasilitasList: MutableList<FasilitasString>) : RecyclerView.Adapter<FasilitasAdapter.FasilitasViewHolder>() {
    private val limit: Int = 5
    private var context: Context? = null


    private var mOnItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FasilitasViewHolder {
        context = parent.context
        val layoutInflate = LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet, parent, false)
        return FasilitasViewHolder(layoutInflate)
    }

    override fun getItemCount(): Int {
        return fasilitasList.size

    }

    override fun onBindViewHolder(holder: FasilitasViewHolder, position: Int) {
        holder.clear()
        holder.onBind(position)
    }

    fun updateListFasilitas(falItem: List<FasilitasString>) {
        this.fasilitasList.clear()
        this.fasilitasList.addAll(falItem)
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemSelected(kategori: FasilitasString)
    }

    internal fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    inner class FasilitasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        override fun onClick(v: View?) {
            mOnItemClickListener?.onItemSelected(fasilitasList[adapterPosition])
        }

        fun clear() {
        }

        fun onBind(position: Int) {
            val (id,name,isSelected) = fasilitasList[position]
            inflateData(id,name,isSelected)
        }

        private fun inflateData(id: Int, name: String, isSelected: Boolean) {


            itemView.ckb_option.text = name
            itemView.ckb_option.isChecked = fasilitasList[adapterPosition].isSelected
            itemView.ckb_option.text = fasilitasList[adapterPosition].name

            itemView.ckb_option.tag = adapterPosition
            itemView.ckb_option.setOnClickListener {
                val ckbox = itemView.ckb_option.tag as Int
                println("Checkbox $name")
                fasilitasList[ckbox].isSelected = !fasilitasList[ckbox].isSelected
            }
            itemView.setOnClickListener(this)
        }
    }

}
