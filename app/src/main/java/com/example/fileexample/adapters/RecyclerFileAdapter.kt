package com.example.fileexample.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fileexample.R
import com.example.fileexample.databinding.ItemFileGridBinding
import com.example.fileexample.databinding.ItemFileLinearBinding
import java.io.File
import java.net.URLConnection

class RecyclerFileAdapter(private val fileList:ArrayList<File>,val fileEvent: FileEvent):RecyclerView.Adapter<RecyclerFileAdapter.FileViewHolder>() {
    var numberForListForm=0
    inner class FileViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        private val txtFile: TextView =itemView.findViewById(R.id.txt_folder_name)
        private val imgFile: ImageView =itemView.findViewById(R.id.img_folder)
        fun bindView(file : File){
            var fileType=""
            txtFile.text=file.name
            when{
                file.isDirectory->{
                    imgFile.setImageResource(R.drawable.ic_folder)
                }
                //site for highlighting file format => http://androidxref.com/4.4.4_r1/xref/frameworks/base/media/java/android/media/MediaFile.java#174
                isImage(file.path)->{
                    imgFile.setImageResource(R.drawable.ic_image)
                    fileType="image/*"
                }
                isVideo(file.path)->{
                    imgFile.setImageResource(R.drawable.ic_video)
                    fileType="video/*"
                }
                isZip(file.name)->{
                    imgFile.setImageResource(R.drawable.ic_zip)
                    fileType="application/zip"
                }
                else->{
                    imgFile.setImageResource(R.drawable.ic_file)
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
            itemView.setOnLongClickListener {
                fileEvent.onFileLongClick(file,adapterPosition)
                true
            }
        }
    }


    fun changeViewType(viewType: Int){
        numberForListForm=viewType
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return numberForListForm
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view:View
        if (viewType==0){
            view=LayoutInflater.from(parent.context).inflate(R.layout.item_file_linear,parent,false)
        }else{
            view=LayoutInflater.from(parent.context).inflate(R.layout.item_file_grid,parent,false)
        }
        return FileViewHolder(view)
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

    fun addFolder(file: File){
        fileList.add(0,file)
        notifyItemInserted(0)
    }
    fun removeFile(file: File,position: Int){
        fileList.remove(file)
        notifyItemRemoved(position)
    }
    interface FileEvent{
        fun onFileClick(file : File,type:String)
        fun onFolderClick(path: String)
        fun onFileLongClick(file:File,position: Int)
    }

}