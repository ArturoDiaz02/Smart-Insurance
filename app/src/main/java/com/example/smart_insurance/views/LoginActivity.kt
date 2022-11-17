package com.example.smart_insurance.views

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.smart_insurance.R
import com.example.smart_insurance.databinding.ActivityLoginBinding
import com.example.smart_insurance.db.SqlOpenHelper
import com.example.smart_insurance.dialog.EmailDialog
import com.example.smart_insurance.dialog.ProgressCycleBar
import com.example.smart_insurance.model.Category
import com.example.smart_insurance.model.Insurance
import com.example.smart_insurance.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var user: User
    private lateinit var sqlOpenHelper: SqlOpenHelper

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sqlOpenHelper = SqlOpenHelper(this)

        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")

        if (json != "NO_USER") {
            user = Gson().fromJson(json, User::class.java)
            loadCategories()
            loadInsurance()
            object: CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                   goMain()
                }
            }.start()

        } else {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.button.setOnClickListener {
                val progressBar = ProgressCycleBar()
                progressBar.show(supportFragmentManager, "progress")
                loginEmailPassword(progressBar)
                object: CountDownTimer(4000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        goMain()
                    }
                }.start()
            }

            binding.button3.setOnClickListener {
                loginGoogle()
                object: CountDownTimer(4000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        goMain()
                    }
                }.start()
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

    @RequiresApi(Build.VERSION_CODES.M)
    private val resultLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        val progressBar = ProgressCycleBar()
        progressBar.show(supportFragmentManager, "progress")

        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            val account = task.result

            if (account != null) {
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                Firebase.auth.signInWithCredential(credential).addOnSuccessListener {

                    if(validationEmail(account.email.toString())) {

                        user = User(
                            Firebase.auth.currentUser?.uid.toString(),
                            account.givenName!!,
                            "",
                            "00/00/0000",
                            "",
                            account.email!!,
                            account.photoUrl.toString()

                        )

                        Firebase.firestore.collection("users").document(user.id).set(user)
                            .addOnSuccessListener {
                                saveUser(user)
                                loadCategories()
                                loadInsurance()
                            }

                    } else {
                        Firebase.firestore.collection("users").whereEqualTo("email", account.email)
                            .get().addOnSuccessListener { result ->
                                result.documents[0].toObject(User::class.java)?.let { it1 ->
                                    saveUser(it1)
                                    loadCategories()
                                    loadInsurance()
                                }

                            }
                    }



                }.addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                    progressBar.dismiss()
                }
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loginGoogle() {
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, googleConf)

        resultLauncher.launch(googleClient.signInIntent)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loginEmailPassword(progressBar: ProgressCycleBar) {
        val userName = binding.editTextTextPersonName2.text.toString()
        val password = binding.editTextTextPersonName3.text.toString()

        if (userName.isNotEmpty() && password.isNotEmpty() && !validationEmail(userName)) {

            Firebase.auth.signInWithEmailAndPassword(userName, password).addOnSuccessListener {
                val fbUser = Firebase.auth.currentUser

                if (fbUser!!.isEmailVerified) {

                    Firebase.firestore.collection("users").document(fbUser.uid).get()
                        .addOnSuccessListener {
                            user = it.toObject(User::class.java)!!
                            saveUser(user)
                            loadCategories()
                            loadInsurance()

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
            Toast.makeText(this, "Please enter your username and password or email already exist", Toast.LENGTH_SHORT)
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

        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        val localVersion = sp.getString("vCategories", "0.0")!!.toDouble()

        Firebase.remoteConfig.fetchAndActivate().addOnSuccessListener {

            val remoteVersion =
                Firebase.remoteConfig.getString("CATEGORY_DATABASE_VERSION").toDouble()

            if (remoteVersion > localVersion) {
                Firebase.firestore.collection("categories").orderBy("id").get()
                    .addOnSuccessListener {
                        sqlOpenHelper.queryToTable("DELETE FROM CATEGORIES")

                        for (document in it) {
                            val category = document.toObject(Category::class.java)
                            sqlOpenHelper.insert(category)
                        }

                    }

                sp.edit().putString("vCategories", remoteVersion.toString()).apply()
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun loadInsurance() {
        val insurances = sqlOpenHelper.getAllInsurances()

        Firebase.firestore.collection("insurance").document(user.id).collection("insurances")
            .orderBy("id").addSnapshotListener { value, _ ->
                for (change in value!!.documentChanges) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            val insurance = change.document.toObject(Insurance::class.java)
                            var validation = true

                            for (i in insurances) {
                                if (i.id == insurance.id) {
                                    validation = false
                                }
                            }

                            if (validation) {
                                sqlOpenHelper.insert(insurance)
                            }

                        }

                        DocumentChange.Type.MODIFIED -> {
                            val insurance = change.document.toObject(Insurance::class.java)
                            sqlOpenHelper.update(insurance)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val insurance = change.document.toObject(Insurance::class.java)
                            sqlOpenHelper.delete(insurance)
                        }
                    }
                }

            }
    }

    private fun validationEmail(email: String): Boolean{

        var validation = false

        Firebase.auth.fetchSignInMethodsForEmail(email).addOnSuccessListener {
            validation = it.signInMethods!!.isNotEmpty()
        }

        return validation
    }
}