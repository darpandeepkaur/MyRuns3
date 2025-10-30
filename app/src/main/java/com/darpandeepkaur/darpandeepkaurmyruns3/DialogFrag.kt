package com.darpandeepkaur.darpandeepkaurmyruns3

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment

class DialogFrag : DialogFragment(), DialogInterface.OnClickListener{

    interface onValueCapturedListener {
        fun onValueCaptured(value: String)
    }
    companion object {
        const val DIALOG_KEY = "dialog"
        const val TITLE_KEY = "title"
        const val LABEL_KEY = "label"
        const val MANUAL_DIALOG = 0
        const val INT_DIALOG = 1
        const val TEXT_DIALOG = 2

    }

    private var listener: onValueCapturedListener? = null
    private var inputEditText: EditText? = null

    fun setOnValueCapturedListener(l: onValueCapturedListener){
        listener = l
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bundle = arguments
        val dialogId = bundle?.getInt(DIALOG_KEY) ?: MANUAL_DIALOG
        val title = bundle?.getString(TITLE_KEY) ?: "Input"
        val hint = bundle?.getString(LABEL_KEY) ?: "Enter value"

        val view: View = requireActivity().layoutInflater.inflate(R.layout.frag_dialog, null)

        inputEditText = view.findViewById(R.id.edit_input)
        inputEditText?.hint = hint

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(title)

        when (dialogId) {
            INT_DIALOG -> {
                inputEditText?.inputType = android.text.InputType.TYPE_CLASS_NUMBER
            }
            TEXT_DIALOG -> {
                inputEditText?.inputType = android.text.InputType.TYPE_CLASS_TEXT
            }
            MANUAL_DIALOG -> {
                inputEditText = null
            }

        }

        builder.setView(view)
        builder.setPositiveButton("ok", this)
        builder.setNegativeButton("cancel", this)
        return builder.create()
    }

    override fun onClick(dialog: DialogInterface, which: Int){
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                if (inputEditText == null) {
                    Toast.makeText(activity, "ok clicked", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    return
                }
                val input = inputEditText!!.text.toString().trim()
                if (input.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter a value", Toast.LENGTH_SHORT).show()
                    return
                }
                val dialogId = arguments?.getInt(DIALOG_KEY) ?: MANUAL_DIALOG
                if (dialogId == INT_DIALOG) {
                    val numValue = input.toIntOrNull()
                    if (numValue == null) {
                        Toast.makeText(requireContext(), "Please enter a valid number", Toast.LENGTH_SHORT).show()
                        return
                    }
                }

                listener?.onValueCaptured(input)
                dialog.dismiss()
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                dialog.dismiss()
            }
        }
    }
}