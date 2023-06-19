package com.example.vetsoft.Cryptation

import android.os.Build
import androidx.annotation.RequiresApi
import java.nio.charset.StandardCharsets
import java.security.Key
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.SecretKeySpec

class Crypto {
    @RequiresApi(Build.VERSION_CODES.O)
    fun encrypt(data: String, key: String): String {
        val cipher = Cipher.getInstance("Blowfish")
        val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "Blowfish")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedBytes = cipher.doFinal(data.toByteArray())
        val base64Encoded = Base64.getEncoder().encodeToString(encryptedBytes)
        return base64Encoded
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun decrypt(encryptedData: String, key: String): String {
        val cipher = Cipher.getInstance("Blowfish")
        val secretKey: SecretKey = SecretKeySpec(key.toByteArray(), "Blowfish")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val encryptedBytes = Base64.getDecoder().decode(encryptedData)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}