package com.ertreby.foodbox.ui.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.ui.fragments.DetailsFragment
import com.ertreby.foodbox.ui.fragments.ExtrasFragment
import com.ertreby.foodbox.ui.fragments.OrderFragment
import com.ertreby.foodbox.ui.fragments.ReviewsFragment

class OrderPagerAdapter(hostFragment:Fragment, val meal: Meal, val callback:OrderFragment.CheckBoxListener): FragmentStateAdapter(hostFragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val bundle=Bundle()
        return when(position){
            0->{
                val details=meal.description
                bundle.putString("details",details)
                return DetailsFragment().apply { arguments=bundle }
            }
            1-> {


                val arrayList=ArrayList<String>(meal.extras)
                bundle.putStringArrayList(ExtrasFragment.EXTRAS_LIST,arrayList )

                return ExtrasFragment(callback).apply { arguments=bundle }
            }
            else-> ReviewsFragment()
        }
    }
}