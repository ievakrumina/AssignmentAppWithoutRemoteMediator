package com.ik.android.assignmentappwithoutremotemediator.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.ik.android.assignmentappwithoutremotemediator.common.Constants.ITEMS_PER_PAGE
import com.ik.android.assignmentappwithoutremotemediator.data.paging.RepoListPagingSource
import javax.inject.Inject

class RepoListUseCase @Inject constructor(private val pagingSource: RepoListPagingSource) {

  fun getRepoList() = Pager(PagingConfig(ITEMS_PER_PAGE)) {
    pagingSource
  }.flow
}