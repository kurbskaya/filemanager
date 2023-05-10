package com.erya.filemanager.data.repository

import androidx.annotation.WorkerThread
import com.erya.filemanager.data.datasource.FileDataSource
import com.erya.filemanager.db.FileDao
import com.erya.filemanager.db.FileEntity
import com.erya.filemanager.domain.reposirory.FileRepository
import com.erya.filemanager.presentation.models.File

class FileRepositoryImpl(
    private val dataSource: FileDataSource,
    private val dao: FileDao
    ): FileRepository {
    override fun getHomeDirectory(): List<File> {
        return dataSource.getHomeDirectory().map {
            File(
                name = it,
                isDirectory =  dataSource.isDirectory(it),
                fullName = dataSource.getFullName(it),
                size = dataSource.getSize(it)
            )
        }
    }

    override fun nextDirectory(file: String, sort: Int): List<File> {
        return dataSource.nextDirectory(file, sort).map {
            File(
                name = it,
                isDirectory = dataSource.isDirectory(it),
                fullName = dataSource.getFullName(it),
                size = dataSource.getSize(it)
            )
        }
    }

    override fun previousDir(): List<File> {
        return dataSource.previousDir().map {
            File(
                name = it,
                isDirectory = dataSource.isDirectory(it),
                fullName = dataSource.getFullName(it),
                size = dataSource.getSize(it)
            )
        }
    }

    override fun switchSort(type: Int): List<File> {
        return dataSource.switchSort(type).map {
            File(
                name = it,
                isDirectory = dataSource.isDirectory(it),
                fullName = dataSource.getFullName(it),
                size = dataSource.getSize(it)
            )
        }
    }

    override fun getSize(f: String): Int {
        return dataSource.getSize(f)
    }

    @WorkerThread
    override suspend fun insert(f: List<File>?) {
        if (f != null) {
            for (i in f) {
                if (!dao.searchIs(i.fullName)) {
                    dao.insert(
                        FileEntity(
                            id = i.fullName,
                            hash = i.hashCode()
                        )
                    )
                }
            }
        }
    }

    @WorkerThread
    override suspend fun getRecent(value: List<File>?): List<File> {
        val arr = ArrayList<File>()
        if (value != null) {
            for (i in value) {
                if (i.hashCode() != dao.search(i.fullName)) {
                    arr.add(i)
                    dao.delete(
                        FileEntity(
                            id = i.fullName
                        )
                    )
                    dao.insert(
                        FileEntity(
                            id = i.fullName,
                            hash = i.hashCode()
                        )
                    )
                }
            }
        }
        return arr
    }
}