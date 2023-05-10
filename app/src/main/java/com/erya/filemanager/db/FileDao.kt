package com.erya.filemanager.db

import androidx.room.*

@Dao
interface FileDao {

    @Query("SELECT * FROM saved_files ORDER BY id ASC")
    suspend fun getSavedHashes(): List<FileEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(f: FileEntity)

    @Delete
    suspend fun delete(f: FileEntity)

    @Query("SELECT hash FROM saved_files WHERE id = :fileId")
    suspend fun search(fileId: String): Int

    @Query("SELECT EXISTS (SELECT 1 FROM saved_files WHERE id = :fileId)")
    suspend fun searchIs(fileId: String): Boolean
}