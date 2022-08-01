package com.example.fileexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.example.fileexample.databinding.ActivityMainBinding
import com.example.fileexample.fragments.FileFragment
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //External my app folder path
        val file=getExternalFilesDir(null)!!
        val path = file.path

        //Folder download Path
        val fileDownload=getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)



        val transaction=supportFragmentManager.beginTransaction()
        transaction.add(R.id.LayoutMain,FileFragment(path))
        transaction.commit()

    }
}