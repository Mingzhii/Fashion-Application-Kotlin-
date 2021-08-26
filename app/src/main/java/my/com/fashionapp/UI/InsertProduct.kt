package my.com.fashionapp.UI

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import my.com.fashionapp.databinding.ActivityInsertProductBinding

class InsertProduct : AppCompatActivity() {

    private lateinit var binding: ActivityInsertProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}