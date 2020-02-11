package com.tinfive.nearbyplace.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tinfive.nearbyplace.R

fun getProgressDrawable(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 5f
        centerRadius = 30f
        start()
    }
}

fun ImageView.loadImage(url: String?, progressDrawable: CircularProgressDrawable){
    val options =  RequestOptions()
        .placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher_round)

    Glide.with(this.context)
        .setDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
    if (view.layoutParams is MarginLayoutParams) {
        val p = view.layoutParams as MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        view.requestLayout()
    }
}

fun showToast(context: Context , message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}

//BOTTOM SHEET OPTIONS
/*    private fun showBottomSheetDialogFragment() {
        val bottomSheetFragment = FasilitasFragment.newInstance(fasilitasList)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)




        println("DATA SIZEE ${fasilitasList.size}")
        println("DATAITUNG $itung")

    }*/

//INI UNTUK MENGAKTIFKAN EFEK UP BUTTON NAV
/*private fun slideUpDownBottomSheet() {
    if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//            changeToTop()
    } else {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//            changeToDown()
    }
}*/

/*private fun changeToDown() {
    setMargins(nav, 5, 0, 5, 20)
}

private fun changeToTop() {
    setMargins(nav, 5, 0, 5, 900)
}*/

//END BOTTOM SHEET OPTIONS
