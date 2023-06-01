package com.geekymusketeers.presin.ui.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.geekymusketeers.medify.R
import com.geekymusketeers.medify.databinding.LayoutCustomEditTextBinding
import com.geekymusketeers.medify.utils.Utils.setNonDuplicateClickListener
import com.geekymusketeers.medify.utils.Utils.show


class CustomEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var binding: LayoutCustomEditTextBinding
    private var isOptional: Boolean = false
    private var passwordVisible: Boolean = false
    private var isPassword: Boolean = false
    private val showPasswordResId: Int = R.drawable.pass_show
    private val hidePasswordResId: Int = R.drawable.pass_hide

    init {
        initView(context, attrs)
    }


    @SuppressLint("CustomViewStyleable")
    private fun initView(context: Context, attrs: AttributeSet?) {
        binding = LayoutCustomEditTextBinding.inflate(LayoutInflater.from(context), this, true)
        context.obtainStyledAttributes(attrs, R.styleable.EditTextCustomLayout).apply {
            try {
                val header = getString(R.styleable.EditTextCustomLayout_header)
                val endDrawableIcon = getDrawable(R.styleable.EditTextCustomLayout_endIcon)
                val inputEnabled = getBoolean(R.styleable.EditTextCustomLayout_inputEnabled, true)
                isOptional = getBoolean(R.styleable.EditTextCustomLayout_optional, false)
                val hint = getString(R.styleable.EditTextCustomLayout_hint)
                val input = getString(R.styleable.EditTextCustomLayout_input)
                isPassword = getBoolean(R.styleable.EditTextCustomLayout_isPassword, false)
                val minHeight = getInt(R.styleable.EditTextCustomLayout_minimumHeight, 0)
                val isMultipleLine = getBoolean(R.styleable.EditTextCustomLayout_multiLine, false)
                val inputType = getInt(R.styleable.EditTextCustomLayout_inputType, InputType.TYPE_CLASS_TEXT)
                setHeader(header)
                setEndDrawableIcon(endDrawableIcon)
                setEditTextBoxEnabled(inputEnabled)
                setHint(hint)
                setEditTextBox(input)
                setOptional()
                if (isPassword) {
                    setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                } else {
                    setInputType(inputType)
                }
                if (isMultipleLine) {
                    switchToMultiLined()
                }
                if (minHeight != 0) {
                    setMinimHeight(minHeight)
                }
            } finally {
                recycle()
            }
        }
        binding.apply {
            editTextEndIcon.setOnClickListener {
                hideKeyboard()
                val currentTransformationMethod = editTextBox.transformationMethod
                editTextEndIcon.setImageResource(if (passwordVisible) showPasswordResId else hidePasswordResId)
                passwordVisible = passwordVisible.not()
                val newTransformationMethod = if (passwordVisible) null else PasswordTransformationMethod()
                editTextBox.text = editTextBox.text
                editTextBox.setSelection(editTextBox.text?.length ?: 0)
                if (currentTransformationMethod != newTransformationMethod) {
                    editTextBox.transformationMethod = newTransformationMethod
                }
            }
        }
    }

    private fun setInputType(inputType: Int) {
        binding.editTextBox.inputType = inputType
    }

    private fun setOptional() {
        if (isOptional)
            binding.editTextOptional.show()
    }

    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setHeader(header: String?) {
        binding.editTextHeader.text = header
    }

    fun setEndDrawableIcon(drawable: Drawable?) {
        drawable?.let {
            binding.editTextEndIcon.apply {
                setImageDrawable(it)
            }
        }
    }

    private fun switchToMultiLined() {
        binding.run {
            editTextBox.apply {
                isSingleLine = false
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
                maxLines = 3
                gravity = Gravity.START or Gravity.TOP
            }
        }
    }

    private fun setMinimHeight(height: Int) {
        binding.editTextBox.minimumHeight = height
    }

    private fun setEditTextBoxEnabled(isEnabled: Boolean) {
        binding.run {
            editTextBox.isEnabled = isEnabled
        }
    }

    fun setHint(hint: String?) {
        binding.editTextBox.apply {
            this.hint = hint
            setHintTextColor(ContextCompat.getColor(context, R.color.hint_color))
        }
    }

    fun setEditTextBox(text: String?) {
        binding.editTextBox.setText(text)
    }

    fun setImeOptionType(imeOption: Int) {
        binding.editTextBox.imeOptions = imeOption
    }

    fun setMultiLined(maxLength: Int) {
        binding.editTextBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s != null && maxLength > 0 && s.length >= maxLength) {
                    val cursorPos = binding.editTextBox.selectionEnd
                    val nextLineIndex = s.toString().indexOf('\n', cursorPos)
                    if (nextLineIndex < 0 || nextLineIndex > cursorPos + 1) {
                        s.append('\n')
                    }
                }
            }
        })
    }

    fun setMaxLines(maxLine: Int) {
        binding.editTextBox.maxLines = maxLine
    }

    fun setMaxLength(maxLength: Int) {
        binding.editTextBox.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
    }

    fun setLayoutListener(isFocusable: Boolean, listener: () -> Unit) {
        binding.run {
            editTextBox.isFocusable = isFocusable
            editTextBox.setNonDuplicateClickListener {
                listener.invoke()
            }
            root.setNonDuplicateClickListener {
                listener.invoke()
            }
            editTextBox.doAfterTextChanged {
                val input = it.toString()
//                setRequiredInput(input.isEmpty())
            }
        }
    }

    fun setUserInputListener(listener: ((input: String) -> Unit)? = null) {
        binding.editTextBox.doAfterTextChanged {
            val input = it.toString().trim()
            if (input.isEmpty()) {
                binding.editTextBox.typeface = ResourcesCompat.getFont(context, R.font.dm_sans)
            } else {
                binding.editTextBox.typeface = ResourcesCompat.getFont(context, R.font.dm_sans_medium)
            }
            listener?.invoke(input)
        }
    }

}