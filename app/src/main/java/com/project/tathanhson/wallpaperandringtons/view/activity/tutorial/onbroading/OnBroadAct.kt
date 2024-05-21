package com.project.tathanhson.wallpaperandringtons.view.activity.tutorial.onbroading

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.project.tathanhson.myapplication.OnBroadAction
import com.project.tathanhson.myapplication.OnBroadStep1Fragment
import com.project.tathanhson.myapplication.OnBroadStep2Fragment
import com.project.tathanhson.myapplication.OnBroadStep3Fragment
import com.project.tathanhson.myapplication.OnBroadViewModel
import com.project.tathanhson.myapplication.onSingleClickListener
import com.project.tathanhson.wallpaperandringtons.CommonObject
import com.project.tathanhson.wallpaperandringtons.databinding.ActivityOnbroadingBinding
import com.project.tathanhson.wallpaperandringtons.view.activity.base.BaseActivity
import com.project.tathanhson.wallpaperandringtons.view.activity.MainActivity
import kotlin.math.abs
import kotlin.math.roundToInt

class OnBroadAct : BaseActivity<ActivityOnbroadingBinding>(ActivityOnbroadingBinding::inflate) {
    private lateinit var viewModel: OnBroadViewModel
    override fun initViewModel() {
        viewModel = ViewModelProvider(this)[OnBroadViewModel::class.java]

    }



    override fun initData() {
        val pagerAdapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return PAGER_SIZE
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    STEP1_POSITION -> OnBroadStep1Fragment.getInstance()
                    STEP2_POSITION -> OnBroadStep2Fragment.getInstance()
                    else -> OnBroadStep3Fragment.getInstance()
                }
            }
        }
        binding.viewPager.adapter = pagerAdapter
        binding.indicatorView.setSepCount(PAGER_SIZE)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                viewModel.dispatch(OnBroadAction.ChangeDragging(dragging = state == ViewPager2.SCROLL_STATE_DRAGGING))
                super.onPageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float, positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                binding.indicatorView.setCurrentSepCount(position, positionOffset)
                if (positionOffset == 0f) {
                    (binding.nextBtn.layoutParams as? ViewGroup.MarginLayoutParams)?.let { params ->
                        val locationButton = binding.indicatorView.getButtonLocation()
                        params.marginStart = locationButton.first.toInt()
                        params.bottomMargin = locationButton.second.toInt()
                        binding.nextBtn.layoutParams = params
                    }
                }

                val scale = abs(0.5f - positionOffset) / 0.5f
                viewModel.dispatch(
                    OnBroadAction.ChangeScale(
                        scale = scale, toPosition = positionOffset.roundToInt()
                    )
                )
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModel.dispatch(OnBroadAction.ChangePosition(position))
            }
        })

    }

    override fun initView() {
        binding.nextBtn.onSingleClickListener {
            when (binding.viewPager.currentItem) {
                STEP1_POSITION -> binding.viewPager.currentItem = STEP2_POSITION
                STEP2_POSITION -> binding.viewPager.currentItem = STEP3_POSITION
                else -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }

    }

    companion object {
        private const val PAGER_SIZE = 3
        private const val STEP1_POSITION = 0
        private const val STEP2_POSITION = 1
        private const val STEP3_POSITION = 2

        fun getIntent(context: Context): Intent {
            return Intent(context, OnBroadAct::class.java).apply {
                this.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            }
        }
    }

}