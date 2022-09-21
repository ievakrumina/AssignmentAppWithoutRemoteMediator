package com.ik.android.assignmentappwithoutremotemediator.data.paging


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.ik.android.assignmentappwithoutremotemediator.common.Resource
import com.ik.android.assignmentappwithoutremotemediator.common.asSuccess
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.repository.MainRepository
import com.ik.android.assignmentappwithoutremotemediator.rules.MainDispatcherRule
import com.ik.android.assignmentappwithoutremotemediator.testdata.TestData.testRepo
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response

class RepoListPagingSourceTest {

  @get:Rule
  val rule = InstantTaskExecutorRule()

  @get:Rule
  val dispatcherRule = MainDispatcherRule()


  private lateinit var source: RepoListPagingSource
  private val repositoryImpl: MainRepository = mockk(relaxed = true)

  @Before
  fun setUp() {
    source = RepoListPagingSource(repositoryImpl)
  }

  /**
   * Documentation for testing paging source
   * https://developer.android.com/topic/libraries/architecture/paging/test#kotlin
   */

  @Test
  fun `load returns page for successful call`() = runTest {
    val repoList = listOf(testRepo)
    coEvery { repositoryImpl.getRepos(any(), any()) } returns repoList.asSuccess()

    val expected = PagingSource.LoadResult.Page(
      data = repoList,
      prevKey = null,
      nextKey = 2
    )

    val actual = source.load(
      PagingSource.LoadParams.Refresh(
        key = 1,
        loadSize = 1,
        placeholdersEnabled = false
      )
    )

    assertEquals(expected,actual)
  }

  @Test
  fun `load returns generic error`() = runTest {
    coEvery { repositoryImpl.getRepos(any(), any()) } returns Resource.Error()

    val actual = source.load(
      PagingSource.LoadParams.Refresh(
        key = null,
        loadSize = 1,
        placeholdersEnabled = false
      )
    )

    assertTrue(actual is PagingSource.LoadResult.Error)
    assertEquals("Generic exception", (actual as PagingSource.LoadResult.Error).throwable.message)
  }

  @Test
  fun `load returns http error`() = runTest {
    val errorResponse = mockk<Response<List<RepoData>>>(relaxed = true)
    coEvery { repositoryImpl.getRepos(any(), any()) } returns Resource.Error(HttpException(errorResponse))

    val actual = source.load(
      PagingSource.LoadParams.Refresh(
        key = null,
        loadSize = 1,
        placeholdersEnabled = false
      )
    )

    assertTrue(actual is PagingSource.LoadResult.Error)
    assertTrue((actual as PagingSource.LoadResult.Error).throwable is HttpException)
    assertFalse(actual.throwable.message?.equals("Generic exception") == true)
  }

  @After
  fun tearDown() {
    unmockkAll()
  }
}