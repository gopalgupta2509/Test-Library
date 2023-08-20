package com.mosambee.notification_sdk.utils

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class HMACUtil {

    fun calculateHMAC(signatureKey: String, textToHash: String): String {
        val signatureKeyInByteArray = hexStringToByteArray(signatureKey)
        val secretKeySpec = SecretKeySpec(signatureKeyInByteArray, "HmacSHA512")
        val mac = Mac.getInstance("HmacSHA512")
        mac.init(secretKeySpec)
        return Base64().encodeToString(mac.doFinal(textToHash.toByteArray()), false)
    }

    private fun hexStringToByteArray(str: String): ByteArray {
        val len = str.length
        val data = ByteArray(len / 2)
        for (i in 0 until len - 1 step 2) {
            data[i / 2] =
                ((Character.digit(str[i], 16) shl 4) + Character.digit(str[i + 1], 16)).toByte()
        }
        return data
    }

}