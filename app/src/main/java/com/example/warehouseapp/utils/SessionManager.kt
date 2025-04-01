package com.example.warehouseapp.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.warehouseapp.data.models.User

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserSession(user: User) {
        prefs.edit().apply {
            putInt("USER_ID", user.id)
            putInt("USER_ROLE", user.role)
            putString("USER_NAME", user.userName)
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }

    fun getUserId(): Int = prefs.getInt("USER_ID", -1)
    fun getUserRole(): Int = prefs.getInt("USER_ROLE", -1)
    fun getUserName(): String? = prefs.getString("USER_NAME", null)
    fun isLoggedIn(): Boolean = prefs.getBoolean("IS_LOGGED_IN", false)

    fun clearSession() {
        prefs.edit() { clear() }
    }
}