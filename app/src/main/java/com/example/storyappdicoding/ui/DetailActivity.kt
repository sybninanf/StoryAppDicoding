package com.example.storyappdicoding.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.storyappdicoding.Constant.KEY_STORY
import com.example.storyappdicoding.Constant.createProgress
import com.example.storyappdicoding.R
import com.example.storyappdicoding.api.models.Story
import com.example.storyappdicoding.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var story: Story? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        story = intent.getParcelableExtra<Story>(KEY_STORY) as Story

        playAnimation()
        setView()
        supportActionBar?.title = getString(R.string.detail_story)

    }

    private fun playAnimation() {
        val view = ObjectAnimator.ofFloat(binding.view, View.ALPHA, 1f).setDuration(800)
        val description = ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(view,description)
            start()
        }
    }

    private fun setView() {
        binding.apply {
            val tanggal = story?.createdAt?.split("T")?.get(0)
            tvname.text = story?.name
            tvCreatedAt.text = getString(R.string.created_at, tanggal)
            tvDescription.text = story?.description
            Glide.with(this@DetailActivity)
                .load(story?.photoUrl)
                .placeholder(this@DetailActivity.createProgress())
                .error(android.R.color.darker_gray)
                .into(imgStory)
        }
    }
}