package com.ik.android.assignmentappwithoutremotemediator.data.repository

import android.util.Log
import com.ik.android.assignmentappwithoutremotemediator.common.Resource
import com.ik.android.assignmentappwithoutremotemediator.common.asSuccess
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import com.ik.android.assignmentappwithoutremotemediator.data.service.RepoApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val service: RepoApiService): MainRepository {
  override suspend fun getRepos(page: Int, items: Int): Resource<List<RepoData>> =
    try { service.getRepos(page, items).asSuccess()
    } catch (exception: IOException) {
      Resource.Error(exception)
    } catch (exception: HttpException) {
      Resource.Error(exception)
  }
}