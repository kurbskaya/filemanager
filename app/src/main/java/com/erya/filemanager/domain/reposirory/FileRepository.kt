package com.erya.filemanager.domain.reposirory

import com.erya.filemanager.db.FileEntity
import com.erya.filemanager.presentation.models.File

interface FileRepository {
    fun getHomeDirectory(): List<File>?
    fun nextDirectory(file: String, sort: Int): List<File>?
    fun previousDir(): List<File>?
    fun switchSort(type: Int): List<File>?
    fun getSize(f: String): Int
    suspend fun insert(f: List<File>?)
    suspend fun getRecent(value: List<File>?): List<File>?
}