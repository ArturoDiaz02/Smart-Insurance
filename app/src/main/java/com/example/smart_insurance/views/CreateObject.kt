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
import com.example.smart_insurance.dialog.ProgressCycleBar
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.Insurance
import com.example.smart_insurance.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.properties.Delegates

class CreateObject : AppCompatActivity() {

    private lateinit var binding: ActivityCreateObjectBinding
    private lateinit var imageUri: Uri
    private var idImageView by Delegates.notNull<Int>()
    private var imageArray = ArrayList<Uri>()
    private val progressBar = ProgressCycleBar()
    private var amount = 0

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBtn.setOnClickListener {
            progressBar.show(supportFragmentManager, "progress")
            createData()

        }

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

    private fun createData() {

        if (imageArray.size == 5 &&
            binding.editTextTextPersonName.text.isNotEmpty() &&
            binding.editTextPrice.text.isNotEmpty()
        ) {
            Firebase.firestore.collection("categories")
                .whereEqualTo("id", intent.getIntExtra("position", -1)).get()
                .addOnSuccessListener { result ->
                    val category = result.documents[0].toObject(Category::class.java)

                    val initDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        LocalDateTime.now()
                    } else {
                        TODO("VERSION.SDK_INT < O")
                    }

                    val endDate = initDate.plusDays(30)

                    var insuranceImages = ""
                    val userId = intent.getStringExtra("userId")!!

                    imageArray.forEach { uri ->
                        val uuid = UUID.randomUUID()
                        val storageRef =
                            Firebase.storage.reference.child("insurancesImages").child(userId)
                                .child("$uuid.jpg")
                        storageRef.putFile(uri).addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                insuranceImages += "$uri,"
                                amount++

                                if (amount == imageArray.size) {
                                    createInsurance(
                                        initDate,
                                        endDate,
                                        category!!,
                                        insuranceImages,
                                        userId
                                    )
                                }
                            }
                        }


                    }

                }

        }else{
            progressBar.dismiss()
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createInsurance(initDate: LocalDateTime, endDate: LocalDateTime, category: Category, insuranceImages: String, userId: String) {

        Firebase.firestore.collection("users").document(userId).get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!

            val insurance = Insurance(
                user.totalInsurance,
                binding.editTextTextPersonName.text.toString(),
                initDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                binding.editTextPrice.text.toString(),
                "Pendiente",
                category.id,
                category.image,
                category.color,
                insuranceImages,
                "true"
            )

            Firebase.firestore.collection("insurances").document(userId).collection("insurances").document(
                insurance.id.toString()
            ).set(insurance)
                .addOnSuccessListener {
                    Firebase.firestore.collection("users").document(userId).update("totalInsurance", user.totalInsurance + 1)
                        .addOnSuccessListener {
                            progressBar.dismiss()
                            Toast.makeText(this, "Asegurado creado", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                }
                .addOnFailureListener {
                    progressBar.dismiss()
                    Toast.makeText(this, "Error al crear el asegurado", Toast.LENGTH_SHORT).show()
                }
        }


    }
}