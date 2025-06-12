package com.example.skripsi.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.skripsi.R

class CustomDialogFragment : DialogFragment() {

    interface ConfirmationListener {
        fun onConfirm()
        fun onCancel()
    }

    private var listener: ConfirmationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_warning, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val iconResId = arguments?.getInt(ARG_ICON)
        val title = arguments?.getString(ARG_TITLE)
        val subtitle = arguments?.getString(ARG_SUBTITLE)
        val positiveButtonText = arguments?.getString(ARG_POSITIVE_BTN)
        val negativeButtonText = arguments?.getString(ARG_NEGATIVE_BTN)

        iconResId?.let { view.findViewById<ImageView>(R.id.iv_dialog_icon).setImageResource(it) }
        view.findViewById<TextView>(R.id.tv_dialog_title).text = title
        view.findViewById<TextView>(R.id.tv_dialog_subtitle).text = subtitle
        view.findViewById<Button>(R.id.btn_positive).text = positiveButtonText
        view.findViewById<Button>(R.id.btn_negative).text = negativeButtonText

        view.findViewById<Button>(R.id.btn_positive).setOnClickListener {
            listener?.onConfirm()
            dismiss()
        }

        view.findViewById<Button>(R.id.btn_negative).setOnClickListener {
            listener?.onCancel()
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    companion object {
        private const val ARG_ICON = "arg_icon"
        private const val ARG_TITLE = "arg_title"
        private const val ARG_SUBTITLE = "arg_subtitle"
        private const val ARG_POSITIVE_BTN = "arg_positive_btn"
        private const val ARG_NEGATIVE_BTN = "arg_negative_btn"

        fun newInstance(
            iconResId: Int,
            title: String,
            subtitle: String,
            positiveButtonText: String,
            negativeButtonText: String,
            listener: ConfirmationListener
        ): CustomDialogFragment {
            val fragment = CustomDialogFragment()
            fragment.listener = listener
            fragment.arguments = Bundle().apply {
                putInt(ARG_ICON, iconResId)
                putString(ARG_TITLE, title)
                putString(ARG_SUBTITLE, subtitle)
                putString(ARG_POSITIVE_BTN, positiveButtonText)
                putString(ARG_NEGATIVE_BTN, negativeButtonText)
            }
            return fragment
        }
    }
}