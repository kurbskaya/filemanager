package com.erya.filemanager.presentation.viemodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erya.filemanager.domain.reposirory.FileRepository

class ViewModelFactory(private val fileRepository: FileRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FileViewModel::class.java))
            FileViewModel(fileRepository) as T
        else
            throw IllegalArgumentException("ViewModel $modelClass Not Found")
    }
}