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
import androidx.recyclerview.widget.LinearLayoutManager
import com.ik.android.assignmentappwithoutremotemediator.R
import com.ik.android.assignmentappwithoutremotemediator.databinding.FragmentRepoListBinding
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.ui.repolist.RepoListFragmentDirections.Companion.actionRepoListFragmentToSingleRepoFragment
import com.ik.android.assignmentappwithoutremotemediator.util.ConnectivityObserver
import com.ik.android.assignmentappwithoutremotemediator.util.NetworkConnectivityObserver
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "RepoListFragment"

@AndroidEntryPoint
class RepoListFragment : Fragment(R.layout.fragment_repo_list), RepoAdapter.OnItemClickedListener {

  private val viewModel: RepoListViewModel by viewModels()

  private lateinit var connectivityObserver: ConnectivityObserver

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    connectivityObserver = NetworkConnectivityObserver(requireContext())
    val binding = FragmentRepoListBinding.bind(view)
    val repoAdapter = RepoAdapter(this)

    binding.apply {
      repoList.apply {
        adapter = repoAdapter
        layoutManager = LinearLayoutManager(requireContext())
        setHasFixedSize(true)
      }
    }

    observeNetworkStates(repoAdapter)

    // Send load state from pagingAdapter to viewModel to map different UI view states
    repoAdapter.addLoadStateListener { loadState ->
      viewModel.setListState(loadState)
    }

    // Observe the list UI states from viewModel
    viewModel.listState.observe(viewLifecycleOwner) { state ->
      when (state) {
        is RepoListViewModel.RepoListState.Error -> {
          handleErrorState(binding, state)
        }
        is RepoListViewModel.RepoListState.Loading -> {
          handleLoadingState(binding, state)
        }
        RepoListViewModel.RepoListState.Present -> {
          handlePresentState(binding, repoAdapter)
        }
      }
    }

    // Old implementation
    /*viewLifecycleOwner.lifecycleScope.launch {
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
    } */

    // Submit list data to paging adapter
    viewModel.getRepoList().observe(viewLifecycleOwner) {
      repoAdapter.submitData(lifecycle = lifecycle, pagingData = it)
    }
  }

  private fun handlePresentState(
    binding: FragmentRepoListBinding,
    repoAdapter: RepoAdapter
  ) {
    binding.apply {
      // Handle list states
      emptyList.isVisible = repoAdapter.itemCount == 0
      repoList.isVisible = repoAdapter.itemCount > 0

      // Disable loading states
      loadingProgress.isVisible = false
      prependProgress.isVisible = false
      appendProgress.isVisible = false

      // Disable error state
      errorState.isVisible = false
    }
  }

  private fun handleLoadingState(
    binding: FragmentRepoListBinding,
    state: RepoListViewModel.RepoListState.Loading
  ) {
    binding.apply {
      // Handle loading states
      appendProgress.isVisible = state.type == RepoListViewModel.LoadingStates.AppendLoad
      prependProgress.isVisible = state.type == RepoListViewModel.LoadingStates.PrependLoad
      loadingProgress.isVisible = state.type == RepoListViewModel.LoadingStates.InitialLoad

      // Disable error state
      errorState.isVisible = false

      // Disable empty state
      emptyList.isVisible = false
    }
  }

  private fun handleErrorState(
    binding: FragmentRepoListBinding,
    state: RepoListViewModel.RepoListState.Error
  ) {
    viewModel.setShouldShowToast(true)
    binding.apply {
      // Handle error states
      if (state.type == RepoListViewModel.LoadingStates.InitialLoad) {
        errorState.isVisible = true
      } else {
        errorState.isVisible = false
        Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
      }
      // Disable loading state
      loadingProgress.isVisible = false
      prependProgress.isVisible = false
      appendProgress.isVisible = false
    }
  }

  override fun onItemClicked(repoData: RepoData) {
    val action = actionRepoListFragmentToSingleRepoFragment(repoData.name, repoData)
    view?.findNavController()?.navigate(action)
  }

  private fun observeNetworkStates(repoAdapter: RepoAdapter) {
    viewLifecycleOwner.lifecycleScope.launch {
      connectivityObserver.observe().collect { status ->
        viewModel.setNetworkStatus(status)
        Log.d(TAG, "$status")
        when(status) {
          ConnectivityObserver.Status.Available -> handleNetworkAvailable(repoAdapter, status)
          ConnectivityObserver.Status.Unavailable -> {}
          ConnectivityObserver.Status.Losing -> {}
          ConnectivityObserver.Status.Lost -> {}
        }
      }
    }
  }

  private fun handleNetworkAvailable(repoAdapter: RepoAdapter, status: ConnectivityObserver.Status) {
    if (viewModel.networkStatus == status && viewModel.shouldShowOnlineToast) {
      Toast.makeText(context, R.string.network_available, Toast.LENGTH_SHORT).show()
      repoAdapter.retry()
    }
  }
}