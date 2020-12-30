package com.example.mysubmission3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.adapter.ListAdapter
import com.example.mysubmission3.databinding.ActivityMyFavoriteUserBinding
import com.example.mysubmission3.db.FavoriteHelper
import com.example.mysubmission3.entity.ModelData
import com.example.mysubmission3.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyFavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavoriteUserBinding
    private lateinit var favAdapter: ListAdapter
    private lateinit var favHelper: FavoriteHelper

    companion object{
        private const val EXTRA_STATE = "extra_state"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.setHasFixedSize(true)
        favAdapter = ListAdapter()
        binding.rvFavorite.adapter = favAdapter

        favHelper = FavoriteHelper.getInstance(applicationContext)
        favHelper.open()

        loadFavAsync()
        if (savedInstanceState == null) {
            loadFavAsync()
        }else{
            val list = savedInstanceState.getParcelableArrayList<ModelData>(EXTRA_STATE)
            if (list != null){
                favAdapter.listData = list
            }
        }
    }

    private fun loadFavAsync() {
//        val user = favHelper.queryAll()
//        MappingHelper.mapCursorToArrayList(user)
//        Log.d("val user = ", user.toString())

        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferredNotes.await()
            Log.d("data user = ", user.toString())
            if (user.size > 0) {
                favAdapter.setData(user)

            } else {
                favAdapter.listData = ArrayList()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favAdapter.listData)
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

}