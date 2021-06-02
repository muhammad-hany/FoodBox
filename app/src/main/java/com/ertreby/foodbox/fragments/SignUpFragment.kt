package com.ertreby.foodbox.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.method.MovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.ertreby.foodbox.R
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, null, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spannable = SpannableString(resources.getString(R.string.terms_and_conditions_text))
        val clickSpan1 = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ResourcesCompat.getColor(resources, R.color.color_primary, null)
                ds.typeface = Typeface.create(context?.let { ResourcesCompat.getFont(it,R.font.sansation) }, Typeface.BOLD)
                ds.isUnderlineText=false
            }

        }

        val clickSpan2 = object : ClickableSpan() {
            override fun onClick(widget: View) {

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = ResourcesCompat.getColor(resources, R.color.color_primary, null)
                ds.typeface = Typeface.create(context?.let { ResourcesCompat.getFont(it,R.font.sansation) }, Typeface.BOLD)
                ds.isUnderlineText=false
            }

        }
        var text = "Terms and Conditions"
        var index = resources.getString(R.string.terms_and_conditions_text).indexOf(text)
        spannable.setSpan(clickSpan1, index, index + text.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        text="Privacy Policy"
        index = resources.getString(R.string.terms_and_conditions_text).indexOf(text)
        spannable.setSpan(clickSpan2, index, index + text.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.checkBox.text = spannable
        view.checkBox.movementMethod=LinkMovementMethod.getInstance()
    }
}