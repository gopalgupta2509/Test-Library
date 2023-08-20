package com.mosambee.notification_sdk.utils

import java.util.Arrays

class Base64 {

    private val CA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray()
    private val IA = IntArray(256)

    init {
        Arrays.fill(IA, -1)
        for (i in CA.indices) {
            IA[CA[i].code] = i
        }
        IA['='.code] = 0
    }


    fun encodeToString(sArr: ByteArray, lineSep: Boolean): String {
        //return getEncoder().encodeToString(sArr)

        //return new String(encodeToChar(sArr, lineSep));
        // Reuse char[] since we can't create a String incrementally anyway and StringBuffer/Builder would be slower.
        return String(encodeToChar(sArr, lineSep))


    }

    fun encodeToChar(sArr: ByteArray?, lineSep: Boolean): CharArray {
        // Check special case
        val sLen = sArr?.size ?: 0
        if (sLen == 0) return CharArray(0)
        val eLen = sLen / 3 * 3 // Length of even 24-bits.
        val cCnt = (sLen - 1) / 3 + 1 shl 2 // Returned character count
        val dLen = cCnt + if (lineSep) (cCnt - 1) / 76 shl 1 else 0 // Length of returned array
        val dArr = CharArray(dLen)

        // Encode even 24-bits
        var s = 0
        var d = 0
        var cc = 0
        while (s < eLen) {

            // Copy next three bytes into lower 24 bits of int, paying attension to sign.
            val i =
                sArr!![s++].toInt() and 0xff shl 16 or (sArr[s++].toInt() and 0xff shl 8) or (sArr[s++].toInt() and 0xff)

            // Encode the int into four chars
            dArr[d++] = CA[i ushr 18 and 0x3f]
            dArr[d++] = CA[i ushr 12 and 0x3f]
            dArr[d++] = CA[i ushr 6 and 0x3f]
            dArr[d++] = CA[i and 0x3f]

            // Add optional line separator
            if (lineSep && ++cc == 19 && d < dLen - 2) {
                dArr[d++] = '\r'
                dArr[d++] = '\n'
                cc = 0
            }
        }

        // Pad and encode last bits if source isn't even 24 bits.
        val left = sLen - eLen // 0 - 2.
        if (left > 0) {
            // Prepare the int
            val i =
                sArr!![eLen].toInt() and 0xff shl 10 or if (left == 2) sArr[sLen - 1].toInt() and 0xff shl 2 else 0

            // Set last four chars
            dArr[dLen - 4] = CA[i shr 12]
            dArr[dLen - 3] = CA[i ushr 6 and 0x3f]
            dArr[dLen - 2] = if (left == 2) CA[i and 0x3f] else '='
            dArr[dLen - 1] = '='
        }
        return dArr
    }

}