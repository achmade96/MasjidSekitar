package com.tinfive.nearbyplace.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.tinfive.nearbyplace.R
import com.tinfive.nearbyplace.adapter.ListMasjidAdapter
import com.tinfive.nearbyplace.networks.EndPoint
import com.tinfive.nearbyplace.utils.getProgressDrawable
import com.tinfive.nearbyplace.viewmodel.DetailMasjidViewModel
import com.tinfive.nearbyplace.viewmodel.ListViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_detail_masjid.*

class DetailMasjid : AppCompatActivity() {

    lateinit var viewModel: ListViewModel
    private val masjidAdapter =
        ListMasjidAdapter(ArrayList())
    private var myCompositeDisposable = CompositeDisposable()

    var progressDrawable: CircularProgressDrawable? = null
    var imgTarget: String = ""
    lateinit var detailMasjidViewModel: DetailMasjidViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_masjid)

        detailMasjidViewModel = ViewModelProvider(this)[DetailMasjidViewModel::class.java]
        observeViewModel()
    }

    private fun observeViewModel() {
        detailMasjidViewModel.mosquesData.observe(this, Observer { mosques ->
            mosques?.let {
                println("DATA recive API ${it.mosqueName}")
                tv_masjid.text = it.mosqueName
                tv_alamat.text = it.address
                tv_latitude.text = it.latitude
                tv_longitude.text = it.address
                /*progressDrawable = getProgressDrawable(this)
                imgTarget = EndPoint.imageUrlPath*/
                /*it.pic.let {
                    image_poster.loadImage(imgTarget + it, progressDrawable!!)
                }*/
            }
        })
    }
}
