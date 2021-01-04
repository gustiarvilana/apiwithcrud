package com.example.mysubmission3.preferences

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.mysubmission3.R

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String

    private lateinit var alarmPreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun init(){
        ALARM = resources.getString(R.string.key_alarm)

        alarmPreference = findPreference<SwitchPreference>(ALARM) as SwitchPreference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)

    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (alarmPreference.isChecked) {
            Toast.makeText(activity, "Toast true", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(activity, "Toast false", Toast.LENGTH_SHORT).show()

        }
    }

    private fun setSummaries(){
        val sh = preferenceManager.sharedPreferences
        alarmPreference.isChecked = sh.getBoolean(ALARM, false)
    }
}