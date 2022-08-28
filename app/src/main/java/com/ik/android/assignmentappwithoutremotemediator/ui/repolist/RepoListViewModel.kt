package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import androidx.lifecycle.*
import androidx.paging.PagingData
import com.ik.android.assignmentappwithoutremotemediator.data.repository.MainRepository
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.domain.RepoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val repoListUseCase: RepoListUseCase) : ViewModel() {

  fun getRepoList(): LiveData<PagingData<RepoData>> =
    repoListUseCase.getRepoList().asLiveData()

}