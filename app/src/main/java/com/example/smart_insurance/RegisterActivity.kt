package com.example.smart_insurance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.smart_insurance.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            go()
        }

        binding.button11.setOnClickListener {
            go()
        }
    }

    private fun go(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }


}