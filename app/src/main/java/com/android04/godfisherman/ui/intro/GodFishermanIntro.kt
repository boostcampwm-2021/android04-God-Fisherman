package com.android04.godfisherman.ui.intro

import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro
import com.android04.godfisherman.ui.main.MainActivity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GodFishermanIntro : AppIntro() {
    private val viewModel : IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        viewModel.fetchFirstStart()

        addSlide(AppIntroFragment())
        addSlide(AppIntroFragment())
        addSlide(AppIntroFragment())
    }

    private fun setObserver() {
        viewModel.isFirstStart.observe(this) {
            if (it == false) {
                startMainActivity()
            }
        }
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        viewModel.setFirstStart()
        startMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        viewModel.setFirstStart()
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}