package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ik.android.assignmentappwithoutremotemediator.R
import com.ik.android.assignmentappwithoutremotemediator.common.Visibility
import com.ik.android.assignmentappwithoutremotemediator.databinding.RepoItemBinding
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData

class RepoAdapter(private val listener: OnItemClickedListener) :
  PagingDataAdapter<RepoData, RepoAdapter.RepoViewHolder>(DiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
    val binding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return RepoViewHolder(binding)
  }

  override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
    val currentItem = getItem(position)
    currentItem?.let { holder.bind(it) }
  }

  inner class RepoViewHolder(
    private val binding: RepoItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {

    init {
      binding.apply {
        root.setOnClickListener {
          val position = adapterPosition
          if (position != RecyclerView.NO_POSITION) {
            val repo = getItem(position)
            repo?.let { listener.onItemClicked(it) }
          }
        }
      }
    }

    fun bind(item: RepoData) {
      binding.apply {
        itemName.text = item.name
        itemAvatarImage.load(item.owner.avatar.toUri().buildUpon().scheme("https").build()) {
          crossfade(true)
          error(R.drawable.image_placeholder)
        }

        handlePrivacy(item)
        handleVisibility(item)
      }
    }

    private fun RepoItemBinding.handlePrivacy(item: RepoData) {
      if (item.privacy) {
        itemPrivacy.load(R.drawable.ic_private)
      } else {
        itemPrivacy.load(R.drawable.ic_public)
      }
    }

    private fun RepoItemBinding.handleVisibility(item: RepoData) {
      when (item.visibility) {
        Visibility.PUBLIC.value -> itemVisibility.load(R.drawable.ic_visibility)
        Visibility.PRIVATE.value -> itemVisibility.load(R.drawable.ic_visibility_off)
        else -> itemVisibility.load(R.drawable.ic_visibility_off)
      }
    }
  }


  interface OnItemClickedListener {
    fun onItemClicked(repoData: RepoData)
  }

  class DiffCallback : DiffUtil.ItemCallback<RepoData>() {
    override fun areItemsTheSame(oldItem: RepoData, newItem: RepoData) =
      oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: RepoData, newItem: RepoData) =
      oldItem == newItem
  }
}
