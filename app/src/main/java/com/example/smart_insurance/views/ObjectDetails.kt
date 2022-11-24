package com.example.smart_insurance.views

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.example.smart_insurance.databinding.ActivityObjectDetailsBinding
import com.example.smart_insurance.db.SqlOpenHelper
import com.bumptech.glide.request.target.Target
import com.example.smart_insurance.dialog.ProgressCycleBar
import com.example.smart_insurance.model.Insurance
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ObjectDetails : AppCompatActivity() {

    private lateinit var binding: ActivityObjectDetailsBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val progressBar = ProgressCycleBar()
        progressBar.show(supportFragmentManager, "progress")

        val sqlOpenHelper = SqlOpenHelper(this)
        val insurance = sqlOpenHelper.getAllInsurances()[intent.getIntExtra("insurance", 0)]

        Glide.with(binding.imageView).load(insurance.images).listener(object :
            RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                return false // important to return false so the error placeholder can be placed
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                binding.textView3.text = insurance.name
                binding.textView8.text = insurance.initDate
                binding.textView9.text = insurance.endDate
                binding.textView10.text = if (insurance.state == "true") "Activo" else "Inactivo"
                binding.textView5.text = insurance.description
                progressBar.dismiss()
                return false
            }
        }).centerCrop().into(binding.imageView)

        binding.imageButton4.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.button12.setOnClickListener {
            val userId = intent.getStringExtra("user")
            if(binding.textView10.text == "Activo") {

                Firebase.firestore.collection("insurances").document(userId!!).collection("insurances").document(
                    insurance.id.toString()
                ).update("state", "false")

                insurance.state = "false"
                sqlOpenHelper.update<Insurance>(insurance)
                binding.textView10.text = "Inactivo"

            }else{

                Firebase.firestore.collection("insurances").document(userId!!).collection("insurances").document(
                    insurance.id.toString()
                ).update("state", "true")

                insurance.state = "true"
                sqlOpenHelper.update<Insurance>(insurance)
                binding.textView10.text = "Activo"
            }

        }



    }
}