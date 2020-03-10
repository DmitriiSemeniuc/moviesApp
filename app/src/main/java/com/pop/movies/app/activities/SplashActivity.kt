package com.pop.movies.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pop.movies.app.R
import com.pop.movies.app.viewmodels.SplashViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val model: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        model.goToMoviesScreenDelayed()
    }
}