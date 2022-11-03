package com.example.smart_insurance.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smart_insurance.databinding.ActivityRegisterBinding
import com.example.smart_insurance.fragments.DatePickerFragment
import com.example.smart_insurance.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.button11.setOnClickListener {
            val progressDialog = ProgressCicleBar()
            progressDialog.show(supportFragmentManager, "progress")
            val pass = binding.editTextTextPassword.text.toString()
            val passAux = binding.editTextTextPassword2.text.toString()

            if (binding.editTextTextPersonName4.text.toString().isNotEmpty() &&
                binding.editTextTextPersonName5.text.toString().isNotEmpty() &&
                binding.editTextDate.text.toString().isNotEmpty() &&
                binding.editTextNumber.text.toString().isNotEmpty() &&
                binding.editTextTextEmailAddress.text.toString().isNotEmpty() &&
                binding.editTextTextPassword.text.toString().isNotEmpty() &&
                binding.editTextTextPassword2.text.toString().isNotEmpty()
            ) {

                if (pass == passAux) {
                    register(progressDialog)

                } else {

                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }

            } else {
                Toast.makeText(this, "Por favor, rellene todos los campos", Toast.LENGTH_SHORT)
                    .show()
                progressDialog.dismiss()
            }
        }

        binding.editTextDate.setOnClickListener {
            showDatePickerDialog()
        }

    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
        datePicker.show(supportFragmentManager, "datePicker")
    }

    @SuppressLint("SetTextI18n")
    private fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.editTextDate.setText("$day/$month/$year")
    }

    private fun register(progressDialog: ProgressCicleBar) {

        Firebase.auth.createUserWithEmailAndPassword(
            binding.editTextTextEmailAddress.text.toString(),
            binding.editTextTextPassword.text.toString()

        ).addOnSuccessListener {
            val filename = UUID.randomUUID().toString()

            val user = User(
                Firebase.auth.currentUser?.uid.toString(),
                binding.editTextTextPersonName4.text.toString(),
                binding.editTextTextPersonName5.text.toString(),
                binding.editTextDate.text.toString(),
                binding.editTextNumber.text.toString(),
                binding.editTextTextEmailAddress.text.toString(),
                filename
            )

            Firebase.firestore.collection("users").document(user.id).set(user)
                .addOnSuccessListener {
                    sendVerificationEmail()
                    finish()
                }

        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
        }


    }

    private fun sendVerificationEmail() {
        Firebase.auth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this, "Se ha enviado un correo de verificación", Toast.LENGTH_SHORT)
                .show()
        }?.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }


}