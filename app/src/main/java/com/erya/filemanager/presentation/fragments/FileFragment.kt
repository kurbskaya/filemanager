package com.erya.filemanager.presentation.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.erya.filemanager.Manager
import com.erya.filemanager.R
import com.erya.filemanager.data.datasource.FileDataSource
import com.erya.filemanager.data.repository.FileRepositoryImpl
import com.erya.filemanager.databinding.FileFragmentBinding
import com.erya.filemanager.db.FileRoomDatabase
import com.erya.filemanager.presentation.adapters.FileAdapter
import com.erya.filemanager.presentation.models.File
import com.erya.filemanager.presentation.viemodels.FileViewModel
import com.erya.filemanager.presentation.viemodels.ViewModelFactory


class FileFragment : Fragment() {
    private var _binding: FileFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FileViewModel
    private var sort = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = Manager()
        manager.create()
        val fDataSource = FileDataSource(manager)
        viewModel = ViewModelProvider(
            requireActivity(),
            ViewModelFactory(
                FileRepositoryImpl(
                    fDataSource,
                    FileRoomDatabase.getDatabase(requireContext()).fileDao(),
                ),
            )
        )[FileViewModel::class.java]
        viewModel.getHomeDirectory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lifecycleOwner = viewLifecycleOwner
        viewModel.fileLiveData.observe(lifecycleOwner) { files ->
            println(viewModel.fileLiveData.value)
            val fileAdapter = binding.rv.adapter
            if (fileAdapter == null) {
                val myAdapter = FileAdapter(
                    onItemClick = {
                        //viewModel.clean()
                        viewModel.nextDirectory(it.name, sort)
                    }
                )
                binding.rv.adapter = myAdapter
                binding.rv.layoutManager = LinearLayoutManager(requireContext())
                myAdapter.submitList(files)
            } else {
                val myAdapter = fileAdapter as FileAdapter
                myAdapter.submitList(files)
            }
        }

        binding.radio2.setChecked(false)
        binding.radio1.setChecked(false)

        binding.button.setOnClickListener{
            viewModel.previousDir()
        }

        binding.radio.setOnCheckedChangeListener{_, id ->
            if (id == R.id.radio1) {
                viewModel.switchSort(1)
                sort = 1
            }
            if (id == R.id.radio2) {
                viewModel.switchSort(2)
                sort = 2
            }
        }

        binding.rec.setOnClickListener {
            val builderSingle: AlertDialog.Builder = AlertDialog.Builder(requireActivity())

            val arrayAdapter = ArrayAdapter<File>(
                requireActivity(),
                android.R.layout.select_dialog_singlechoice
            )
            viewModel.recentLiveData.value?.let { it1 -> arrayAdapter.addAll(it1) }

            builderSingle.setNegativeButton("cancel",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })

            builderSingle.setAdapter(arrayAdapter,
                DialogInterface.OnClickListener { dialog, which ->
                })
            builderSingle.show()
        }
    }
}