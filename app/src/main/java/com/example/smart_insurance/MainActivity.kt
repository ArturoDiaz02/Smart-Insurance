package com.example.smart_insurance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.smart_insurance.databinding.ActivityMainBinding
import com.example.smart_insurance.fragments.AddFragment
import com.example.smart_insurance.fragments.HomeFragment
import com.example.smart_insurance.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

    }

    private fun itemEnable(enable: Boolean){
        binding.bottomNavigationView.menu.getItem(1).isEnabled = enable

        if (enable){
            binding.bottomNavigationView.menu.getItem(1).setIcon(R.drawable.ic_baseline_add_circle_24)
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

}