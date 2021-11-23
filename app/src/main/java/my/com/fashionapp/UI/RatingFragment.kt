package my.com.fashionapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import my.com.fashionapp.R
import my.com.fashionapp.databinding.FragmentRatingBinding

class RatingFragment : Fragment() {

    private lateinit var binding: FragmentRatingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        binding = FragmentRatingBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            val num = (binding.ratingBar.rating).toString()


            Toast.makeText(context,num, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }


}