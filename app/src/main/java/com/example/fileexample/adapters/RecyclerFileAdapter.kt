package com.example.fileexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fileexample.R
import com.example.fileexample.databinding.ItemFileLinearBinding
import java.io.File
import java.net.URLConnection

class RecyclerFileAdapter(private val fileList:ArrayList<File>,val fileEvent: FileEvent):RecyclerView.Adapter<RecyclerFileAdapter.FileViewHolder>() {
    lateinit var binding: ItemFileLinearBinding
    inner class FileViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bindView(file : File){
            var fileType=""
            binding.txtFolderName.text=file.name
            when{
                file.isDirectory->{
                    binding.imgFolder.setImageResource(R.drawable.ic_folder)
                }
                //site for highlighting file format => http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174
                isImage(file.path)->{
                    binding.imgFolder.setImageResource(R.drawable.ic_image)
                    fileType="image/*"
                }
                isVideo(file.path)->{
                    binding.imgFolder.setImageResource(R.drawable.ic_video)
                    fileType="video/*"
                }
                isZip(file.name)->{
                    binding.imgFolder.setImageResource(R.drawable.ic_zip)
                    fileType="application/zip"
                }
                else->{
                    binding.imgFolder.setImageResource(R.drawable.ic_file)
                    fileType="text/plain"
                }
            }
            itemView.setOnClickListener {
                if (file.isDirectory){
                    fileEvent.onFolderClick(file.path)
                }else{
                    fileEvent.onFileClick(file,fileType)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        binding= ItemFileLinearBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FileViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        holder.bindView(fileList[position])
    }

    override fun getItemCount(): Int {
        return fileList.size
    }

    fun isImage(path:String):Boolean{
        val mimeType:String=URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("image")
    }
    fun isVideo(path:String):Boolean{
        val mimeType:String=URLConnection.guessContentTypeFromName(path)
        return mimeType.startsWith("video")
    }

    fun isZip(name:String):Boolean{
        return name.contains(".zip")||name.contains(".rar")
    }

    interface FileEvent{
        fun onFileClick(file : File,type:String)
        fun onFolderClick(path: String)
    }
}