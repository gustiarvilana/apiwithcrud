package com.example.mysubmission3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmission3.adapter.ListAdapter
import com.example.mysubmission3.entity.ModelData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray

class FollowingFragment : Fragment() {

    private lateinit var adapter: ListAdapter

    companion object{
        const val USERNAME = "username"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        rv_Following.layoutManager = LinearLayoutManager(activity)
        rv_Following.adapter = adapter

        val userName = arguments?.getString(USERNAME)
        userName?.let { setUser(it) }

        adapter.setOnItemClickCallBack(object : ListAdapter.OnitemClikCallBack{
            override fun onItemCliked(data: ModelData) {
                showSelectedUser(data)
            }

        })
    }

    fun setUser(username: String){
        val listUser = ArrayList<ModelData>()

        val url = "https://api.github.com/users/$username/following"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","8aead0474ce747174c61c6fa0fc69d2b55c97c61")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d("following", result)
                try {
                    // parsing
                    val jsonrray = JSONArray(result)

                    for (i in 0 until jsonrray.length()) {
                        val item = jsonrray.getJSONObject(i)
                        val user = ModelData()
                        user.image = item.getString("avatar_url")
                        user.userName = item.getString("login")
                        user.url = item.getString("html_url")

                        listUser.add(user)
                    }
                    Log.d("data", listUser.toString())

                    //set data ke adapter
                    adapter.setData(listUser)


                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {

                Log.d("onFailure", error.message.toString())
            }
        })
    }

    private fun showSelectedUser(user: ModelData){
        Toast.makeText(activity, "Kamu memilih ${user.userName}", Toast.LENGTH_SHORT).show()
        val intentData = Intent(activity, DetailActivity::class.java)
        intentData.putExtra(DetailActivity.INTENT_PARCELABLE, user)

        startActivity(intentData)
    }
}