package com.ertreby.foodbox.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    lateinit var binding:FragmentSplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSplashBinding.inflate(layoutInflater)
        val view = inflater.inflate(R.layout.fragment_splash, null, false)
        addMotionLayoutListener(view)
        return view
    }

    override fun onResume() {
        super.onResume()
       // view?.let { animateImageView(it) }
    }

    private fun addMotionLayoutListener(view: View) {
        (binding.constrainLayout as MotionLayout).addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                (activity as AppCompatActivity).supportActionBar?.show()
                (activity as AppCompatActivity).supportActionBar?.elevation=0f
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

                Log.i("MOTION","onTransitionChanged called $p3")
                if (p3 >0.98) findNavController().navigate(R.id.action_splash_to_home)
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
    }


    private fun animateImageView(view: View) {
        val imageView: ImageView = binding.iconCenterImage

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            val imageXAnimator =
                ObjectAnimator.ofFloat(imageView, ImageView.SCALE_X, 0.1f, 1f).apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                }

            val imageYAnimator =
                ObjectAnimator.ofFloat(imageView, ImageView.SCALE_Y, 0.1f, 1f).apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {

                            binding.textView.visibility = View.VISIBLE

                        }
                    })
                }

            val bounceAnimation =
                ObjectAnimator.ofFloat(binding.constrainLayout, View.TRANSLATION_Y, -50f, 0f).apply {
                    interpolator = BounceInterpolator()
                    startDelay = 500
                    duration = 500


                }


            val imageAnimationSet = AnimatorSet()
            imageAnimationSet.playTogether(imageXAnimator, imageYAnimator)
            imageAnimationSet.start()
            imageAnimationSet.addListener({
                //onEnd
                bounceAnimation.start()
            })


        }
    }
}