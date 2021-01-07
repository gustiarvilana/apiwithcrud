package com.example.consumerapp

import android.os.Bundle
import com.example.consumerapp.preferences.MyPreferenceFragment

class SettingHolderActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_holder)
        supportFragmentManager.beginTransaction().add(R.id.setting_holder_main, MyPreferenceFragment()).commit()
    }
}