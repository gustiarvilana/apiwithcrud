package com.example.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ListUserViewBinding
import com.example.consumerapp.entity.ModelData

class ListAdapter: RecyclerView.Adapter<ListAdapter.ListViewolde>() {

    var listData = ArrayList<ModelData>()

    fun setData(items: ArrayList<ModelData>) {
        listData.clear()
        listData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewolde {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_user_view, parent, false)
        return ListViewolde(view)
    }

    override fun getItemCount(): Int = listData.size

    override fun onBindViewHolder(holder: ListViewolde, position: Int) {
        holder.bind(listData[position])
    }

    inner class ListViewolde(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListUserViewBinding.bind(itemView)
        fun bind(listItems: ModelData){
            with(itemView){
                Glide.with(context)
                    .load(listItems.image)
                    .apply(RequestOptions().override(65,65))
                    .into(binding.ivImage)

                binding.tvName.text = listItems.userName
                binding.tvDesc.text = listItems.url

                itemView.setOnClickListener { onitemClikCallBack?.onItemCliked(listItems) }
            }
        }
    }

    interface OnitemClikCallBack{
        fun onItemCliked(data: ModelData)
    }

    fun addItem (user:ModelData){
        this.listData.add(user)
        notifyItemInserted(this.listData.size - 1)
    }
    fun updateItem (position: Int, user: ModelData){
        this.listData[position] = user
        notifyItemChanged(position, user)
    }
    fun removeItem (position: Int){
        this.listData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position, this.listData.size)
    }

    private var onitemClikCallBack: OnitemClikCallBack? = null

    fun setOnItemClickCallBack(onitemClikCallBack: OnitemClikCallBack){
        this.onitemClikCallBack = onitemClikCallBack
    }

}