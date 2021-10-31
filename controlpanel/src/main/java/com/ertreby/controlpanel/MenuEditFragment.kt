package com.ertreby.controlpanel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ertreby.controlpanel.databinding.FragmentMenuAddBinding

class MenuEditFragment : Fragment() {

    lateinit var bind: FragmentMenuAddBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var logoImageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMenuAddBinding.inflate(inflater, container, false)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                logoImageUri = it.data?.data as Uri
                val imageName=DocumentFile.fromSingleUri(requireContext(),logoImageUri)?.name
                bind.imageNameText.text = imageName
            }
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val meal = requireArguments().getParcelable<Meal>("edited_meal")
        bind.mealNameEditText.setText(meal?.name)
        bind.mealExtrasEditText.setText(meal?.extras.toString().filterNot { it=='[' || it==']' })
        bind.mealDescriptionEditText.setText(meal?.description)
        bind.mealCategoryEditText.setText(meal?.type)
        bind.mealPrice.setText(meal?.price)
        bind.chooseImageButton.text = "choose another image"
        bind.chooseImageButton.setOnClickListener {
            val imageIntent = Intent(Intent.ACTION_GET_CONTENT)
            imageIntent.type = "image/*"
            resultLauncher.launch(imageIntent)
        }

        bind.submitMealButton.setOnClickListener {
            if (this::logoImageUri.isInitialized) {
                FirebaseService.uploadMealImage(
                    logoImageUri,
                    ::updateMealInDatabase,
                    ::toastErrorMessage
                )
            } else {
                meal?.let { updateMealInDatabase(it.imageUrl.toString()) }
            }


        }
    }

    private fun updateMealInDatabase(mealImageUrl: String) {
        val oldMeal = requireArguments().getParcelable<Meal>("edited_meal")
        val updatedMeal = Meal(
            bind.mealNameEditText.text.toString(),
            oldMeal?.restaurantId,
            oldMeal?.id,
            bind.mealPrice.text.toString(),
            mealImageUrl,
            bind.mealExtrasEditText.text.toString().split(","),
            bind.mealDescriptionEditText.text.toString(),
            0,
            bind.mealCategoryEditText.text.toString()
        )

        FirebaseService.updateMeal(updatedMeal, {
            findNavController().navigateUp()
        }, ::toastErrorMessage)
    }




    private fun toastErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}