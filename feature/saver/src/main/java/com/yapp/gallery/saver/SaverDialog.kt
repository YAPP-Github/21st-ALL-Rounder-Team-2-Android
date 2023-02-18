package com.yapp.gallery.saver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yapp.gallery.saver.databinding.DialogSaverBinding

class SaverDialog : BottomSheetDialogFragment() {

    private lateinit var _binding: DialogSaverBinding
    private val binding: DialogSaverBinding
        get() = _binding

    private val viewModel: SaverViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    companion object {
        fun show(fragmentManager: FragmentManager) =
            SaverDialog().show(fragmentManager, "saverDialog")
    }
}
