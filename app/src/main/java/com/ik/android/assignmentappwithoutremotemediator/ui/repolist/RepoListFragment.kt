package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.ik.android.assignmentappwithoutremotemediator.R
import com.ik.android.assignmentappwithoutremotemediator.databinding.FragmentRepoListBinding
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.ui.repolist.RepoListFragmentDirections.Companion.actionRepoListFragmentToSingleRepoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepoListFragment : Fragment(R.layout.fragment_repo_list), RepoAdapter.OnItemClickedListener {

  private val viewModel: RepoListViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val binding = FragmentRepoListBinding.bind(view)
    val repoAdapter = RepoAdapter(this)

    binding.apply {
      repoList.apply {
        adapter = repoAdapter
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      repoAdapter.loadStateFlow.collectLatest { state ->
        Log.d("Test", "$state")
        // Show loading wheel, refresh == loading
        binding.loadingProgress.isVisible = state.source.refresh is LoadState.Loading

        // Show append and prepend animation
        binding.appendProgress.isVisible = state.source.append is LoadState.Loading
        binding.prependProgress.isVisible = state.source.prepend is LoadState.Loading

        // Show empty state
        binding.emptyList.isVisible =
          state.source.refresh is LoadState.NotLoading && repoAdapter.itemCount == 0

        // Show error state
        if (state.source.refresh is LoadState.Error) {
          if (repoAdapter.itemCount == 0) {
            binding.errorState.isVisible = true
            // Show refresh button
          } else {
            // Show toast, when refresh/append/prepend in error
            Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
            binding.errorState.isVisible = false
          }
        }
      }
    }

    viewModel.getRepoList().observe(viewLifecycleOwner) {
      repoAdapter.submitData(lifecycle = lifecycle, pagingData = it)
    }
  }

  override fun onItemClicked(repoData: RepoData) {
    val action = actionRepoListFragmentToSingleRepoFragment(repoData.name, repoData)
    view?.findNavController()?.navigate(action)
  }
}