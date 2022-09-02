package com.ik.android.assignmentappwithoutremotemediator.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ik.android.assignmentappwithoutremotemediator.common.Constants.ITEMS_PER_PAGE
import com.ik.android.assignmentappwithoutremotemediator.data.paging.RepoListPagingSource
import javax.inject.Inject

class RepoListUseCase @Inject constructor(private val pagingSource: RepoListPagingSource) {

  /**
   * Set initialLoad size, because otherwise the 2nd page was showed twice
   * TODO: Check if this can be solved in other way.
  */
  fun getRepoList() = Pager(PagingConfig(pageSize = ITEMS_PER_PAGE, initialLoadSize = ITEMS_PER_PAGE)) {
    pagingSource
  }.flow
}