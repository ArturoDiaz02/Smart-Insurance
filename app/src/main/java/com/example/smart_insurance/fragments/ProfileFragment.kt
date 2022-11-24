package com.example.smart_insurance.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.smart_insurance.views.ConfigureGroup
import com.example.smart_insurance.databinding.FragmentProfileBinding
import com.example.smart_insurance.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
        var json = sp.getString("user", "NO_USER")
        val user = Gson().fromJson(json, User::class.java)

        binding.textView6.text = user.name + " " + user.lassName

        val path = user.profileImage

        if (path != "") {
            Glide.with(binding.imageView3)
                .load(path)
                .into(binding.imageView3)

        }

        binding.button7.setOnClickListener {
            val intent = Intent(this.context, ConfigureGroup::class.java)
            startActivity(intent)
        }

        binding.button13.setOnClickListener {
            listener?.logOut()
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){

            if (it != null){

                Firebase.storage.reference.child("profileImages").child(user.id)
                    .putFile(it).addOnSuccessListener {
                        Firebase.storage.reference.child("profileImages").child(user.id)
                            .downloadUrl.addOnSuccessListener { image ->
                                Firebase.firestore.collection("users").document(user.id).update("profileImage", image.toString())
                                Glide.with(binding.imageView3).load(image).into(binding.imageView3)

                                user.profileImage = image.toString()

                                json = Gson().toJson(user)
                                sp.edit().putString("user", json).apply()


                            }
                    }

            }

        }

        binding.imageView3.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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