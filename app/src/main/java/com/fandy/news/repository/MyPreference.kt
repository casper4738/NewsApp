package com.fandy.news.repository

import android.content.Context
import com.fandy.news.security.SecurityEncryption
import com.fandy.news.util.PREF_MODE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPreference @Inject constructor(
    @ApplicationContext context: Context,
    private val securityEncryption: SecurityEncryption
) {

    private val prefs = context.getSharedPreferences(PREF_MODE_KEY, Context.MODE_PRIVATE)

    fun getStoredString(key: String): String {
        var str : String?
        try {
            str = prefs.getString(key, "");
        } catch (e: java.lang.ClassCastException) {
            try {
                str = prefs.getBoolean(key, false).toString();
            } catch (e: Exception) {
                str = ""
            }
        }
        return str?.let {
            val decryptValue = securityEncryption.decrypt(decodeString(str))
            decryptValue
        } ?: ""
    }

    fun getStoredBoolean(key: String): Boolean {
        return getStoredString(key).toBoolean()
    }

    fun setStored(key: String, value: String) {
        prefs.edit().putString(key, encodeString(value)).apply()
    }

    fun setStored(key: String, value: Boolean) {
        setStored(key, encodeString("${value}"))
    }

    private fun encodeString(value: String): String {
        val encryptValue = securityEncryption.encrypt(value)
        return android.util.Base64.encodeToString(encryptValue, android.util.Base64.DEFAULT)
    }

    private fun decodeString(value: String): ByteArray {
        return android.util.Base64.decode(value, android.util.Base64.DEFAULT)
    }

}