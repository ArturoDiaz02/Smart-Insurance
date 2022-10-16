package com.example.smart_insurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_insurance.databinding.ActivityCreateObjectBinding
import com.example.smart_insurance.databinding.ActivityEditObjectBinding

class EditObject : AppCompatActivity() {

    private lateinit var binding: ActivityEditObjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton5.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}