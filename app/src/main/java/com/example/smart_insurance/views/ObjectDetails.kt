package com.example.smart_insurance.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_insurance.databinding.ActivityObjectDetailsBinding

class ObjectDetails : AppCompatActivity() {

    private lateinit var binding: ActivityObjectDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton4.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.button2.setOnClickListener {
                val intent = Intent(this, EditObject::class.java)
            startActivity(intent)
        }
    }
}