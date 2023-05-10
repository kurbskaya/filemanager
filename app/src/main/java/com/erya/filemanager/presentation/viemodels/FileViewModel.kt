package com.erya.filemanager.presentation.viemodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erya.filemanager.db.FileEntity
import com.erya.filemanager.domain.reposirory.FileRepository
import com.erya.filemanager.presentation.models.File
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class FileViewModel(private val repository: FileRepository) : ViewModel() {
    private val _fileLiveData = MutableLiveData<List<File>>()
    val fileLiveData: LiveData<List<File>> = _fileLiveData

    private val _recentLiveData = MutableLiveData<List<File>>()
    val recentLiveData: LiveData<List<File>> = _recentLiveData

    fun getHomeDirectory() {
        _fileLiveData.value = repository.getHomeDirectory()
        viewModelScope.launch {
            runCatching {
                repository.insert(_fileLiveData.value)
            }.onSuccess {
                _recentLiveData.value = repository.getRecent(_fileLiveData.value)
            }.onFailure {
            }
        }
    }

    fun clean() {
        _fileLiveData.value = emptyList()
    }

    fun nextDirectory(file: String, sort: Int) {
        _fileLiveData.value = repository.nextDirectory(file, sort)
        viewModelScope.launch {
           runCatching {
               repository.insert(_fileLiveData.value)
           }.onSuccess {
               _recentLiveData.value = repository.getRecent(_fileLiveData.value) }
        }
    }

    fun previousDir() {
        _fileLiveData.value = repository.previousDir()
        viewModelScope.launch {
            kotlin.runCatching {
                repository.insert(_fileLiveData.value)
            }.onSuccess { _recentLiveData.value = repository.getRecent(_fileLiveData.value)  }
        }
    }

    fun switchSort(type: Int) {
        _fileLiveData.value = repository.switchSort(type)
    }
}