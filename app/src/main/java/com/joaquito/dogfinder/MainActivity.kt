package com.joaquito.dogfinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.LinearLayoutManager
import com.joaquito.dogfinder.adapter.DogAdapter
import com.joaquito.dogfinder.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnQueryTextListener{

    private lateinit var adapter: DogAdapter
    private lateinit var binding: ActivityMainBinding
    private val dogImages = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svDogs.setOnQueryTextListener(this)
        binding.svDogs2.setOnQueryTextListener(this)
        initRecyclerView()
        initListeners()
    }

    private fun initListeners() {
        binding.back.setOnClickListener { goBack() }
    }

    private fun goBack() {
        binding.svDogs.isVisible = true
        binding.svDogs2.isVisible = false
        binding.tvTitle.isVisible = true
        binding.back.isVisible = false
        binding.rvDogs. isVisible = false
    }

    private fun initRecyclerView() {
        adapter = DogAdapter(dogImages)
        binding.rvDogs.layoutManager = LinearLayoutManager(this)
        binding.rvDogs.adapter = adapter
    }

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query:String){
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(DogApiClient::class.java).getDogListByBreeds("$query/images")
            val puppies = call.body()
            runOnUiThread {
                if (call.isSuccessful){
                    val images: List<String>? = puppies?.images ?: emptyList()
                    dogImages.clear()
                    dogImages.addAll(images as Collection<String>)
                    adapter.notifyDataSetChanged()
                }else{
                    showError()
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(this,"paso algo malo", Toast.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchByName(query.lowercase())
            binding.svDogs.isVisible = false
            binding.svDogs2.isVisible = true
            binding.tvTitle.isVisible = false
            binding.back.isVisible = true
            binding.rvDogs.isVisible = true
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

}