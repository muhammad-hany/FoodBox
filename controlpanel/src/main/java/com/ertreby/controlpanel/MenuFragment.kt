package com.ertreby.controlpanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ertreby.controlpanel.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var bind: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentMenuBinding.inflate(inflater, container, false)
        return bind.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startLoadingAnimation()
        FirebaseService.getRestaurantMeals { meals ->
            endLoadingAnimation()
            setUpRecyclerView(meals)

        }


        bind.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        bind.floatingActionButton.setOnClickListener {
            val action = "notFirstMenu"
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToMenuAddFragment(
                    action
                )
            )
        }

    }

    private fun setUpRecyclerView(meals: List<Meal>) {
        val adapter = MenuListAdapter(meals.toMutableList(), {
            navigateToEditMealFragment(it)
        }, ::removeMeal)

        bind.recyclerView.adapter = adapter
    }

    private fun removeMeal(meal: Meal, position: Int) {
        FirebaseService.removeMeal(meal, {
            (bind.recyclerView.adapter as MenuListAdapter).removeItem(position)
        }, ::toastErrorMessage)
    }


    private fun navigateToEditMealFragment(meal: Meal) {
        val bundle = Bundle()
        bundle.putParcelable("edited_meal", meal)
        findNavController().navigate(R.id.action_menuFragment_to_menuEditFragment, bundle)
    }


    private fun startLoadingAnimation() {

        bind.loadingImage.visibility = View.VISIBLE
        val loadingAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink)
        bind.loadingImage.startAnimation(loadingAnimation)
    }


    private fun endLoadingAnimation() {

        bind.loadingImage.visibility = View.GONE
        bind.loadingImage.clearAnimation()
    }


    private fun toastErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


}