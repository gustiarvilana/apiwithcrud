package com.example.mysubmission3

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.adapter.ListAdapter
import com.example.mysubmission3.databinding.ActivityMainBinding
import com.example.mysubmission3.db.FavoriteHelper
import com.example.mysubmission3.entity.ModelData
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ListAdapter
    private lateinit var favoriteHelper: FavoriteHelper

    private lateinit var binding : ActivityMainBinding
    private var title = "App Search"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        mainViewModel.getUser().observe(this, Observer { listItems ->
            if (listItems != null){
                adapter.setData(listItems)
                showLoading(false)
            }
        })

        supportActionBar?.title = title

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        mainViewModel.setUserActive()
        adapter.setOnItemClickCallBack(object : ListAdapter.OnitemClikCallBack{
            override fun onItemCliked(data: ModelData) {
                showSelectedUser(data)
            }

        })
        showLoading(false)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                showLoading(true)
                mainViewModel.setUser(newText)

                adapter.setOnItemClickCallBack(object : ListAdapter.OnitemClikCallBack{
                    override fun onItemCliked(data: ModelData) {
                        showSelectedUser(data)
                    }

                })

                return true
            }

        })

        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onOptionsItemSelected(v: MenuItem): Boolean {
        when(v.itemId){
            R.id.setting ->{
                val mIntent = Intent(this@MainActivity, SettingHolderActivity::class.java)
                startActivity(mIntent)
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
            }
            R.id.mfavorite -> {
                val intent = Intent(this@MainActivity, MyFavoriteUserActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(v)
    }

//    val intent = Intent(this@DetailActivity, MyFavoriteUserActivity::class.java)
//    startActivity(intent)
//
//    adapter.setOnItemClickCallBack(object : ListAdapter.OnitemClikCallBack{
//        override fun onItemCliked(data: ModelData) {
//            showSelectedUser(data)
//        }
//
//    })

    private fun showSelectedUser(user: ModelData){
        Toast.makeText(this, "Kamu memilih ${user.userName}", Toast.LENGTH_SHORT).show()
        val intentData = Intent(this@MainActivity, DetailActivity::class.java)
        intentData.putExtra(DetailActivity.INTENT_PARCELABLE, user)

        startActivity(intentData)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }
}