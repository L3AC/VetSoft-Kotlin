package com.example.vetsoft.Validation

import android.text.InputFilter
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.vetsoft.btnConfirm2

class Validat {
    fun isEmailValid(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun validateEmail(editText: EditText,btn:Button): Boolean {
        val email = editText.text.toString().trim()
        val isValid = isEmailValid(email)
        if (!isValid) {
            editText.error = "Correo electrónico inválido"
            btn.isEnabled=false

        } else {
            editText.error = null
            btn.isEnabled=true
        }
        return isValid
    }
    fun setupET(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("[a-zA-Z\\s]*") // Expresión regular para letras y espacios
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }
    fun setupUC(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("^[a-zA-Z0-9]+$") // Expresión regular para letras y numeros
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }
    fun setupNumb(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            val pattern = Regex("[0-9]+") // Expresión regular para numeros
            if (pattern.matches(source)) {
                source
            } else {
                "" // Si no coincide con la expresión regular, se rechaza el carácter
            }
        }
        editText.filters = arrayOf(filter)
    }
    fun areFieldsNotEmpty(editTextList: List<EditText>): Boolean {
        for (editText in editTextList) {
            val text = editText.text.toString().trim()
            if (text.isEmpty()) {
                return false
            }
        }
        return true
    }
    fun setMax(editText: EditText, maxLength: Int) {
        val filterArray = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        editText.filters = filterArray
    }
}