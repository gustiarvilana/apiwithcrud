package com.example.mysubmission3.preferences

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.example.mysubmission3.AlarmReceiver
import com.example.mysubmission3.MainActivity
import com.example.mysubmission3.R


class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var LANG: String

    private lateinit var alarmPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver

    private lateinit var langPreference: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        retainInstance = true
        init()
        setSummaries()
        alarmReceiver = AlarmReceiver()
    }

    private fun init(){
        ALARM = resources.getString(R.string.key_alarm)
        LANG = resources.getString(R.string.key_lang)

        alarmPreference = findPreference<SwitchPreference>(ALARM) as SwitchPreference
        langPreference = findPreference<ListPreference>(LANG) as ListPreference
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
        if (key == ALARM){
            if (alarmPreference.isChecked) {
                Toast.makeText(activity, "Toast true", Toast.LENGTH_SHORT).show()
                setAllarm()
            }else{
                cancelAllarm()
                Toast.makeText(activity, "Toast false", Toast.LENGTH_SHORT).show()
            }
        }

        if (key == LANG){
            val intent = Intent(context, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
        }
    }

    private fun setAllarm(){
        val repeatTime = "09:00"
        val repeatMessage = "Temukan faforite user pilihanmu!!"
        alarmReceiver.setRepeatingAlarm(context, AlarmReceiver.TYPE_REPEATING,
            repeatTime, repeatMessage)
    }

    private fun cancelAllarm(){
        alarmReceiver.cancelAlarm(context)
    }

    private fun setSummaries(){
        val sh = preferenceManager.sharedPreferences
        alarmPreference.isChecked = sh.getBoolean(ALARM, false)
    }
}