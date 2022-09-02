package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.domain.RepoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val repoListUseCase: RepoListUseCase) : ViewModel() {

  /**
   * Used cachedIn(viewModelScope) so that after configuration change
   * fragment received already cached data.
   */
  fun getRepoList(): LiveData<PagingData<RepoData>> =
    repoListUseCase.getRepoList().cachedIn(viewModelScope).asLiveData()

}