package com.dicoding.asclepius.view.history

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.view.ViewModelFactory
import com.dicoding.asclepius.view.result.HistoryInsViewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private val viewInsModel: HistoryInsViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private val viewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Setup RecyclerView and ViewModel observers
        setRView()
        setViewModel()
    }

    private fun setRView() {
        historyAdapter = HistoryAdapter(viewInsModel.repository)
        binding.rvHistory.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = this@HistoryActivity.historyAdapter
        }
    }

    private fun setViewModel() {
        // Show loading initially
        showLoading(true)

        viewModel.history.observe(this) { history ->
            Log.d("FavoriteFragment", "Favorite events updated: $history")
            val items = history.map { history ->
                HistoryEntity(
                    id = history.id,
                    imageUri = history.imageUri,
                    result = history.result,
                    timestamp = history.timestamp
                )
            }

            historyAdapter.submitList(items)

            showLoading(false)
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}