package com.ik.android.assignmentappwithoutremotemediator.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ik.android.assignmentappwithoutremotemediator.common.Constants.STARTING_PAGE
import com.ik.android.assignmentappwithoutremotemediator.common.getResourceResult
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.repository.MainRepository
import java.lang.Exception
import javax.inject.Inject

class RepoListPagingSource @Inject constructor(
    private val repository: MainRepository
  ) : PagingSource<Int, RepoData>() {
    override fun getRefreshKey(state: PagingState<Int, RepoData>): Int? {
      // refresh key close to the last accessed item
      return state.anchorPosition?.let { anchorPosition ->
        state.closestPageToPosition(anchorPosition)?.prevKey
      }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoData> {
      // Start paging with the STARTING_PAGE if this is the first load
      val page = params?.key ?: STARTING_PAGE
      val response = repository.getRepos(page = page, item = params.loadSize).getResourceResult()
      return if (response.first != null) {
        val repoData = response.first!!
        LoadResult.Page(
          data = repoData,
          prevKey = if (page == STARTING_PAGE) null else page - 1,
          nextKey = if (repoData.isEmpty()) null else page + 1
        )
      } else {
        when (response.second) {
          null -> LoadResult.Error(Exception("Generic exception"))
          else -> LoadResult.Error(response.second!!)
        }
      }
    }
  }