package com.erya.filemanager.presentation.models

data class File(
    val name: String,
    val isDirectory: Boolean,
    val fullName: String,
    val size: Int
)
