package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ik.android.assignmentappwithoutremotemediator.R
import com.ik.android.assignmentappwithoutremotemediator.databinding.FragmentRepoListBinding
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.ui.repolist.RepoListFragmentDirections.Companion.actionRepoListFragmentToSingleRepoFragment
import dagger.hilt.android.AndroidEntryPoint

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

    viewModel.getRepoList().observe(viewLifecycleOwner) {
      repoAdapter.submitData(lifecycle = lifecycle, pagingData = it)
    }
  }

  override fun onItemClicked(repoData: RepoData) {
    val action = actionRepoListFragmentToSingleRepoFragment(repoData.name,repoData)
    view?.findNavController()?.navigate(action)
  }
}