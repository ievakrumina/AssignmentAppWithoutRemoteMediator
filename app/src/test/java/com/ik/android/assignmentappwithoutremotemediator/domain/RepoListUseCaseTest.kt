package com.ik.android.assignmentappwithoutremotemediator.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.paging.RepoListPagingSource
import com.ik.android.assignmentappwithoutremotemediator.rules.MainDispatcherRule
import com.ik.android.assignmentappwithoutremotemediator.testdata.TestData
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RepoListUseCaseTest {

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var useCase: RepoListUseCase
  private val pagingSource: RepoListPagingSource = mockk(relaxed = true)

  @Before
  fun setUp() {
    useCase = RepoListUseCase(pagingSource = pagingSource)
  }

  @Test
  fun `get repo list`() = runTest {

    val result = useCase.getRepoList().first()
    assertNotNull(result)
  }

  @After
  fun tearDown() {
    unmockkAll()
  }
}