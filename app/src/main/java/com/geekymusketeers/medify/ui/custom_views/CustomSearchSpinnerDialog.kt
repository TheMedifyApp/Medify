package com.geekymusketeers.presin.ui.custom_views

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.geekymusketeers.medify.adapter.CustomSpinnerAdapter
import com.geekymusketeers.medify.databinding.LayoutCustomSearchSpinnerDialogBinding
import com.geekymusketeers.medify.utils.Utils.hide
import com.geekymusketeers.medify.utils.Utils.show
import com.geekymusketeers.medify.utils.Utils.toStringWithoutSpaces

class CustomSearchSpinnerDialog(
    private val itemList: List<String>,
    private val editTextHint: String?,
    private val dualTextFirstString: String?,
    private val dualTextSecondString: String?,
    private val dualTextSecondStringOnClick: (isClicked: Boolean) -> Unit?
) : DialogFragment() {

    private var onItemSelectedListener: ((item: String) -> Unit)? = null
    private var _binding: LayoutCustomSearchSpinnerDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CustomSpinnerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutCustomSearchSpinnerDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        setDualTextsIfPresent()

        adapter = CustomSpinnerAdapter { item ->
            onItemSelectedListener?.invoke(item)
            dismiss()
        }

        return binding.root
    }

    private fun setDualTextsIfPresent() {
        binding.spinnerCreateNewDualText.apply {
            if (dualTextFirstString.isNullOrEmpty().not()) {
                firstTextView.text = dualTextFirstString
            }
            if (dualTextSecondString.isNullOrEmpty().not()) {
                secondTextView.text = dualTextSecondString
                secondTextView.setOnClickListener {
                    dualTextSecondStringOnClick(true)
                    dismiss()
                }
            }
        }
    }

    fun setOnItemSelectedListener(listener: (item: String) -> Unit) {
        onItemSelectedListener = listener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerSearchBarEditText.hint = editTextHint

        binding.spinnerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CustomSearchSpinnerDialog.adapter
            this.setHasFixedSize(false)
        }
        adapter.setItem(itemList)

        binding.spinnerSearchBarEditText.addTextChangedListener(textWatcher)
        binding.spinnerClearTextImageView.setOnClickListener {
            binding.spinnerSearchBarEditText.setText("")
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.isNullOrEmpty()) {
                binding.spinnerClearTextImageView.hide()
                adapter.setItem(itemList)
            } else {
                val filterList = mutableListOf<String>()
                binding.spinnerClearTextImageView.show()
                for (item in itemList) {
                    if ((item.lowercase().toStringWithoutSpaces()).contains(s.toString().lowercase().toStringWithoutSpaces())) {
                        filterList.add(item)
                    }
                }
                adapter.setItem(filterList)
            }
            binding.spinnerRecyclerView.smoothScrollToPosition(0)
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}