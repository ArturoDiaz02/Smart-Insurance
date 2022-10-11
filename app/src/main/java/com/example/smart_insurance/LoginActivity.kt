package com.example.smart_insurance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smart_insurance.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener{
            goMain()
        }

        binding.button3.setOnClickListener{
            goMain()
        }
    }

    private fun goMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}