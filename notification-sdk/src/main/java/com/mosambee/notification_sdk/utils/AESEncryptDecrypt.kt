package com.mosambee.notification_sdk.utils

import Decoder.BASE64Decoder
import Decoder.BASE64Encoder
import android.util.Log
import org.apache.commons.lang3.StringUtils
import java.security.Key
import java.security.spec.AlgorithmParameterSpec
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryptDecrypt {

    private val ENCRYPTION_KEY = "1234561231234564"
    private val ENCRYPTION_IV = "1234561231234564"

    fun getAESEncryptedString(plainText: String): String? {
        var aesEncryptedString: String? = null
        try {
            if (StringUtils.isNotEmpty(plainText)) {
                aesEncryptedString = encrypt(plainText)
            }
        } catch (e: Exception) {
            Log.d("getAESEncryptedString :", e.message!!)
        }
        return aesEncryptedString
    }

    fun getAESDecryptedString(encryptedText: String): String? {
        var decryptedText: String? = StringUtils.EMPTY
        try {
            if (StringUtils.isNotEmpty(encryptedText)) {
                decryptedText = decrypt(encryptedText)
            }
        } catch (e: Exception) {
            Log.d("getAESDecryptedString :", e.message!!)
        }
        return decryptedText
    }

    private fun encrypt(data: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, makeKey(), makeIv())
            val encVal = cipher.doFinal(data.toByteArray())
            return BASE64Encoder().encode(encVal)
        } catch (e: Exception) {
            Log.d("encrypt :", e.message!!)
        }
        return StringUtils.EMPTY
    }

    private fun decrypt(encryptedData: String): String? {
        try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, makeKey(), makeIv())
            val decordedValue = BASE64Decoder().decodeBuffer(encryptedData)
            val decValue = cipher.doFinal(decordedValue)
            val aaaa = String(decValue)
            return String(decValue)
        } catch (e: Exception) {
            Log.d("decrypt :", e.message!!)
        }
        return StringUtils.EMPTY
    }

    private fun makeIv(): AlgorithmParameterSpec? {
        try {
            Log.d(
                "makeIv of AESEncrypt : ",
                IvParameterSpec(ENCRYPTION_IV.toByteArray(charset("UTF-8"))).toString()
            )
            return IvParameterSpec(ENCRYPTION_IV.toByteArray(charset("UTF-8")))
        } catch (e: Exception) {
            Log.d("makeIv of AESEncrypt : ", e.message!!)
        }
        return null
    }

    private fun makeKey(): Key? {
        try {
            val key = ENCRYPTION_KEY.toByteArray(charset("UTF-8"))
            return SecretKeySpec(key, "AES")
        } catch (e: Exception) {
            Log.d("makeKey of AESEncrypt :", e.message!!)
        }
        return null
    }


    fun byteToHex(byteData: ByteArray): String? {
        val stringBuilder = StringBuilder(byteData.size * 2)
        for (hashByte in byteData) {
            stringBuilder.append(String.format("%02x", hashByte))
        }
        return stringBuilder.toString().uppercase(Locale.getDefault())
    }

    fun byteArrayToHex(a: ByteArray): String? {
        val sb = StringBuilder(a.size * 2)
        for (b in a) sb.append(String.format("%02x", b))
        return sb.toString()
    }
}