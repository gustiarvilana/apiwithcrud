package com.example.mysubmission3

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysubmission3.entity.ModelData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel: ViewModel() {
    val listUsers = MutableLiveData<ArrayList<ModelData>>()

    fun setUser(userName: String){
        val listUser = ArrayList<ModelData>()

        val url = "https://api.github.com/search/users?q=$userName"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","93dc24a00c12e8e45af2037623c3d1eba8589397")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                try {
                    // parsing
                    val jsonObject = JSONObject(result)
                    val list = jsonObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val item = list.getJSONObject(i)
                        val user = ModelData()
                        user.id = item.getString("id")
                        user.image = item.getString("avatar_url")
                        user.userName = item.getString("login")
                        user.url = item.getString("html_url")

                        listUser.add(user)
                    }
                    Log.d("data", listUser.toString())

                    //set data ke adapter
                    listUsers.postValue(listUser)


                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {

                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun setUserActive(){
        val listUser = ArrayList<ModelData>()

        val url = "https://api.github.com/users"

        val client = AsyncHttpClient()
        client.addHeader("Authorization","a0a1a0d6b3013eac64575565c22f8e11cfad9a01")
        client.addHeader("User-Agent","request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val result = String(responseBody)
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
                    listUsers.postValue(listUser)


                }catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {

                Log.d("onFailure", error.message.toString())
            }
        })
    }

    fun getUser(): LiveData<ArrayList<ModelData>> {
        return listUsers
    }

}