package com.example.storyappdicoding.ui

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappdicoding.Constant.TAG
import com.example.storyappdicoding.Constant.tokenBearer
import com.example.storyappdicoding.R
import com.example.storyappdicoding.api.models.User
import com.example.storyappdicoding.autentikasi.login.LoginActivity
import com.example.storyappdicoding.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  var user: User? = null
    private lateinit var mAdapter: MainAdapter
    private val viewModel: MainViewModel by viewModels()

    companion object {
        const val INSERT_RESULT = 200
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getUser {
            user = it
        }
        binding.fabTambah.setOnClickListener {
            Intent(this@MainActivity, AddStoryActivity::class.java).also {
                launcherInsertStory.launch(it)
            }
        }

        setupRecyclerData()
        getStories()
        showLoading()
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getStories() {
        val token = user?.tokenBearer.toString()
        viewModel.getStories(token) {
            Log.d(TAG, "onCreate: $it")
            mAdapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading() {
        viewModel.isLoading.observe(this@MainActivity){
            binding.progressBar.isVisible = it
        }
    }

    private fun setupRecyclerData() {
        mAdapter = MainAdapter()
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = mAdapter.withLoadStateFooter(
                footer = LoadingStateAdap{
                    mAdapter.retry()
                }
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout -> {
                user?.let { viewModel.logout(it) }
                Intent(this@MainActivity, LoginActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(it)
                }
            }
        }
        return true
    }

    private fun reGetStory() {
        getStories()
        setupRecyclerData()
    }

    private val launcherInsertStory = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == INSERT_RESULT){
            reGetStory()
        }
    }
}