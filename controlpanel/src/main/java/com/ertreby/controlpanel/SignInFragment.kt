package com.ertreby.controlpanel

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.controlpanel.databinding.FragmnetSignInBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {

    private lateinit var editTexts: List<EditText>
    private lateinit var bind: FragmnetSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmnetSignInBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTexts = listOf(bind.editTextEmail, bind.editTextPassword)



        bind.signInButton.setOnClickListener {

            signIn()
        }


        bind.showPasswordImage.setOnClickListener {

            showOrHidePassword(it)
        }

        bind.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }


    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }

    }

    private fun signIn() {
        if (!checkAllFieldFilled()) return

        startLoadingAnimation()
        FirebaseService.signIn(
            bind.editTextEmail.text.toString(),
            bind.editTextPassword.text.toString(), {
                endLoadingAnimation()
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            }, {
                endLoadingAnimation()
                toastErrorMessage(it)
            }
        )
    }


    private fun showOrHidePassword(view: View) {
        val isItPasswordType =
            bind.editTextPassword.transformationMethod is PasswordTransformationMethod
        bind.editTextPassword.transformationMethod =
            if (isItPasswordType) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
        val drawableId =
            if (isItPasswordType) R.drawable.ic_visibility_off_ else R.drawable.ic_password_visible
        val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
        (view as ImageView).setImageDrawable(drawable)
        bind.editTextPassword.setSelection(bind.editTextPassword.text.length)
    }


    private fun checkAllFieldFilled(): Boolean {

        var isAllChecked = true
        editTexts.forEach {
            if (it.text.isEmpty()) {
                it.error = "You must type ${it.hint}"
                isAllChecked = false
            }

        }
        return isAllChecked
    }


    private fun startLoadingAnimation() {
        editTexts.map { it.isEnabled = false }
        bind.loadingImage.visibility = View.VISIBLE
        val loadingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
        bind.loadingImage.startAnimation(loadingAnimation)
    }


    private fun endLoadingAnimation() {
        editTexts.map { it.isEnabled = true }
        bind.loadingImage.visibility = View.GONE
        bind.loadingImage.clearAnimation()
    }

    private fun toastErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}


