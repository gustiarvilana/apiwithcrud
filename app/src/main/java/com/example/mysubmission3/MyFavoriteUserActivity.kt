package com.example.mysubmission3

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.adapter.ListAdapter
import com.example.mysubmission3.databinding.ActivityMyFavoriteUserBinding
import com.example.mysubmission3.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.example.mysubmission3.db.FavoriteHelper
import com.example.mysubmission3.entity.ModelData
import com.example.mysubmission3.helper.MappingHelper
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MyFavoriteUserActivity : BaseActivity() {

    private lateinit var binding: ActivityMyFavoriteUserBinding
    private lateinit var favAdapter: ListAdapter
    private lateinit var favHelper: FavoriteHelper

    companion object {
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

        //
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        loadFavAsync()

        if (savedInstanceState == null) {
            loadFavAsync()
        } else {
            savedInstanceState.getParcelableArrayList<ModelData>(EXTRA_STATE)?.also { favAdapter.listData = it }
        }
//
//        if (savedInstanceState == null) {
//            ()
//        }else{
//            val list = savedInstanceState.getParcelableArrayList<ModelData>(EXTRA_STATE)
//            if (list != null){
//                favAdapter.listData = list
//            }
//        }

        favAdapter.setOnItemClickCallBack(object : ListAdapter.OnitemClikCallBack {
            override fun onItemCliked(data: ModelData) {
                showSelectedUser(data)
            }

        })

        Log.d("test URI = ", CONTENT_URI.toString())
    }

    private fun loadFavAsync() {

        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferredNotes.await()
            binding.progressBar.visibility = View.INVISIBLE
            if (user.size > 0) {
                favAdapter.setData(user)

            } else {
                favAdapter.listData = ArrayList()
                showSnackBarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favAdapter.listData)
    }

    private fun showSelectedUser(user: ModelData) {
        Toast.makeText(this, "Kamu memilih ${user.userName}", Toast.LENGTH_SHORT).show()
        val intentData = Intent(this@MyFavoriteUserActivity, DetailActivity::class.java)
        intentData.putExtra(DetailActivity.INTENT_PARCELABLE, user)
        startActivity(intentData)
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.settingHolderFav, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }

}