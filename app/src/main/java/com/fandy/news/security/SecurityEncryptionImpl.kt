/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fandy.news.security

import java.lang.Exception
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Singleton

/**
 * Data manager class that handles data manipulation between the database and the UI.
 */
@Singleton
class SecurityEncryptionImpl : SecurityEncryption {

    private lateinit var keyGenerator: KeyGenerator
    private lateinit var secretKey: SecretKey
    private var IV = ByteArray(16)
    private var random: SecureRandom? = null

    constructor() {
        try {
            keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(256)
            secretKey = keyGenerator.generateKey()
        } catch (e: Exception) {
        }

        random = SecureRandom()
        random!!.nextBytes(IV)
    }

    @Throws(Exception::class)
    override fun encrypt(plaintext: String): ByteArray {
        return encrypt(plaintext.toByteArray(Charsets.UTF_8), secretKey, IV)
    }

    @Throws(Exception::class)
    private fun encrypt(
        plaintext: ByteArray,
        key: SecretKey,
        IV: ByteArray
    ): ByteArray {
        val cipher: Cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(key.getEncoded(), "AES")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        return cipher.doFinal(plaintext)
    }

    override fun decrypt(cipherText: ByteArray): String? {
        try {
            return decrypt(cipherText, secretKey, IV)
        } catch (e: Exception) {
        }
        return null
    }

    @Throws(Exception::class)
    private fun decrypt(cipherText: ByteArray, key: SecretKey, IV: ByteArray): String? {
        val cipher: Cipher = Cipher.getInstance("AES")
        val keySpec = SecretKeySpec(key.getEncoded(), "AES")
        val ivSpec = IvParameterSpec(IV)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        val decryptedText: ByteArray = cipher.doFinal(cipherText)
        return String(decryptedText)
    }
}
