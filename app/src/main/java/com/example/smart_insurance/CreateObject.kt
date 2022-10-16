package com.example.smart_insurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_insurance.databinding.ActivityCreateObjectBinding

class CreateObject : AppCompatActivity() {

    private lateinit var binding: ActivityCreateObjectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton2.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}