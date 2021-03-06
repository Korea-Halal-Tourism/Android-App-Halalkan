package com.feylabs.halalkan.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.feylabs.halalkan.R
import com.feylabs.halalkan.databinding.ItemGridRsBinding
import com.feylabs.halalkan.view.home.ListPostsAdapter.PostViewHolder

class ListPostsAdapter : RecyclerView.Adapter<PostViewHolder>() {

    val data = mutableListOf<ShowedPosts?>()
    lateinit var adapterInterface: PostItemInterface

    fun setWithNewData(data: MutableList<ShowedPosts?>) {
        this.data.clear()
        this.data.addAll(data)
    }

    fun setupAdapterInterface(obj: PostItemInterface) {
        this.adapterInterface = obj
    }

    inner class PostViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var binding: ItemGridRsBinding = ItemGridRsBinding.bind(itemView)

        fun onBInd(model: ShowedPosts?) {
            val mContext = binding.root.context

            binding.base.animation = AnimationUtils.loadAnimation(
                mContext,
                R.anim.fade_transition_animation
            )

            binding.root.setOnClickListener {
                adapterInterface.onclick(model)
            }

            binding.tvPost.text = model?.body.toString()

            binding.tvTitle.text = model?.title.toString()
            binding.tvnumber.text = (adapterPosition + 1).toString()

            binding.tvAuthor.text = model?.userName.toString()
            binding.tvCompany.text = model?.userCompanyName.toString()


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid_rs, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBInd(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface PostItemInterface {
        fun onclick(model: ShowedPosts?)
        fun onUserClick(model: ShowedPosts?)
    }
}