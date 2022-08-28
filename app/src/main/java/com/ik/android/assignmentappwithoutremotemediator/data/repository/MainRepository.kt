package com.ik.android.assignmentappwithoutremotemediator.data.repository

import com.ik.android.assignmentappwithoutremotemediator.common.Resource
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData

interface MainRepository {

  suspend fun getRepos(page: Int, item:Int): Resource<List<RepoData>>
}