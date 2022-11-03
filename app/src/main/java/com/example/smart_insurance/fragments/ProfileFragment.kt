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
import com.example.smart_insurance.views.ConfigureGroup
import com.example.smart_insurance.views.EditProfile
import com.example.smart_insurance.databinding.FragmentProfileBinding
import com.example.smart_insurance.model.User


class ProfileFragment(private val user: User) : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var listener: OnItemClickListener? = null

    @SuppressLint("WrongThread", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.textView6.text = user.name + " " + user.lassName

        var path = this.context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.path
        path += "/${user.profileImage}.jpg"

        val uri = Uri.parse(path)

        if (uri != null) {
            binding.imageView3.setImageURI(uri)
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
        fun newInstance(user: User) = ProfileFragment(user)
    }

    interface OnItemClickListener {
        fun logOut()
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }


}