package com.ertreby.foodbox.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.HideReturnsTransformationMethod
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmentSignUpBinding
import com.ertreby.foodbox.repositories.FirebaseService

class SignUpFragment : Fragment() {
    lateinit var bind: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    private lateinit var editTexts: List<EditText>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeTermsText()

        editTexts = listOf(
            bind.editTextFirstName,
            bind.editTexLastName,
            bind.editTextEmail,
            bind.editTextPassword
        )


        bind.showPasswordImage.setOnClickListener {
            val isItPasswordType =
                bind.editTextPassword.transformationMethod is PasswordTransformationMethod
            bind.editTextPassword.transformationMethod =
                if (isItPasswordType) HideReturnsTransformationMethod.getInstance() else PasswordTransformationMethod.getInstance()
            val drawableId =
                if (isItPasswordType) R.drawable.ic_visibility_off_ else R.drawable.ic_password_visible
            val drawable = ResourcesCompat.getDrawable(resources, drawableId, null)
            (it as ImageView).setImageDrawable(drawable)
            bind.editTextPassword.setSelection(bind.editTextPassword.text.length)


        }
        bind.createAccountButton.setOnClickListener {
            signup()

        }

        bind.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun signup() {
        if (!checkAllFieldFilled(editTexts) || !checkTermsAndConditions()) return

        val email = bind.editTextEmail.text.toString()
        val password = bind.editTextPassword.text.toString()
        val firstName = bind.editTextFirstName.text.toString()
        val lastName = bind.editTexLastName.text.toString()

        FirebaseService.signUp(
            email,
            password,
            firstName,
            lastName,
            ::onAccountCreationSuccess,
            ::onAccountCreationFail
        )


    }

    private fun onAccountCreationSuccess() {
        findNavController().navigate(R.id.action_signUpFragment_to_enterPhoneFragment)
    }

    private fun onAccountCreationFail(e: Exception) {
        Toast.makeText(
            context,
            "Signup Failed due to $e",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun checkTermsAndConditions(): Boolean {
        return if (bind.termsCheck.isChecked) {
            true
        } else {
            bind.termsCheck.error = "You must accept user terms and conditions"
            Toast.makeText(context, "You must accept user terms and conditions", Toast.LENGTH_LONG)
                .show()
            false
        }
    }

    private fun checkAllFieldFilled(editTexts: List<EditText>): Boolean {
        var isAllChecked = true
        editTexts.forEach {
            if (it.text.isEmpty()) {
                it.error = "You must type ${it.hint}"
                isAllChecked = false
            }
        }
        return isAllChecked
    }

    private fun makeTermsText() {
        val spannable = SpannableString(resources.getString(R.string.terms_and_conditions_text))
        val clickSpan1 = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ResourcesCompat.getColor(resources, R.color.color_primary, null)
                ds.typeface = Typeface.create(
                    context?.let { ResourcesCompat.getFont(it, R.font.sansation) },
                    Typeface.BOLD
                )
                ds.isUnderlineText = false
            }

        }

        val clickSpan2 = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ResourcesCompat.getColor(resources, R.color.color_primary, null)
                ds.typeface = Typeface.create(
                    context?.let { ResourcesCompat.getFont(it, R.font.sansation) },
                    Typeface.BOLD
                )
                ds.isUnderlineText = false
            }

        }
        var text = "Terms and Conditions"
        var index = resources.getString(R.string.terms_and_conditions_text).indexOf(text)
        spannable.setSpan(clickSpan1, index, index + text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text = "Privacy Policy"
        index = resources.getString(R.string.terms_and_conditions_text).indexOf(text)
        spannable.setSpan(clickSpan2, index, index + text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        bind.termsCheck.text = spannable
        bind.termsCheck.movementMethod = LinkMovementMethod.getInstance()
    }


}