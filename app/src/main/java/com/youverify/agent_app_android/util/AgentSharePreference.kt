package com.youverify.agent_app_android.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AgentSharePreference @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val AGENT_ID_KEY = "AGENT_ID_KEY"
        private const val TOKEN_KEY = "TOKEN"
        private const val FCM_TOKEN_SENT = "FCM_TOKEN_SENT"
    }

    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun setString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
            apply()
        }
    }

    var agentId: String
        get() = sharedPreferences.getString(AGENT_ID_KEY, "") ?: ""
        set(value) {
            sharedPreferences.edit().putString(AGENT_ID_KEY, value).apply()
        }

    var token: String
        get() = sharedPreferences.getString(TOKEN_KEY, "") ?: ""
        set(value) {
            sharedPreferences.edit().putString(TOKEN_KEY, value).apply()
        }

    var agentVisiblityStatus: String
        get() = sharedPreferences.getString(SharedPrefKeys.AGENT_VISIBLITY_STATE, AgentStatus.ONINE) ?: AgentStatus.ONINE
        set(value) {
            sharedPreferences.edit().putString(SharedPrefKeys.AGENT_VISIBLITY_STATE, value).apply()
        }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
            apply()
        }
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value)
            apply()
        }
    }

    fun getString(key: String, defaultString: String? = null) =
        sharedPreferences.getString(key, defaultString) ?: ""

    fun getBoolean(key: String, defaultBoolean: Boolean = false) =
        sharedPreferences.getBoolean(key, defaultBoolean)

    fun getInt(key: String, defaultInt: Int = 0) = sharedPreferences.getInt(key, defaultInt)

    fun clear(key: String) {
        sharedPreferences.edit().remove(key).commit()
    }

    var fcmTokenSent: Boolean
     get() = sharedPreferences.getBoolean(FCM_TOKEN_SENT, false) ?: false
     set(value) {
         sharedPreferences.edit {
             putBoolean(FCM_TOKEN_SENT, value)
             apply()
         }
     }
}