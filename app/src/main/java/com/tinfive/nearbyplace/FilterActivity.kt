package com.tinfive.nearbyplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tinfive.nearbyplace.networks.MasjidApi
import io.reactivex.disposables.CompositeDisposable

class FilterActivity : AppCompatActivity() {
    internal var compositeDisposable = CompositeDisposable()
    internal lateinit var iMasjidAPI: MasjidApi

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        //actionbar
        val actionbar = supportActionBar
        //set back button
        actionbar!!.setDisplayHomeAsUpEnabled(true)

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
