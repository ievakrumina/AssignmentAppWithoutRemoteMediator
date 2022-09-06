package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import androidx.lifecycle.*
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.domain.RepoListUseCase
import com.ik.android.assignmentappwithoutremotemediator.util.ConnectivityObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(private val repoListUseCase: RepoListUseCase) :
  ViewModel() {


  sealed class RepoListState {
    data class Error(val type: LoadingStates? = null) : RepoListState()
    data class Loading(val type: LoadingStates? = null) : RepoListState()
    object Present : RepoListState()
  }

  sealed class LoadingStates {
    object InitialLoad : LoadingStates()
    object PrependLoad : LoadingStates()
    object AppendLoad : LoadingStates()
  }

  private val _listState = MutableLiveData<RepoListState>()
  val listState: LiveData<RepoListState>
    get() = _listState

  private var _networkStatus: ConnectivityObserver.Status? = null
  val networkStatus: ConnectivityObserver.Status?
    get() = _networkStatus

  fun setListState(state: CombinedLoadStates) {
    val transformedState = when (state.source.refresh) {
      is LoadState.NotLoading -> {
        if (state.source.prepend is LoadState.Loading) {
          RepoListState.Loading(LoadingStates.PrependLoad)
        } else if (state.source.append is LoadState.Loading) {
          RepoListState.Loading(LoadingStates.AppendLoad)
        } else if (state.source.append is LoadState.Error || state.source.prepend is LoadState.Error) {
          RepoListState.Error()
        } else {
          RepoListState.Present
        }
      }
      LoadState.Loading -> RepoListState.Loading(LoadingStates.InitialLoad)
      is LoadState.Error -> RepoListState.Error(LoadingStates.InitialLoad)
    }
    _listState.value = transformedState
  }

  /**
   * Used cachedIn(viewModelScope) so that after configuration change
   * fragment received already cached data.
   */
  fun getRepoList(): LiveData<PagingData<RepoData>> =
    repoListUseCase.getRepoList().cachedIn(viewModelScope).asLiveData()

  /**
   * Set network status.
   * Status will be observed in fragment to trigger automatic retry
   */
  fun setNetworkStatus(status: ConnectivityObserver.Status) {
    _networkStatus = status
  }

}