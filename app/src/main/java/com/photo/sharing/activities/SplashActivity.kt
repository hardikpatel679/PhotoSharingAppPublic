package com.photo.sharing.activities

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.photo.sharing.R
import com.photo.sharing.viewmodel.SplashViewModel
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SplashActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setUpViewModel()
    }

    private fun setUpViewModel() {
        viewModel = ViewModelProvider(this@SplashActivity).get(SplashViewModel::class.java)

        viewModel.isDBCreated().observe(this, Observer {
            if (it) {
                Handler().postDelayed({
                    startActivity<HomeActivity>().also {
                        finish()
                    }
                }, 1000)
            } else {
                toast("Error creating Database")
                finish()
            }
        })
    }
}
