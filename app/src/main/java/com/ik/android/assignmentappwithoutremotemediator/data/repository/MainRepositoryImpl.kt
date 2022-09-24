package com.ik.android.assignmentappwithoutremotemediator.data.repository

import com.ik.android.assignmentappwithoutremotemediator.common.Resource
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.service.RepoApiService
import com.ik.android.assignmentappwithoutremotemediator.util.safeApiCall
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val service: RepoApiService): MainRepository {
  override suspend fun getRepos(page: Int, items: Int): Resource<List<RepoData>> =
    safeApiCall { service.getRepos(page, items) }
}