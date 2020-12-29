package com.example.mysubmission3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysubmission3.adapter.ListAdapter
import com.example.mysubmission3.databinding.ActivityMyFavoriteUserBinding
import com.example.mysubmission3.entity.ModelData

class MyFavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyFavoriteUserBinding
    private lateinit var favAdapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                DetailActivity.REQUEST_ADD -> if (resultCode == DetailActivity.RESULT_ADD) {
                    val user =
                        data.getParcelableExtra<ModelData>(DetailActivity.INTENT_PARCELABLE)!!
                    Log.d("data note= ", user.toString())

                    favAdapter.addItem(user)
                    binding.rvFavorite.smoothScrollToPosition(favAdapter.itemCount - 1)

                    Toast.makeText(this@MyFavoriteUserActivity, "Satu tem berhasil dtambahkan", Toast.LENGTH_SHORT).show()
                }
                DetailActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        DetailActivity.RESULT_UPDATE -> {
                            val note =
                                data.getParcelableExtra<ModelData>(DetailActivity.INTENT_PARCELABLE) !!
                            val position =
                                data.getIntExtra(DetailActivity.EXTRA_POSITION, 0)

                            favAdapter.updateItem(position, note)
                            binding.rvFavorite.smoothScrollToPosition(position)

                            Toast.makeText(this@MyFavoriteUserActivity, "Satu item berhasil diubah", Toast.LENGTH_SHORT).show()

                        }
                        DetailActivity.RESULT_DELETE -> {
                            val position =
                                data.getIntExtra(DetailActivity.EXTRA_POSITION, 0)
                            favAdapter.removeItem(position)
                            Toast.makeText(this@MyFavoriteUserActivity, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }
    }


}