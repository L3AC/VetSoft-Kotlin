package com.example.vetsoft.Controlador.validation
import android.text.InputFilter
import android.text.Spanned
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText

class Validat {
        fun isEmailValid(email: String): Boolean {
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }

        fun validateEmail(editText: EditText, btn: Button): Boolean {
            val email = editText.text.toString().trim()
            val isValid = isEmailValid(email)
            if (!isValid) {
                editText.error = "Correo electrónico inválido"
                btn.isEnabled = false

            } else {
                editText.error = null
                btn.isEnabled = true
            }
            return isValid
        }

        fun setupET(editTextList: List<EditText>) {
            for (editText in editTextList) {
                val filter = InputFilter { source, _, _, _, _, _ ->
                    val pattern = Regex("[a-zA-Z\\s]+") // Expresión regular para letras y espacios
                    if (pattern.matches(source)) {
                        source
                    } else {
                        "" // Si no coincide con la expresión regular, se rechaza el carácter
                    }
                }
                editText.filters = arrayOf(filter)
            }
        }

        fun setupUC(editTextList: List<EditText>) {
            for (editText in editTextList) {
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
        }

        fun setupNumb(editTextList: List<EditText>) {
            for (editText in editTextList) {
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
        fun configEditText(editText: EditText,maxLength:Int,validar:String) {
            // Limitar a 15 caracteres
            val filterArray = arrayOfNulls<InputFilter>(1)
            filterArray[0] = InputFilter.LengthFilter(maxLength)
            editText.filters = filterArray

            // Validar solo letras
            val inputFilter = object : InputFilter {
                val regex = Regex(validar)
                override fun filter(
                    source: CharSequence?,
                    start: Int,
                    end: Int,
                    dest: Spanned?,
                    dstart: Int,
                    dend: Int
                ): CharSequence? {
                    val input = source.toString()
                    if (input.matches(regex)) {
                        return null  // Permitir ingreso de letras
                    }
                    return ""  // Bloquear ingreso de otros caracteres
                }
            }
            editText.filters = editText.filters.plus(inputFilter)
        }
    fun Habilit(mt:List<View>, tf: Boolean) {
        for (obj in mt){
            obj.isEnabled=tf
        }
    }
}