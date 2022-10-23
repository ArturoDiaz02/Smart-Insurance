package com.example.smart_insurance.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.smart_insurance.views.ConfigureGroup
import com.example.smart_insurance.views.EditProfile
import com.example.smart_insurance.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var listener: OnItemClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.button5.setOnClickListener {
            val intent = Intent(this.context, EditProfile::class.java)
            startActivity(intent)
        }

        binding.button7.setOnClickListener {
            val intent = Intent(this.context, ConfigureGroup::class.java)
            startActivity(intent)
        }

        binding.button13.setOnClickListener {
            listener?.logOut()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }

    interface OnItemClickListener{
        fun logOut()
    }

    fun setListener(listener: OnItemClickListener){
        this.listener = listener
    }
}