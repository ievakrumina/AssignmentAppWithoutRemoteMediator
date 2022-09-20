package com.ik.android.assignmentappwithoutremotemediator.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ik.android.assignmentappwithoutremotemediator.common.getErrorOrNull
import com.ik.android.assignmentappwithoutremotemediator.common.getSuccessOrNull
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.service.RepoApiService
import com.ik.android.assignmentappwithoutremotemediator.rules.MainDispatcherRule
import com.ik.android.assignmentappwithoutremotemediator.testdata.TestData
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class MainRepositoryImplTest {

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @get:Rule
  val dispatcherRule = MainDispatcherRule()

  private lateinit var repositoryImpl: MainRepositoryImpl
  private val service = mockk<RepoApiService>(relaxed = true)

  @Before
  fun setUp() {
    repositoryImpl = MainRepositoryImpl(service)
  }

  @Test
  fun `get repo list successfully`() = runTest {
    val response = listOf(TestData.testRepo)
    coEvery { service.getRepos(any(), any()) } returns response

    val result = repositoryImpl.getRepos(1, 10).getSuccessOrNull()
    assertTrue(result?.size == 1)
    assertEquals(TestData.testRepo, result?.get(0))
  }

  @Test
  fun `get empty repo list successfully`() = runTest {
    val response = emptyList<RepoData>()
    coEvery { service.getRepos(any(), any()) } returns response

    val result = repositoryImpl.getRepos(1, 10).getSuccessOrNull()
    assertTrue(result?.isEmpty() == true)
  }

  @Test
  fun `get repo returns HTTPException`() = runTest {
    val response = mockk<Response<List<RepoData>>>(relaxed = true)
    coEvery { service.getRepos(any(), any()) } throws HttpException(response)

    val result = repositoryImpl.getRepos(1,10).getErrorOrNull()
    assertNotNull(result)
    assertTrue(result is HttpException)
  }

  @Test
  fun `get repo returns IOException`() = runTest {
    coEvery { service.getRepos(any(), any()) } throws IOException()

    val result = repositoryImpl.getRepos(1,10).getErrorOrNull()
    assertNotNull(result)
    assertTrue(result is IOException)
  }

  @After
  fun tearDown() {
    unmockkAll()
  }
}