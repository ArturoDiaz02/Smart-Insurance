package com.example.smart_insurance.views


import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.bumptech.glide.Glide
import com.example.smart_insurance.R
import com.example.smart_insurance.databinding.ActivityEditProfileBinding
import com.example.smart_insurance.model.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.gson.Gson

class EditProfile() : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    private lateinit var user: User

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        user = loadUser()

        val resultLauncher = registerForActivityResult(StartActivityForResult(), ::onGalleryResult)

        Firebase.storage.reference.child("profileImages").child(user.profileImage!!).downloadUrl.addOnSuccessListener {
            Glide.with(binding.imageView5).load(it).into(binding.imageView5)
        }.addOnFailureListener{
            binding.imageView5.setImageResource(R.drawable.addimg)
        }

        binding.imageButton3.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.imageView5.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

    }

    private fun onGalleryResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            binding.imageView5.setImageURI(data?.data)

            Firebase.storage.reference.child("profileImages").child(user.profileImage!!).putFile(data?.data!!)

        }

    }

    private fun loadUser(): User {
        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        return Gson().fromJson(json, User::class.java)

    }
}