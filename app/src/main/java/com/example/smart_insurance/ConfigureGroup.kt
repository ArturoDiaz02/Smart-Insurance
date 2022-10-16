package com.example.smart_insurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_insurance.databinding.ActivityConfigureGroupBinding

class ConfigureGroup : AppCompatActivity() {

    private lateinit var binding: ActivityConfigureGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfigureGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.backButton.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

}