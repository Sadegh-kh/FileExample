package com.example.fileexample.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fileexample.R
import com.example.fileexample.adapters.RecyclerFileAdapter
import com.example.fileexample.databinding.DialogAddNewfileBinding
import com.example.fileexample.databinding.DialogAddNewfolderBinding
import com.example.fileexample.databinding.FragmentFileListBinding
import java.io.File

class FileFragment(private val path: String) : Fragment(), RecyclerFileAdapter.FileEvent {
    private var checkingLinearList = true
    lateinit var binding: FragmentFileListBinding
    private lateinit var adapter:RecyclerFileAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFileListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ourFile = File(path)
        initUi(ourFile)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initUi(file: File) {
        binding.txtNamePath.text = file.name + ">"
        initActionBar()
        initRecyclerView(file)

    }

    private fun initActionBar() {
        binding.btnChangList.setOnClickListener {
            if (checkingLinearList) {
                binding.btnChangList.setImageResource(R.drawable.ic_grid)
                checkingLinearList = false
            } else {
                binding.btnChangList.setImageResource(R.drawable.ic_list)
                checkingLinearList = true
            }
        }

        binding.btnAddFolder.setOnClickListener {
            val dialog=AlertDialog.Builder(context).create()
            val layoutDialogAddNewFolder=DialogAddNewfolderBinding.inflate(layoutInflater)
            dialog.setView(layoutDialogAddNewFolder.root)
            dialog.show()
            layoutDialogAddNewFolder.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            layoutDialogAddNewFolder.btnCreate.setOnClickListener {
                val nameFolder=layoutDialogAddNewFolder.editTxtFolderName.text.toString()

                //file/nameFolder ( File.separator is mean / between file and nameFolder)
                val newFile=File(path+File.separator+nameFolder)

                if (!newFile.exists()){
                    if (newFile.mkdir()){
                        adapter.addFolder(newFile)
                        binding.filesRecycler.scrollToPosition(0)

                        binding.imgEmptyFileList.visibility=View.GONE
                        binding.filesRecycler.visibility=View.VISIBLE
                    }
                    dialog.dismiss()
                }else{
                    Toast.makeText(context, "Folder exist!!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnAddFile.setOnClickListener {
            val dialog=AlertDialog.Builder(context).create()
            val layoutDialogAddNewFile=DialogAddNewfileBinding.inflate(layoutInflater)
            dialog.setView(layoutDialogAddNewFile.root)
            dialog.show()
            layoutDialogAddNewFile.btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            layoutDialogAddNewFile.btnCreate.setOnClickListener {
                val nameFolder=layoutDialogAddNewFile.editTxtFileName.text.toString()

                //file/nameFolder ( File.separator is mean / between file and nameFolder)
                val newFile=File(path+File.separator+nameFolder)

                if (!newFile.exists()){
                    if (newFile.createNewFile()){
                        adapter.addFolder(newFile)
                        binding.filesRecycler.scrollToPosition(0)

                        binding.imgEmptyFileList.visibility=View.GONE
                        binding.filesRecycler.visibility=View.VISIBLE

                    }
                    dialog.dismiss()
                }else{
                    Toast.makeText(context, "File exist!!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun initRecyclerView(file: File) {

        //for check it's folder or file
        if (file.isDirectory) {
            val fileList = arrayListOf<File>()
            fileList.addAll(file.listFiles()!!)
            adapter = RecyclerFileAdapter(fileList, this)
            binding.filesRecycler.adapter = adapter
            if (checkingLinearList) {
                binding.filesRecycler.layoutManager = LinearLayoutManager(context)
            } else {
                binding.filesRecycler.layoutManager = GridLayoutManager(context, 3)
            }
            //for visibility
            if (fileList.isNotEmpty()) {

                binding.imgEmptyFileList.visibility = View.GONE
                binding.filesRecycler.visibility = View.VISIBLE
            } else {
                binding.imgEmptyFileList.visibility = View.VISIBLE
                binding.filesRecycler.visibility = View.GONE
            }


        }


    }

    override fun onFileClick(file: File, type: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val fileUriProvider = FileProvider.getUriForFile(
                requireContext(),
                requireActivity().packageName + ".provider",
                file
            )
            intent.setDataAndType(fileUriProvider,type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    override fun onFolderClick(path: String) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.LayoutMain, FileFragment(path))
        transaction.addToBackStack(null)
        transaction.commit()
    }
}

