package com.dicoding.asclepius.view.result

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.view.ViewModelFactory

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var currentImageUri: Uri? = null
    private var isDataInserted = false

    private val historyViewModel: HistoryInsViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        savedInstanceState?.let {
            currentImageUri = it.getParcelable("Uri")
            isDataInserted = it.getBoolean("isDataInserted", false)
        } ?: run {

            currentImageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        }

        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

        val result = intent.getStringExtra(EXTRA_RESULT)
        binding.resultText.text = result

        if (!isDataInserted && !result.isNullOrEmpty()) {
            val historyEntity = HistoryEntity(
                imageUri = currentImageUri.toString(),
                result = result,
                timestamp = System.currentTimeMillis()
            )
            historyViewModel.insert(historyEntity)
            Toast.makeText(this, "Data telah dimasukkan ke riwayat", Toast.LENGTH_LONG).show()
            isDataInserted = true
        } else if (result.isNullOrEmpty()) {
            Toast.makeText(this, "Gagal memasukkan data ke riwayat", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        currentImageUri?.let {
            outState.putParcelable("Uri", it)
        }
        outState.putBoolean("isDataInserted", isDataInserted)
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}