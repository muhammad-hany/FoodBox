package com.ertreby.foodbox.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.addListener
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import kotlinx.android.synthetic.main.fragment_splash.view.*


class SplashFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash, null, false)
        addMotionLayoutListener(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        view?.let { animateImageView(it) }
    }

    private fun addMotionLayoutListener(view: View) {
        (view.constrain_layout as MotionLayout).addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                (activity as AppCompatActivity).supportActionBar?.show()
                (activity as AppCompatActivity).supportActionBar?.elevation=0f
            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {


            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

                  findNavController().navigate(R.id.action_splash_to_home)
            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
    }


    private fun animateImageView(view: View) {
        val imageView: ImageView = view.icon_center_Image

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

                            view.textView.visibility = View.VISIBLE

                        }
                    })
                }

            val bounceAnimation =
                ObjectAnimator.ofFloat(view.constrain_layout, View.TRANSLATION_Y, -50f, 0f).apply {
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