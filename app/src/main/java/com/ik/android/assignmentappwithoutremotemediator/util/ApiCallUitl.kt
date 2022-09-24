package com.ik.android.assignmentappwithoutremotemediator.util

import com.ik.android.assignmentappwithoutremotemediator.common.Resource
import com.ik.android.assignmentappwithoutremotemediator.common.asSuccess
import retrofit2.HttpException
import java.io.IOException

suspend inline fun <T> safeApiCall(apiCallFunction: suspend () -> T): Resource<T> {
  return try {
    apiCallFunction().asSuccess()
  } catch (e: HttpException) {
    Resource.Error(e)
  } catch (e: IOException) {
    Resource.Error(e)
  }
}

