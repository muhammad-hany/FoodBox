package com.ertreby.controlpanel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.controlpanel.databinding.FragmentMenuAddBinding

class MenuAddFragment : Fragment() {

    private lateinit var editTexts: List<EditText>
    private lateinit var bind: FragmentMenuAddBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var mealImageUri: Uri
    var action="notFirstMenu"





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMenuAddBinding.inflate(inflater, container, false)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                mealImageUri = it.data?.data as Uri
                val imageName=DocumentFile.fromSingleUri(requireContext(),mealImageUri)?.name
                bind.imageNameText.text = imageName
            }
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        action=arguments?.getString("action","NotFirstMenu").toString()
        if (action=="notFirstMenu"){
            bind.title.text="Add Your Meal"

        }else if (action=="firstMenu"){
            bind.title.text="Add Your First Meal"
        }


        editTexts = listOf(
            bind.mealNameEditText,
            bind.mealExtrasEditText,
            bind.mealPrice,
            bind.mealCategoryEditText,
            bind.mealDescriptionEditText
        )

        bind.chooseImageButton.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_GET_CONTENT)
            imageIntent.type = "image/*"
            resultLauncher.launch(imageIntent)
        }


        bind.submitMealButton.setOnClickListener {
            if (!checkAllFieldFilled(editTexts)) return@setOnClickListener
            startLoadingAnimation()
            uploadMealImage()


        }
    }

    private fun uploadMealImage() {
        FirebaseService.uploadMealImage(mealImageUri, ::addMealToDatabase, ::toastErrorMessage)
    }

    private fun addMealToDatabase(imageUrl: String) {

        val meal = Meal(
            bind.mealNameEditText.text.toString(),
            FirebaseService.currentUser?.uid,
            FirebaseService.getUniqueId(),
            bind.mealPrice.text.toString(),
            imageUrl,
            getListFromCommaSeparatedText(bind.mealExtrasEditText.text.toString()),
            bind.mealDescriptionEditText.text.toString(),
            0,
            bind.mealCategoryEditText.text.toString()
        )

        FirebaseService.addMeal(meal, {
            endLoadingAnimation()

            if (action=="notFirstMenu"){
                findNavController().navigateUp()

            }else if (action=="firstMenu"){
                findNavController().navigate(R.id.action_menuAddFragment_to_homeFragment)
            }

        }, {
            endLoadingAnimation()
            toastErrorMessage(it)

        })
    }


    private fun checkAllFieldFilled(editTexts: List<EditText>): Boolean {
        var isAllChecked = true
        editTexts.forEach {
            if (it.text.isEmpty()) {
                it.error = "You must type ${it.hint}"
                isAllChecked = false
            }
        }
        if (!this::mealImageUri.isInitialized) {
            isAllChecked = false
            Toast.makeText(
                requireContext(), "You must choose logo image for the Meal !",
                Toast.LENGTH_LONG
            ).show()
        }

        return isAllChecked
    }

    private fun getListFromCommaSeparatedText(text: String): List<String> {
        return text.split(",")
    }

    private fun toastErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun startLoadingAnimation() {
        editTexts.map { it.isEnabled=false }
        bind.loadingImage.visibility = View.VISIBLE
        val loadingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
        bind.loadingImage.startAnimation(loadingAnimation)
    }


    private fun endLoadingAnimation() {
        editTexts.map { it.isEnabled=true }
        bind.loadingImage.visibility = View.GONE
        bind.loadingImage.clearAnimation()
    }
}