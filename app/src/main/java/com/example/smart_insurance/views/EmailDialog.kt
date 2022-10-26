package com.example.smart_insurance.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import com.example.smart_insurance.databinding.DialogInputBinding

class EmailDialog (private val onSubmitClick: (String) -> Unit) : DialogFragment() {

    private lateinit var binding: DialogInputBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogInputBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)


        binding.bForgotEmail.setOnClickListener {
            onSubmitClick(binding.setEmail.text.toString())
            dismiss()
        }

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.CENTER)

        return dialog
    }
}
