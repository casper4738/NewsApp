package com.fandy.news.security

interface SecurityEncryption {

    fun encrypt(plaintext: String): ByteArray

    fun decrypt(cipherText: ByteArray): String?

}