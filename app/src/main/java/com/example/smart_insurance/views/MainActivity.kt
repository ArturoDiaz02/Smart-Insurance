package com.example.smart_insurance.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.smart_insurance.R
import com.example.smart_insurance.databinding.ActivityMainBinding
import com.example.smart_insurance.db.SqlOpenHelper
import com.example.smart_insurance.fragments.AddFragment
import com.example.smart_insurance.fragments.HomeFragment
import com.example.smart_insurance.fragments.ProfileFragment
import com.example.smart_insurance.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), ProfileFragment.OnItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var user: User

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login()

        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(0).isChecked = true

        itemEnable(false)
        setFragment(HomeFragment.newInstance(user))


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_Home -> {
                    setFragment(HomeFragment.newInstance(user))
                    binding.floatingActionButton.show()
                    itemEnable(false)
                    true
                }
                R.id.menu_item_profile -> {
                    val fragment = ProfileFragment.newInstance()
                    fragment.setListener(this)
                    setFragment(fragment)
                    binding.floatingActionButton.show()
                    itemEnable(false)
                    true
                }
                else -> false
            }
        }

        binding.floatingActionButton.setOnClickListener {
            setFragment(AddFragment.newInstance(user))
            binding.floatingActionButton.hide()
            binding.bottomNavigationView.menu.getItem(1).isChecked = true
            itemEnable(true)

        }

        requestPermissions(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1
        )

    }

    private fun itemEnable(enable: Boolean) {
        binding.bottomNavigationView.menu.getItem(1).isEnabled = enable

        if (enable) {
            val timer = object : CountDownTimer(200, 40) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    binding.bottomNavigationView.menu.getItem(1)
                        .setIcon(R.drawable.ic_baseline_add_circle_24)

                }
            }
            timer.start()
            binding.bottomNavigationView.menu.getItem(1).title = "Add"


        } else {
            binding.bottomNavigationView.menu.getItem(1).icon = null
            binding.bottomNavigationView.menu.getItem(1).title = ""
        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    private fun login() {

        if (Firebase.auth.currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        } else {
            user = loadUser()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun loadUser(): User {
        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        return Gson().fromJson(json, User::class.java)

    }

    override fun logOut() {
        SqlOpenHelper(this).clean()
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
        val sp = getSharedPreferences("smart_insurance", MODE_PRIVATE)
        sp.edit().clear().apply()
        Firebase.auth.signOut()
    }


}