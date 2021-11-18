package com.android04.godfisherman.ui.intro

import android.os.Bundle
import com.github.paolorotolo.appintro.AppIntro
import com.android04.godfisherman.ui.main.MainActivity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.android04.godfisherman.R
import com.github.paolorotolo.appintro.AppIntroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GodFishermanIntro : AppIntro() {
    private val viewModel: IntroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObserver()
        viewModel.fetchFirstStart()

        addSlide(
            AppIntroFragment.newInstance(
                "물고기 카메라",
                "잡은 물고기를 1000원권 지폐와 함께 촬영해보세요\n자동으로 물고기의 길이를 측정해줄게요🤩",
                R.drawable.ic_camera,
                resources.getColor(R.color.secondary)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                "낚시 타임",
                "스톱워치를 킨 상태로 낚시 기록을 남겨 보세요\n여러 낚시 기록을 한 번에 업로드할 수 있어요😜",
                R.drawable.ic_stopwatch,
                resources.getColor(R.color.money_red)
            )
        )
        addSlide(
            AppIntroFragment.newInstance(
                "낚시 랭킹",
                "낚시 랭킹을 제공합니다\n낚시를 하면서 다른 사람들과 경쟁도 해보세요😊",
                R.drawable.ic_ranking,
                resources.getColor(R.color.purple_700)
            )
        )
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