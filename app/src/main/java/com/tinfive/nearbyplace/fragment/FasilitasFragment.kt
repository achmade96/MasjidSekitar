package com.tinfive.nearbyplace.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.adapter.FasilitasAdapter
import com.tinfive.nearbyplace.model.FasilitasString
import com.tinfive.nearbyplace.view.MainActivity
import kotlinx.android.synthetic.main.bottom_sheet_filter.*


class FasilitasFragment : BottomSheetDialogFragment() {

    companion object {

        var mFasilitas: MutableList<FasilitasString> = mutableListOf()
        fun newInstance(): FasilitasFragment {
            return FasilitasFragment()
        }

        fun newInstance(listFasilitas: List<FasilitasString>): FasilitasFragment {
            mFasilitas = listFasilitas.toMutableList()
            return FasilitasFragment()
        }
    }

    private val mainActivity: MainActivity = MainActivity()
    private var mAdapterFasilitas = FasilitasAdapter(ArrayList())
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>
    lateinit var fragmentView: View
    var numbers: MutableList<FasilitasString> = ArrayList()

    var kategoriName: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = BottomSheetDialog(requireActivity(), theme)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setOnShowListener(object : DialogInterface.OnShowListener {
            override fun onShow(dialog: DialogInterface?) {
                val d = dialog as BottomSheetDialog
                val sheet = d.findViewById<View>(R.id.design_bottom_sheet)
                behavior = BottomSheetBehavior.from(sheet!!)
                behavior.isHideable = false
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        })
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView = inflater.inflate(R.layout.bottom_sheet_filter, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycle(mFasilitas) //GET BUTTON FILTER
    }

    private fun initRecycle(mFasilitas: List<FasilitasString>) {
        mAdapterFasilitas.updateListFasilitas(mFasilitas)
        recycle_filter.adapter = mAdapterFasilitas
        recycle_filter.setHasFixedSize(true)
        recycle_filter.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        println("CEK $mFasilitas")

        mAdapterFasilitas.setOnItemClickListener(object : FasilitasAdapter.OnItemClickListener {
            override fun onItemSelected(kategori: FasilitasString) {
                setPassData(kategori.name)
            }

        })

        btn_filter_fasilitas!!.setOnClickListener {
            val stringBuilder = StringBuilder()
            for (number in numbers) {
                if (number.isSelected) {
                    if (stringBuilder.isNotEmpty())
                        stringBuilder.append(", ")
                    stringBuilder.append(number.name)
                }
            }
            Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG).show()
            mainActivity.OnClickEventPassData("CLICK FILTER BLA $numbers")
            //                dialog.dismiss()
        }
    }


    private fun setPassData(name: String): String {
        kategoriName = name
        return kategoriName
    }

}