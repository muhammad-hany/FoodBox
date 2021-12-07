package com.ertreby.foodbox.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ertreby.foodbox.R
import com.ertreby.foodbox.data.FirebaseService
import com.ertreby.foodbox.data.Meal
import com.ertreby.foodbox.data.Order
import com.ertreby.foodbox.databinding.FragmentOrderBinding
import com.ertreby.foodbox.ui.adapters.OrderPagerAdapter
import com.ertreby.foodbox.ui.fragments.OrderFragment.CheckBoxListener
import com.ertreby.foodbox.viewmodels.OrderViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class OrderFragment : Fragment() {


    lateinit var binding: FragmentOrderBinding
    private lateinit var meal: Meal
    private val viewModel: OrderViewModel by viewModels()

    val orderedExtras = mutableListOf<String>()
    val checkBoxesState = mutableListOf<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = requireArguments()
        meal = args.getParcelable("meal")!!
        meal.extras?.forEach { _ -> checkBoxesState.add(false) }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defineViews()

    }

    private fun defineViews() {
        Picasso.get().load(meal.imageUrl).into(binding.mealImage)
        binding.mealPrice.text = meal.price
        binding.mealName.text = meal.name
        binding.mealType.text = meal.type
        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        definePagerWithTaps()
        setCountText()

        viewModel.order.observe(viewLifecycleOwner){ userOrders->


            binding.cartButton.setOnClickListener {


                val bundle = Bundle()
                val arrayList= ArrayList(userOrders)
                bundle.putParcelableArrayList("orders",arrayList)
                findNavController().navigate(
                    R.id.action_orderFragment_to_cartFragment, bundle
                )
            }
        }



        binding.addToCartButton.setOnClickListener {
            meal.extras?.forEachIndexed { index: Int, extra: String ->
                if (checkBoxesState[index]) {
                    orderedExtras.add(index, extra)
                }
            }

            val order=Order(meal,orderedExtras,orderCount,FirebaseService.getUniqueId(), userId = Firebase.auth.currentUser?.uid, fulfilled = false)


            viewModel.submitOrder(order, ::onOrderSuccess)





        }

    }

    private fun onOrderSuccess(order: Order) {
        val bundle = Bundle()
        bundle.putParcelable("order", order)
        findNavController().navigate(
            R.id.action_orderFragment_to_cartFragment,
            bundle
        )
    }


    private fun getUserCartFromDB() {
        val db = Firebase.firestore
        val userId = Firebase.auth.uid.toString()
        val cartRef = db.collection("users").document(userId).collection("carts")
        cartRef.whereEqualTo("isItFulfilled", false).get()
            .addOnSuccessListener { cartsSnapshot ->
                if (cartsSnapshot.size() > 0) {
                    val carts = cartsSnapshot.toObjects<Order>()
                    val bundle = Bundle()
                    bundle.putParcelable("cart", carts[0])
                    findNavController().navigate(
                        R.id.action_orderFragment_to_cartFragment,
                        bundle
                    )
                } else {
                    Toast.makeText(
                        requireContext(),
                        "there is no saved carts !",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private var orderCount = 1
        set(value) {
            if (value in 1..9) {
                binding.mealQuantity.text = value.toString()
                field = value
            }
        }

    private fun setCountText() {
        binding.mealQuantity.text = orderCount.toString()
        binding.plusBtn.setOnClickListener { orderCount++ }
        binding.minusBtn.setOnClickListener { orderCount-- }
    }

    val callback = CheckBoxListener { position, condition ->
        checkBoxesState[position] = condition
    }

    private fun definePagerWithTaps() {
        binding.viewPager.adapter = OrderPagerAdapter(this, meal, callback)
        val tabNames = arrayOf("Details", "Extras", "Reviews")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()
    }


    fun interface CheckBoxListener {
        fun onExtraChecked(position: Int, condition: Boolean)
    }


}