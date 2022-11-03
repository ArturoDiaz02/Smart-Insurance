package com.example.smart_insurance.views

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.example.smart_insurance.databinding.ActivityCreateObjectBinding
import kotlin.properties.Delegates

class CreateObject : AppCompatActivity() {

    private lateinit var binding: ActivityCreateObjectBinding
    private lateinit var imageUri: Uri
    private var idImageView by Delegates.notNull<Int>()
    private var imageArray = ArrayList<Uri>()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                when (idImageView) {
                    0 -> binding.mainImg.setImageURI(imageUri)
                    1 -> binding.SecondaryOneImg.setImageURI(imageUri)
                    2 -> binding.SecondaryThreeImg.setImageURI(imageUri)
                    3 -> binding.SecondaryTwoImg.setImageURI(imageUri)
                    4 -> binding.SecondaryFourImg.setImageURI(imageUri)
                    else -> {
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
                imageArray.add(imageUri)
            }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton2.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

        }

        binding.mainImg.setOnClickListener {
            openCamera()
            idImageView = 0
        }

        binding.SecondaryOneImg.setOnClickListener {
            openCamera()
            idImageView = 1
        }

        binding.SecondaryThreeImg.setOnClickListener {
            openCamera()
            idImageView = 2
        }

        binding.SecondaryTwoImg.setOnClickListener {
            openCamera()
            idImageView = 3
        }

        binding.SecondaryFourImg.setOnClickListener {
            openCamera()
            idImageView = 4
        }

    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageUri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )!!
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        launcher.launch(intent)
    }
}