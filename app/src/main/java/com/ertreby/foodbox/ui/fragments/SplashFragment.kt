package com.ertreby.foodbox.ui.fragments

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
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.addListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmentSplashBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding
    var isSearchActive=false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater, container, false)

        setupLayouts()

        return binding.root
    }

    private fun setupLayouts() {
        addMotionLayoutListener()
        binding.editTextTextPersonName2.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.constrainLayout.transitionToEnd()
                v.clearFocus()
                isSearchActive=true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        animateImageView()
    }

    private fun addMotionLayoutListener() {


        val currentUser = Firebase.auth.currentUser?.uid


        (binding.constrainLayout).addTransitionListener(object :
            MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {

                Log.i("MOTION", "onTransitionChanged called $p3")
                if (p3 > 0.98) {



                    if (Firebase.auth.currentUser?.uid != null) {
                        val bundle=Bundle()
                        bundle.putBoolean("search_state",isSearchActive)
                        findNavController().navigate(R.id.splash_to_home,bundle)
                    } else {
                        findNavController().navigate(R.id.action_splash_to_signIn)
                    }
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {

            }

            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

        })
    }





    private fun animateImageView() {
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
                ObjectAnimator.ofFloat(binding.constrainLayout, View.TRANSLATION_Y, -50f, 0f)
                    .apply {
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