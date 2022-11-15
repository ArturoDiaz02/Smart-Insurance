package com.example.smart_insurance.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.DialogFragment
import com.example.smart_insurance.databinding.ProgressBarBinding

class ProgressCycleBar : DialogFragment() {

    private lateinit var binding: ProgressBarBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = ProgressBarBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }


}