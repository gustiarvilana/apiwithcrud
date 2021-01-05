package com.example.mysubmission3

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import java.util.*

open class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateBaseContextLocale(newBase))
    }

    private fun updateBaseContextLocale(context: Context?): Context? {
//        context?.let {
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            val language = preferences.getString("bahasa", "id") ?: "id"
//            val locale = Locale(language)
//            Locale.setDefault(locale)
//            val config = it.resources.configuration
//            config?.setLocale(locale)
//            if (config != null) return it.createConfigurationContext(config)
//        }
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val language = preferences.getString("bahasa", "id") ?: "id"
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = context?.resources?.configuration
        config?.setLocale(locale)
        val asd = Locale.getDefault().displayLanguage
        Log.d("testing",asd)
        if (config != null) return context.createConfigurationContext(config)
        return context
    }
}