package com.ik.android.assignmentappwithoutremotemediator.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class RepoData(
  @Json(name = "id")
  val id: String,

  @Json(name = "name")
  val name: String,

  @Json(name = "full_name")
  val fullName: String,

  @Json(name = "description")
  val description: String?,

  @Json(name = "html_url")
  val url: String,

  @Json(name = "owner")
  val owner: OwnerData,

  @Json(name = "private")
  val privacy: Boolean,

  @Json(name = "visibility")
  val visibility: String
) : Parcelable
