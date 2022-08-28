package com.ik.android.assignmentappwithoutremotemediator.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ik.android.assignmentappwithoutremotemediator.common.Constants.ITEMS_PER_PAGE
import com.ik.android.assignmentappwithoutremotemediator.common.Constants.STARTING_PAGE
import com.ik.android.assignmentappwithoutremotemediator.common.getSuccessOrNull
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.repository.MainRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepoListPagingSource @Inject constructor(
    private val repository: MainRepository
  ) : PagingSource<Int, RepoData>() {
    override fun getRefreshKey(state: PagingState<Int, RepoData>): Int? {
      return state.anchorPosition?.let { anchorPosition ->
        // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
        // multiple pages, the initial load will still load items centered around
        // anchorPosition. This also prevents needing to immediately launch prepend due to
        // prefetchDistance.
        state.closestPageToPosition(anchorPosition)?.prevKey
      }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoData> {
      // Start paging with the STARTING_PAGE if this is the first load
      val page = params?.key ?: STARTING_PAGE
      return try {
        val repoData = repository.getRepos(page = page, item = params.loadSize).getSuccessOrNull()
        LoadResult.Page(
          data = repoData ?: emptyList() ,
          prevKey = if (page == STARTING_PAGE) null else page - 1,
          nextKey = if (repoData.isNullOrEmpty()) null else page + 1
        )
      } catch (e: IOException) {
        LoadResult.Error(e)
      } catch (e: HttpException) {
        LoadResult.Error(e)
      }
    }
  }