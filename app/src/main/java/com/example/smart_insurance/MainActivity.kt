package com.example.smart_insurance

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.smart_insurance.databinding.ActivityMainBinding
import com.example.smart_insurance.fragments.AddFragment
import com.example.smart_insurance.fragments.HomeFragment
import com.example.smart_insurance.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragment(HomeFragment.newInstance())
        binding.bottomNavigationView.background = null
        binding.bottomNavigationView.menu.getItem(0).isChecked = true
        itemEnable(false)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_item_Home -> {
                    setFragment(HomeFragment.newInstance())
                    binding.floatingActionButton.show()
                    itemEnable(false)
                    true
                }
                R.id.menu_item_profile -> {
                    setFragment(ProfileFragment())
                    binding.floatingActionButton.show()
                    itemEnable(false)
                    true
                }
                else -> false
            }
        }

        binding.floatingActionButton.setOnClickListener {
            setFragment(AddFragment.newInstance())
            binding.floatingActionButton.hide()
            binding.bottomNavigationView.menu.getItem(1).isChecked = true
            itemEnable(true)

        }

        requestPermissions(arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)

    }

    private fun itemEnable(enable: Boolean){
        binding.bottomNavigationView.menu.getItem(1).isEnabled = enable

        if (enable){
            val timer = object: CountDownTimer(200, 40) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    binding.bottomNavigationView.menu.getItem(1).setIcon(R.drawable.ic_baseline_add_circle_24)

                }
            }
            timer.start()
            binding.bottomNavigationView.menu.getItem(1).title = "Add"


        }else{
            binding.bottomNavigationView.menu.getItem(1).icon = null
            binding.bottomNavigationView.menu.getItem(1).title = ""
        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}