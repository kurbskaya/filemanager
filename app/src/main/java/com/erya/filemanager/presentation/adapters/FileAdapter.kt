package com.erya.filemanager.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erya.filemanager.R
import com.erya.filemanager.databinding.ItemFileBinding
import com.erya.filemanager.presentation.models.File

class FileAdapter(private val onItemClick:  (item: File) -> Unit)
    : ListAdapter<File, FileAdapter.GifViewHolder>(FileDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val binding = ItemFileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val id = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClick(id)
        }
        holder.bind(id)
    }


    class GifViewHolder(
        gifItemLayoutBinding: ItemFileBinding,
    ) : RecyclerView.ViewHolder(gifItemLayoutBinding.root) {

        private var cell: File? = null
        private val binding = gifItemLayoutBinding

        fun bind(product: File) {
            this.cell = product
            binding.text.text = product.name
            binding.size.text = product.size.toString()
            if (product.isDirectory) {
                binding.img.setImageResource(R.drawable.ic_baseline_folder_24)
//            } else if (product.name.endsWith(".jpg") || product.name.endsWith(".png")
//                || product.name.endsWith(".jpeg")){
//                binding.img.setImageURI(product.fullName.toUri())
//            } else if (product.name.endsWith(".pdf")){
//                binding.img.setImageResource(R.drawable.ic_baseline_picture_as_pdf_24)
//            } else if (product.name.endsWith(".zip")){
//                binding.img.setImageResource(R.drawable.ic_baseline_archive_24)
            } else {
                binding.img.setImageResource(R.drawable.ic_baseline_insert_drive_file_24)
            }
        }
    }

}