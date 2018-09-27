package com.example.hafiz_saad.movies.view.activities

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.hafiz_saad.movies.R
import com.example.hafiz_saad.movies.R.id.poster
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_transition.*

class TransitionActivity : AppCompatActivity() {

    var bundle: Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)
        bundle = Bundle()
//        var transitionName = bundle!!.getParcelable("movies") as Bundle
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        poster.transitionName = "movies"
//        Fresco.initialize(applicationContext)
//        postponeEnterTransition()
    }
    fun scheduleStartPostPonedTransition(sharedElement: View){

    }


}
