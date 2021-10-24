package com.ertreby.controlpanel

import android.content.Intent
import android.net.Uri
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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.controlpanel.databinding.FragmentSignUpBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment : Fragment() {


    private lateinit var bind: FragmentSignUpBinding
    private lateinit var editTexts: List<EditText>
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var logoImageUri: Uri
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentSignUpBinding.inflate(inflater, container, false)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                logoImageUri = it.data?.data as Uri
                val imageFileName = DocumentFile.fromSingleUri(requireContext(),logoImageUri)?.name
                bind.imageNameText.text=imageFileName
            }
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTexts = listOf(
            bind.editTextRestaurantName,
            bind.editTextEmail,
            bind.editTextPassword,
            bind.editTextAddress,
            bind.editTextDescription
        )


        bind.showPasswordImage.setOnClickListener {

            showOrHidePassword(it)
        }

        bind.createAccountButton.setOnClickListener {

            signup()
        }



        bind.chooseLogoButton.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_GET_CONTENT)
            imageIntent.type = "image/*"
            resultLauncher.launch(imageIntent)
        }
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

    private fun signup() {
        if (!checkAllFieldFilled(editTexts)) return

        startLoadingAnimation()

        val email = bind.editTextEmail.text.toString()
        val password = bind.editTextPassword.text.toString()

        FirebaseService.signUp(
            email,
            password,
            ::uploadLogoImage,
            ::toastErrorMessage
        )


    }

    private fun addRestaurantToDatabase(imageUrl: String, userId: String) {

        val restaurant = Restaurant(
            bind.editTextRestaurantName.text.toString(),
            bind.editTextDescription.text.toString(),
            0, imageUrl, null, listOf(), userId
        )

        FirebaseService.addRestaurant(restaurant,{
            endLoadingAnimation()
            findNavController().navigate(R.id.action_signUp_to_menuAdd)
        },{
            toastErrorMessage(it)
            endLoadingAnimation()
        })

    }

    private fun uploadLogoImage(userId: String) {
        val id = Firebase.auth.currentUser
        logoImageUri.let { FirebaseService.uploadLogoImage(it, userId,::addRestaurantToDatabase,::toastErrorMessage) }
    }


    private fun checkAllFieldFilled(editTexts: List<EditText>): Boolean {
        var isAllChecked = true
        editTexts.forEach {
            if (it.text.isEmpty()) {
                it.error = "You must type ${it.hint}"
                isAllChecked = false
            }
        }

        if(!this::logoImageUri.isInitialized) {
            isAllChecked=false
            Toast.makeText(requireContext(),"You must choose logo image for the restaurant !",Toast.LENGTH_LONG).show()
        }
        return isAllChecked
    }


    private fun startLoadingAnimation() {
        editTexts.map { it.isEnabled = false }
        bind.loadingImageView.visibility=View.VISIBLE
        val loadingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
        bind.loadingImageView.startAnimation(loadingAnimation)
    }


    private fun endLoadingAnimation() {
        editTexts.map { it.isEnabled = true }
        bind.loadingImageView.visibility = View.GONE
        bind.loadingImageView.clearAnimation()
    }


    private fun toastErrorMessage(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_LONG).show()
    }
}