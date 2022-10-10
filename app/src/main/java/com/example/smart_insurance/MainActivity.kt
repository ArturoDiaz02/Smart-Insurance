package com.example.smart_insurance

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        vectorColor(0)

        binding.bottomAppBar.setNavigationOnClickListener{
            setFragment(HomeFragment.newInstance())
            vectorColor(0)
        }

        binding.bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_item_profile -> {
                    setFragment(ProfileFragment())
                    vectorColor(1)
                    true
                }
                else -> false
            }
        }

        binding.floatingActionButton.setOnClickListener {
            setFragment(AddFragment.newInstance())
            vectorColor(2)
        }

    }

    private fun setFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun vectorColor(id: Int){
        when(id) {
            0 -> {
                binding.bottomAppBar.navigationIcon?.setTint(getColor(R.color.select))
                binding.bottomAppBar.menu.getItem(0).icon?.setTint(getColor(R.color.white))
                binding.floatingActionButton.show()
            }

            1 -> {
                binding.bottomAppBar.navigationIcon?.setTint(getColor(R.color.white))
                binding.bottomAppBar.menu.getItem(0).icon?.setTint(getColor(R.color.select))
                binding.floatingActionButton.show()
            }

            2 -> {
                binding.bottomAppBar.navigationIcon?.setTint(getColor(R.color.white))
                binding.bottomAppBar.menu.getItem(0).icon?.setTint(getColor(R.color.white))
                binding.floatingActionButton.hide()
            }


        }
    }


}