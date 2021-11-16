package com.android04.godfisherman.ui.intro

import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro
import com.android04.godfisherman.ui.main.MainActivity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntroFragment

class GodFishermanIntro : AppIntro() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(AppIntroFragment())
        addSlide(AppIntroFragment())
        addSlide(AppIntroFragment())
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        startMainActivity()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startMainActivity()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}