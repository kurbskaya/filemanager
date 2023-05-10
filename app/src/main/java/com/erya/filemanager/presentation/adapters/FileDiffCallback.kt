package com.erya.filemanager.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.erya.filemanager.presentation.models.File

class FileDiffCallback : DiffUtil.ItemCallback<File>() {

    override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: File, newItem: File) =
        oldItem.name == newItem.name
}