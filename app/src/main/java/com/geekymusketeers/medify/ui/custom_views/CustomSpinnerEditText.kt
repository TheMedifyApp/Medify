package com.geekymusketeers.presin.ui.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.LayoutCustomSpinnerBinding
import com.geekymusketeers.medify.utils.Utils.show


class CustomSpinnerEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutCustomSpinnerBinding
    private var spinnerList = mutableListOf<String>()
    private var editTextHint: String? = null
    private var isOptional = false
    private var dualTextFirstString: String? = null
    private var dualTextSecondString: String? = null
    private lateinit var dualTextSecondStringOnClick: (isClicked: Boolean) -> Unit?
    private lateinit var onItemSelectedListener: (item: String) -> Unit?
    private lateinit var dialog: CustomSearchSpinnerDialog


    init {
        initView(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = LayoutCustomSpinnerBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(attrs, R.styleable.SpinnerEditTextCustomLayout).apply {
            try {
                val header = getString(R.styleable.SpinnerEditTextCustomLayout_header)
                val inputEnabled =
                    getBoolean(R.styleable.SpinnerEditTextCustomLayout_inputEnabled, true)
                val hint = getString(R.styleable.SpinnerEditTextCustomLayout_hint)
                val input = getString(R.styleable.SpinnerEditTextCustomLayout_input)
                isOptional = getBoolean(R.styleable.SpinnerEditTextCustomLayout_optional, false)
                setHeader(header)
                setInputEnabled(inputEnabled)
                setHint(hint)
                setInput(input)
                setOptional()
                binding.spinnerTextLayout.setOnClickListener {
                    showDialog()
                }
            } finally {
                recycle()
            }
        }
    }

    private fun setOptional() {
        if (isOptional)
            binding.spinnerEditTextOptional.show()
    }

    fun setUpDialogData(
        inputList: List<String>,
        hint: String?,
        dualFirstText: String?,
        dualSecondText: String?,
        dualTextSecondStringOnClick: ((isClicked: Boolean) -> Unit?)?
    ) {
        spinnerList.apply {
            clear()
            addAll(inputList)
        }
        hint?.let { editTextHint = it }
        dualFirstText?.let { dualTextFirstString = it }
        dualSecondText?.let { dualTextSecondString = it }
        dualTextSecondStringOnClick?.let { this.dualTextSecondStringOnClick = it }
    }

    private fun showDialog() {
        dialog = CustomSearchSpinnerDialog(
            spinnerList,
            editTextHint,
            dualTextFirstString,
            dualTextSecondString
        ) {
            if (dualTextFirstString.isNullOrEmpty().not())
                dualTextSecondStringOnClick(it)
        }
        dialog.setOnItemSelectedListener {
            if (it.isNotEmpty()) {
                binding.spinnerTextView.apply {
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                    typeface = ResourcesCompat.getFont(context, R.font.dm_sans)
                    text = it
                }
            }
            onItemSelectedListener(it)
        }
        dialog.show(
            (context as AppCompatActivity).supportFragmentManager,
            "CustomSpinnerDialogFragment"
        )
    }

    fun getSelectedItemFromDialog(listener: (item: String) -> Unit) {
        onItemSelectedListener = listener
    }

    private fun setHeader(header: String?) {
        binding.spinnerEditTextHeader.text = header
    }

    private fun setInputEnabled(isEnabled: Boolean) {
        binding.run {
            spinnerTextView.isEnabled = isEnabled
            spinnerTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    if (isEnabled) R.color.dark_grey else R.color.black
                )
            )
        }
    }

    private fun setHint(hint: String?) {
        binding.spinnerTextView.apply {
            this.hint = hint
            setTextColor(ResourcesCompat.getColor(resources, R.color.hint_color, null))
        }
    }

    private fun setInput(text: String?) {
        binding.spinnerTextView.apply {
            this.text = text
            binding.spinnerTextView.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.black
                )
            )
        }
    }
}