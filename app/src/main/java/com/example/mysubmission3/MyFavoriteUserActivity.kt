package com.example.mysubmission3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mysubmission3.databinding.ActivityMyFavoriteUserBinding

class MyFavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavoriteUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}