package com.example.fileexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fileexample.databinding.ActivityMainBinding
import com.example.fileexample.fragments.FileFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.LayoutMain,FileFragment())
        transaction.commit()
    }
}