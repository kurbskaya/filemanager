package com.erya.filemanager.data.datasource

import android.os.Environment
import com.erya.filemanager.Manager

class FileDataSource(private val manager: Manager) {
    fun getHomeDirectory(): List<String> {
        return manager.setHomeDir(Environment.getExternalStorageDirectory().path)
    }

    fun nextDirectory(file: String, sort: Int): List<String> {
        return manager.getNextDirectory(file, sort)
    }

    fun previousDir(): List<String> {
        return manager.getPreviousDir()
    }

    fun switchSort(type: Int): List<String> {
        return manager.switchSort(type)
    }

    fun isDirectory(f: String): Boolean {
        return manager.isDirectory(f)
    }

    fun getFullName(f: String): String {
        return manager.getFullName(f)
    }

    fun getSize(f: String): Int {
        return manager.getSize2(f)
    }
}