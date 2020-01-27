package com.tinfive.nearbyplace.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.adapter.FasilitasAdapter
import com.tinfive.nearbyplace.model.Fasilitas
import com.tinfive.nearbyplace.view.MainActivity
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import kotlinx.android.synthetic.main.item_bottom_sheet.*

class FasilitasFragment : BottomSheetDialogFragment() {

    companion object {

        var mFasilitas: MutableList<Fasilitas> = mutableListOf()
        fun newInstance(): FasilitasFragment {
            return FasilitasFragment()
        }

        fun newInstance(listFasilitas: List<Fasilitas>): FasilitasFragment {
            mFasilitas = listFasilitas.toMutableList()
            return FasilitasFragment()
        }
    }

    private val mainActivity: MainActivity = MainActivity()
    private var mAdapterFasilitas = FasilitasAdapter(ArrayList())
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>
    lateinit var fragmentView: View


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

        println("Fragment ${mFasilitas.size}")

        initRecycle(mFasilitas)

        btn_filter_fasilitas.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                dialog.dismiss()
                mainActivity.OnClickEventPassData("CLICK FILTER")
            }
        })
    }

    private fun initRecycle(mFasilitas: List<Fasilitas>) {
        mAdapterFasilitas.updateListFasilitas(mFasilitas)
        recycle_filter.adapter = mAdapterFasilitas
        recycle_filter.setHasFixedSize(true)
        recycle_filter.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }
}