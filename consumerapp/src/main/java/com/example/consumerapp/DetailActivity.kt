package com.example.consumerapp

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.consumerapp.adapter.ListAdapter
import com.example.consumerapp.adapter.PagerAdapter
import com.example.consumerapp.databinding.ActivityDetailBinding
import com.example.consumerapp.db.DatabaseContract
import com.example.consumerapp.db.DatabaseContract.FavColumns.Companion.CONTENT_URI
//import com.example.consumerapp.db.FavoriteHelper
import com.example.consumerapp.entity.ModelData
import com.example.consumerapp.helper.MappingHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject


class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var favAdapter: ListAdapter
    private lateinit var uriWithUser: Uri

    companion object {
        const val INTENT_PARCELABLE = "intent_parcelable"
    }

    private var title = "Detail User"
    private var statusFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentUsers = intent.getParcelableExtra<ModelData>(INTENT_PARCELABLE)!!
        val userName = intentUsers.userName.toString()
        val url = intentUsers.url.toString()
        val image = intentUsers.image.toString()
        val id = intentUsers.id

        val pagerAdapter = PagerAdapter(
                userName,
                this,
                supportFragmentManager
        )
        binding.viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(view_pager)

        showLoading(true)
        showDetail(userName)

        favAdapter= ListAdapter()

        supportActionBar?.title = title

        loadFavAsync(userName)

        setStatusFavorite(statusFavorite)
        binding.fab.setOnClickListener {
            statusFavorite = !statusFavorite

            if (statusFavorite) {
                val values = ContentValues()
                values.put(DatabaseContract.FavColumns._ID, id)
                values.put(DatabaseContract.FavColumns.USERNAME, userName)
                values.put(DatabaseContract.FavColumns.URL, url)
                values.put(DatabaseContract.FavColumns.IMAGE, image)
                contentResolver.insert(CONTENT_URI, values)

                "Satu item berhasil ditambah".showSnackbarMessage()
                statusFavorite = true
            } else {
                uriWithUser = Uri.parse("$CONTENT_URI/$userName")
                contentResolver.delete(uriWithUser,null,null)
                "Satu item berhasil dihapus".showSnackbarMessage()
                statusFavorite = false
            }
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(favorite: Boolean) {
        if (favorite) {
            binding.fab.setImageResource(R.drawable.ic_favorite_full_24)
        } else {
            binding.fab.setImageResource(R.drawable.ic_favorite_border_24)
        }
    }

    private fun loadFavAsync(fav: String) {
        val uri = Uri.parse("$CONTENT_URI/$fav")
        val cursor = contentResolver.query(uri, null, null, null, null)
        val listfav =  MappingHelper.mapCursorToArrayList(cursor)

        if (listfav.size > 0) {
            statusFavorite = true
        }
        setStatusFavorite(statusFavorite)
        Log.d("uname =", fav)
    }

    private fun showDetail(userName: String){

        val url = "https://api.github.com/users/$userName"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","8aead0474ce747174c61c6fa0fc69d2b55c97c61")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                try {
                    val data = JSONObject(result)

                    val name = data.getString("name")
                    val location = data.getString("location")
                    val company = data.getString("company")
                    val blog = data.getString("blog")
                    val image = data.getString("avatar_url")
                    val follower = data.getInt("followers")
                    val following = data.getInt("following")

                    Glide.with(this@DetailActivity)
                        .load(image)
                        .into(binding.ivBigImage)

                    binding.tvName.text = name
                    binding.tvLocation.text = location
                    binding.tvCompany.text = company
                    binding.tvBlog.text = blog
                    binding.tvFollower.text = follower.toString()
                    binding.tvFollowing.text = following.toString()

                    showLoading(false)

                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                txError.visibility = View.VISIBLE
                txError.text = errorMessage
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarDetail.visibility = View.VISIBLE
        } else {
            progressBarDetail.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        menu.findItem(R.id.search).isVisible = false

        return true
    }

    override fun onOptionsItemSelected(v: MenuItem): Boolean {
        when(v.itemId){
            R.id.setting ->{
                val mIntent = Intent(this@DetailActivity, SettingHolderActivity::class.java)
                startActivity(mIntent)
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
            }
            R.id.mfavorite -> {
                val intent = Intent(this@DetailActivity, MyFavoriteUserActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(v)
    }

    private fun String.showSnackbarMessage() {
        Toast.makeText(this@DetailActivity, this, Toast.LENGTH_SHORT).show()
    }
}

