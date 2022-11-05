package com.example.smart_insurance.views

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.smart_insurance.R
import com.example.smart_insurance.databinding.ActivityLoginBinding
import com.example.smart_insurance.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import com.example.smart_insurance.dialog.EmailDialog
import com.example.smart_insurance.dialog.ProgressCycleBar
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.Insurance
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.storage.ktx.storage
import com.google.gson.reflect.TypeToken
import java.util.UUID


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")

        if (json != "NO_USER") {
            loadCategories()
            loadInsurance(Gson().fromJson(json, User::class.java))

        } else {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.button.setOnClickListener {
                val progressBar = ProgressCycleBar()
                progressBar.show(supportFragmentManager, "progress")
                loginEmailPassword(progressBar)
            }

            binding.button3.setOnClickListener {
                loginGoogle()
            }

            binding.button4.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            binding.forgot.setOnClickListener {
                val dialog = EmailDialog { email ->

                    if (email.isNotEmpty()) {
                        Firebase.auth.sendPasswordResetEmail(email).addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Se envio un correo para restablecer su contraseña",
                                Toast.LENGTH_SHORT
                            ).show()

                        }.addOnFailureListener {
                            Toast.makeText(this, "No se pudo enviar el correo", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this, "Ingrese un correo", Toast.LENGTH_SHORT).show()
                    }


                }

                dialog.show(supportFragmentManager, "EmailDialog")
            }
        }

    }

    private fun loginGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)

        val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            val progressBar = ProgressCycleBar()
            progressBar.show(supportFragmentManager, "progress")

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                val account = task.result

                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    Firebase.auth.signInWithCredential(credential).addOnSuccessListener {

                        val filename = UUID.randomUUID().toString()

                        val user = User(
                            Firebase.auth.currentUser?.uid.toString(),
                            account.givenName!!,
                            "",
                            "00/00/0000",
                            "",
                            account.email!!,
                            filename

                        )

                        Firebase.firestore.collection("users").document(user.id).set(user)
                            .addOnSuccessListener {
                                saveUser(user)
                                loadCategories()
                                loadInsurance(user)
                            }

                        account.photoUrl?.let { it1 ->
                            Firebase.storage.reference.child("profile").child(filename).putFile(
                                it1
                            )
                        }

                    }.addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        progressBar.dismiss()
                    }
                }

            }
        }

        resultLauncher.launch(googleClient.signInIntent)

    }

    private fun loginEmailPassword(progressBar: ProgressCycleBar) {
        val userName = binding.editTextTextPersonName2.text.toString()
        val password = binding.editTextTextPersonName3.text.toString()

        if (userName.isNotEmpty() && password.isNotEmpty()) {
            Firebase.auth.signInWithEmailAndPassword(userName, password).addOnSuccessListener {
                val fbUser = Firebase.auth.currentUser

                if (fbUser!!.isEmailVerified) {

                    Firebase.firestore.collection("users").document(fbUser.uid).get()
                        .addOnSuccessListener {
                            val user = it.toObject(User::class.java)!!
                            saveUser(user)
                            loadCategories()
                            loadInsurance(user)

                        }

                } else {
                    Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                progressBar.dismiss()
            }

        } else {
            Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT)
                .show()
            progressBar.dismiss()
        }

    }

    private fun saveUser(user: User) {
        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        val json = Gson().toJson(user)
        sp.edit().putString("user", json).apply()
    }

    private fun goMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun loadCategories() {

        var categories = ArrayList<Category>()
        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        var json = sp.getString("categories", "NO_CATEGORIES")

        if (json != "NO_CATEGORIES") {
            categories = Gson().fromJson(json, object : TypeToken<ArrayList<Category>>() {}.type)
        }

        Firebase.firestore.collection("categories").orderBy("id")
            .addSnapshotListener { value, _ ->
                for (change in value!!.documentChanges) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val category = change.document.toObject(Category::class.java)
                            var validation = true

                            categories.forEach {
                                if (it.id == category.id) {
                                    validation = false
                                }
                            }

                            if (validation) {
                                categories.add(category)
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val category = change.document.toObject(Category::class.java)
                            categories[change.newIndex] = category
                        }

                        DocumentChange.Type.REMOVED -> {
                            categories.removeAt(change.oldIndex)
                        }
                    }
                }

                json = Gson().toJson(categories)
                sp.edit().putString("categories", json).apply()

            }

    }

    private fun loadInsurance(user: User) {

        var insurances = ArrayList<Insurance>()
        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        var json = sp.getString("insurances", "NO_INSURANCES")

        if (json != "NO_INSURANCES") {
            insurances = Gson().fromJson(json, object : TypeToken<ArrayList<Insurance>>() {}.type)
        }

        Firebase.firestore.collection("insurance").document(user.id).collection("insurances")
            .orderBy("id").addSnapshotListener { value, _ ->
            for (change in value!!.documentChanges) {
                when (change.type) {
                    DocumentChange.Type.ADDED -> {
                        val insurance = change.document.toObject(Insurance::class.java)
                        var validation = true

                        insurances.forEach {
                            if (it.id == insurance.id) {
                                validation = false
                            }
                        }

                        if (validation) {
                            insurances.add(insurance)
                        }
                    }

                    DocumentChange.Type.MODIFIED -> {
                        val category = change.document.toObject(Insurance::class.java)
                        insurances[change.newIndex] = category
                    }

                    DocumentChange.Type.REMOVED -> {
                        insurances.removeAt(change.oldIndex)
                    }
                }
            }

            json = Gson().toJson(insurances)
            sp.edit().putString("insurances", json).apply()

            goMain()

        }

    }

}