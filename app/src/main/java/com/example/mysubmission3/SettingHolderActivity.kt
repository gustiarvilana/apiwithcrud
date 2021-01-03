package com.example.mysubmission3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mysubmission3.preferences.MyPreferenceFragment

class SettingHolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_holder)
        supportFragmentManager.beginTransaction().add(R.id.setting_holder_main, MyPreferenceFragment()).commit()

    }
}