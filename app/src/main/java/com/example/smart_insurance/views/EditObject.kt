package com.example.smart_insurance.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.smart_insurance.databinding.ActivityCreateObjectBinding
import com.example.smart_insurance.databinding.ActivityEditObjectBinding

class EditObject : AppCompatActivity() {

    private lateinit var binding: ActivityEditObjectBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton5.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }
}