package com.ik.android.assignmentappwithoutremotemediator.common

sealed class Resource<out T> {
  data class Loading<T>(val data: T? = null) : Resource<T>()
  data class Error<T>(val error: Throwable? = null) : Resource<T>()
  data class Success<T>(val data: T) : Resource<T>()
}

fun <T> T.asSuccess() = Resource.Success(this)
fun <T> T.asLoading() = Resource.Loading(this)
fun <T> T.asError(error: Throwable? = null) = Resource.Error<T>(error)

fun <T> Resource<T>.getSuccessOrNull() = if (this is Resource.Success) {
  this.data
} else {
  null
}

fun <T> Resource<T>.getResourceResult(): Pair<T?, Throwable?> = when (this) {
  is Resource.Error -> Pair(null, this.error)
  is Resource.Loading -> Pair(this.data, null)
  is Resource.Success -> Pair(this.data, null)
}

fun <T> Resource<T>.getErrorOrNull() = if (this is Resource.Error) {
  this.error
} else {
  null
}
