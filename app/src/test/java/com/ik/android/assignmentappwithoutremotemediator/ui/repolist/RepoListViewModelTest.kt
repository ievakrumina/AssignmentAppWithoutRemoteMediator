package com.ik.android.assignmentappwithoutremotemediator.ui.repolist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.domain.RepoListUseCase
import com.ik.android.assignmentappwithoutremotemediator.rules.MainDispatcherRule
import com.ik.android.assignmentappwithoutremotemediator.testdata.TestData.testRepo
import com.ik.android.assignmentappwithoutremotemediator.util.ConnectivityObserver
import com.ik.android.assignmentappwithoutremotemediator.util.LiveDataTestUtil.getValue
import com.ik.android.assignmentappwithoutremotemediator.util.observeForTesting
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.any
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepoListViewModelTest {

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var viewModel: RepoListViewModel
  private val useCase: RepoListUseCase = mockk(relaxed = true)


  @Before
  fun setUp() {
    viewModel = RepoListViewModel(useCase)
  }

  @Test
  fun `get repo list`() = runTest {
    val response = PagingData.from(listOf(testRepo))
    coEvery { useCase.getRepoList() } returns flowOf(response)

    assertNotNull(getValue(viewModel.getRepoList()))

    coVerify( exactly = 1 ) { useCase.getRepoList() }
  }

  @Test
  fun `set network ui state`() = runTest {
    val networkStatus = ConnectivityObserver.Status.Available

    assertNull( viewModel.networkStatus)
    viewModel.setNetworkStatus(networkStatus)

    assertEquals(networkStatus, viewModel.networkStatus )
  }

  @Test
  fun `get ui state error when refresh state returns error`() {
    val source = LoadStates(refresh = LoadState.Error(Throwable()), append = LoadState.Loading, prepend = LoadState.Loading)
    val state = CombinedLoadStates(refresh = LoadState.Error(Throwable()), append = LoadState.Loading, prepend = LoadState.Loading, source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Error(RepoListViewModel.LoadingStates.InitialLoad),getValue(viewModel.listState))
  }

  @Test
  fun `get ui state error when append state returns error`() {
    val source = LoadStates(refresh = LoadState.NotLoading(false), append = LoadState.Error(Throwable()), prepend = LoadState.NotLoading(false))
    val state = CombinedLoadStates(refresh = LoadState.NotLoading(false), append = LoadState.Error(Throwable()), prepend = LoadState.NotLoading(false), source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Error(),getValue(viewModel.listState))
  }

  @Test
  fun `get ui state error when prepend state returns error`() {
    val source = LoadStates(refresh = LoadState.NotLoading(false), append = LoadState.NotLoading(false), prepend = LoadState.Error(Throwable()))
    val state = CombinedLoadStates(refresh = LoadState.NotLoading(false), append = LoadState.NotLoading(false), prepend = LoadState.Error(Throwable()), source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Error(),getValue(viewModel.listState))
  }

  @Test
  fun `get ui state loading when refresh state returns loading`() {
    val source = LoadStates(refresh = LoadState.Loading, append = LoadState.NotLoading(false), prepend = LoadState.NotLoading(false))
    val state = CombinedLoadStates(refresh = LoadState.Loading, append = LoadState.NotLoading(false), prepend = LoadState.NotLoading(false), source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Loading(RepoListViewModel.LoadingStates.InitialLoad),getValue(viewModel.listState))
  }

  @Test
  fun `get ui state loading when append state returns loading`() {
    val source = LoadStates(refresh = LoadState.NotLoading(false), append = LoadState.Loading, prepend = LoadState.NotLoading(false))
    val state = CombinedLoadStates(refresh = LoadState.NotLoading(false), append = LoadState.Loading, prepend = LoadState.NotLoading(false), source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Loading(RepoListViewModel.LoadingStates.AppendLoad),getValue(viewModel.listState))
  }

  @Test
  fun `get ui state loading when prepend state returns loading`() {
    val source = LoadStates(refresh = LoadState.NotLoading(false), append = LoadState.NotLoading(false), prepend = LoadState.Loading)
    val state = CombinedLoadStates(refresh = LoadState.NotLoading(false), append = LoadState.NotLoading(false), prepend = LoadState.Loading, source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Loading(RepoListViewModel.LoadingStates.PrependLoad),getValue(viewModel.listState))
  }

  @Test
  fun `get ui state present when all states returns notloading`() {
    val source = LoadStates(refresh = LoadState.NotLoading(false), append = LoadState.NotLoading(false), prepend = LoadState.NotLoading(false))
    val state = CombinedLoadStates(refresh = LoadState.NotLoading(false), append = LoadState.NotLoading(false), prepend = LoadState.NotLoading(false), source = source)

    viewModel.setListState(state)

    assertEquals(RepoListViewModel.RepoListState.Present,getValue(viewModel.listState))
  }


  @After
  fun tearDown() {
    unmockkAll()
  }
}