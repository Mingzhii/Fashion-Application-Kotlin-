package my.com.fashionapp.UI

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import my.com.fashionapp.R
import my.com.fashionapp.data.CartViewModel
import my.com.fashionapp.data.UserViewModel
import my.com.fashionapp.databinding.FragmentPaymentHistoryBinding
import my.com.fashionapp.util.CartAdapter
import my.com.fashionapp.util.HistoryAdapter
import my.com.fashionapp.util.ProductAdapter


class PaymentHistoryFragment : Fragment() {

    private lateinit var binding: FragmentPaymentHistoryBinding
    private val nav by lazy{ findNavController() }
    private val vm: CartViewModel by activityViewModels()
    private val vmU: UserViewModel by activityViewModels()

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentHistoryBinding.inflate(inflater, container, false)

        //TODO

        val btn : BottomNavigationView = requireActivity().findViewById(R.id.bottom_navigation)
        btn.visibility = View.GONE
        val btn1 : BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationDelivery)
        btn1.visibility = View.GONE

        binding.imgHistoryBack.setOnClickListener { nav.navigate(R.id.action_paymentHistoryFragment_to_loginProfileFragment) }

        adapter = HistoryAdapter() { holder, cart ->
            // Item click

        }

        binding.rv.adapter = adapter
        binding.rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        val preferences = activity?.getSharedPreferences("email", Context.MODE_PRIVATE)
        val emailLogin = preferences?.getString("emailLogin","")

        val u = emailLogin?.let { vmU.getEmail(it) }

        vm.getAll().observe(viewLifecycleOwner) { list ->

            var cartArray = list.filter { c ->
                c.cartUsername == u?.userName && c.cartStatus == "Done The Payment"
            }
            adapter.submitList(cartArray)
        }


        return binding.root
    }

}