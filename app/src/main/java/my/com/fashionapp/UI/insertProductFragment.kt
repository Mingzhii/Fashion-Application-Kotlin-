package my.com.fashionapp.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.selects.select
import my.com.fashionapp.R
import my.com.fashionapp.data.Product
import my.com.fashionapp.data.ProductViewModel
import my.com.fashionapp.databinding.FragmentInsertProductBinding
import my.com.fashionapp.util.cropToBlob
import my.com.fashionapp.util.errorDialog

class insertProductFragment : Fragment() {

    private lateinit var binding: FragmentInsertProductBinding
    private val nav by lazy { findNavController() }
    private val vm: ProductViewModel by activityViewModels()

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            binding.imgProduct.setImageURI(it.data?.data)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInsertProductBinding.inflate(inflater, container, false)

        // TODO
        binding.imgProduct.setOnClickListener { selectImage() }
        binding.btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        launcher.launch(intent)
    }

    private fun submit() {
        val id = "PROD00"
        val p = Product(
            productId = id + vm.calSize().toString() + 1,
            productName = binding.edtProductName.text.toString().trim(),
            productDescrip = binding.edtDescription.text.toString().trim(),
            productQuan = binding.edtQuantity.text.toString().toIntOrNull() ?: 0,
            productPhoto = binding.imgProduct.cropToBlob(300,300),
        )

        val err = vm.validate(p)
        if (err != ""){
            errorDialog(err)
            return
        }
        vm.set(p)
    }

}