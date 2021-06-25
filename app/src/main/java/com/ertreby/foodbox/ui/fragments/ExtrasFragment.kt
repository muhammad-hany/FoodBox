package com.ertreby.foodbox.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.ertreby.foodbox.R
import com.ertreby.foodbox.databinding.FragmentExtrasBinding

class ExtrasFragment(val callback:OrderFragment.CheckBoxListener) : Fragment(),
    CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: FragmentExtrasBinding

    private lateinit var data: ArrayList<String>


    companion object{
        const val EXTRAS_LIST="extras"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args=arguments
        data= args?.getStringArrayList(EXTRAS_LIST)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExtrasBinding.inflate(inflater, container, false)
        addingTheCheckBoxes()
        return binding.root
    }

    private val checkBoxesIds=mutableListOf<Int>()
    private fun addingTheCheckBoxes() {

        data.forEach {
            val checkBox = CheckBox(context)
            checkBox.setOnCheckedChangeListener(this)
            checkBox.text=it
            checkBox.buttonTintList= ColorStateList.valueOf(ResourcesCompat.getColor(resources,R.color.color_primary,null))
            checkBox.id = View.generateViewId()
            binding.constrainLayout.addView(checkBox)
            checkBoxesIds.add(checkBox.id)
        }

        binding.flowLayout.referencedIds = checkBoxesIds.toIntArray()
    }



    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val clickedId=buttonView?.id
        checkBoxesIds.forEachIndexed { index, id ->
            if (id == clickedId) callback.onExtraChecked(index,isChecked)
        }
    }
}