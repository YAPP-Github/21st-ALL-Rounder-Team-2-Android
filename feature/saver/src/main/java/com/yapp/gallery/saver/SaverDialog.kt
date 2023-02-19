package com.yapp.gallery.saver

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yapp.gallery.saver.databinding.DialogSaverBinding
import kotlinx.coroutines.launch

class SaverDialog : BottomSheetDialogFragment() {

    private lateinit var _binding: DialogSaverBinding
    private val binding: DialogSaverBinding
        get() = _binding

    private val viewModel: SaverViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSaverBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            viewModel = this@SaverDialog.viewModel
            executePendingBindings()
        }

        binding.setOnTextChangeListener()

        lifecycleScope.launch {
            viewModel.publishRecord.collect {
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        requireActivity().finishAffinity()
    }

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    private fun DialogSaverBinding.setOnTextChangeListener() {
        etName.doOnTextChanged { text, _, _, _ ->
            this@SaverDialog.viewModel.update {
                copy(name = text.toString())
            }
        }

        etTitle.doOnTextChanged { text, _, _, _ ->
            this@SaverDialog.viewModel.update {
                copy(title = text.toString())
            }
        }

        tvSave.setOnClickListener {
            val postId = requireArguments().getLong("postId")

            this@SaverDialog.viewModel.onPublishRecord(postId)
        }

        tvSkip.setOnClickListener { dismiss() }
    }

    companion object {
        fun getInstance(postId: Long): SaverDialog {
            val bundle = bundleOf("postId" to postId)
            return SaverDialog().apply {
                arguments = bundle
            }
        }
    }
}
