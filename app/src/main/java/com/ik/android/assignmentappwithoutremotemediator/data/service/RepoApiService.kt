package com.ik.android.assignmentappwithoutremotemediator.data.service

import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RepoApiService {

  @GET("repos")
  suspend fun getRepos(
    @Query("page") page: Int,
    @Query("per_page") items: Int
  ): List<RepoData>
}