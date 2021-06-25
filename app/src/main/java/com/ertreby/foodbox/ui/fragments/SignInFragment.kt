package com.ertreby.foodbox.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmnetSignInBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInFragment : Fragment() {
    lateinit var bind: FragmnetSignInBinding

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



        bind.createAccountButton.setOnClickListener {
            val isEmailFilled = bind.editTextEmail.text.isNotEmpty()
            val isPasswordFilled = bind.editTextPassword.text.isNotEmpty()
            val isAllFieldsFilled = isEmailFilled && isPasswordFilled
            if (isAllFieldsFilled) {
                signIn()

            } else {
                if (!isEmailFilled) bind.editTextEmail.error = "email field must be filled"
                if (!isPasswordFilled) bind.editTextPassword.error =
                    "password field must be filled"
            }

        }

        bind.showPasswordImage.setOnClickListener {
            val isItPasswordType =
                bind.editTextPassword.transformationMethod is PasswordTransformationMethod
            bind.editTextPassword.transformationMethod =
                if (isItPasswordType) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            val drawableId =
                if (isItPasswordType) R.drawable.ic_visibility_off_ else R.drawable.ic_password_visible
            bind.editTextPassword.setSelection(bind.editTextPassword.text.length)
            val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
            (it as ImageView).setImageDrawable(drawable)


        }


        bind.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }

    }

    private fun signIn() {
        Firebase.auth.apply {
            signInWithEmailAndPassword(
                bind.editTextEmail.text.toString(),
                bind.editTextPassword.text.toString()
            ).addOnCompleteListener(activity as Activity) {
                if (it.isSuccessful) {
                    findNavController().navigate(R.id.action_signIn_to_home)
                } else {
                    Toast.makeText(context, "Login failed ", Toast.LENGTH_LONG).show()

                }
            }

        }

    }
}