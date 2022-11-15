package com.example.smart_insurance.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.smart_insurance.views.ConfigureGroup
import com.example.smart_insurance.views.EditProfile
import com.example.smart_insurance.databinding.FragmentProfileBinding
import com.example.smart_insurance.model.User
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var listener: OnItemClickListener? = null

    @SuppressLint("WrongThread", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences("smart_insurance", AppCompatActivity.MODE_PRIVATE)
        val json = sp.getString("user", "NO_USER")
        val user = Gson().fromJson(json, User::class.java)

        binding.textView6.text = user.name + " " + user.lassName

        val path = user.profileImage

        if (path != "") {
            Glide.with(binding.imageView3)
                .load(path)
                .into(binding.imageView3)

        }

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

    interface OnItemClickListener {
        fun logOut()
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }


}