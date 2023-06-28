package com.example.storyappdicoding.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.core.util.Pair
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.storyappdicoding.Constant.KEY_STORY
import com.example.storyappdicoding.Constant.createProgress
import com.example.storyappdicoding.databinding.ItemStoriesBinding
import com.example.storyappdicoding.R
import com.example.storyappdicoding.api.models.Story
import java.util.*

class MainAdapter :
    PagingDataAdapter<Story, MainAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        data?.let {
            holder.bind(it)
        }
    }

    inner class ViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Story) {
            val context = itemView.context

            val firstLetter = data.name.first().toString()
            val date = context.getString(
                R.string.created_at,
                data.createdAt?.split("T")?.get(0) ?: ""
            )

            binding.apply {
                tvname.text = data.name
                tvcreated.text = date
                tvfirstletter.text = firstLetter

                val rnd = Random()
                val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                cardView.setCardBackgroundColor(color)

                Glide.with(context)
                    .load(data.photoUrl)
                    .placeholder(createProgressDrawable(context))
                    .error(android.R.color.darker_gray)
                    .into(imgStories)

                itemView.setOnClickListener {
                    Intent(context, DetailActivity::class.java).also { intent ->
                        intent.putExtra(KEY_STORY, data)

                        val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            context as Activity,
                            Pair(tvname, context.getString(R.string.image)),
                            Pair(tvcreated, context.getString(R.string.date))
                        )
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    private fun createProgressDrawable(context: Context): CircularProgressDrawable {
        val progressDrawable = CircularProgressDrawable(context)
        progressDrawable.strokeWidth = 10f
        progressDrawable.centerRadius = 50f
        progressDrawable.start()
        return progressDrawable
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Story> =
            object : DiffUtil.ItemCallback<Story>() {
                override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
