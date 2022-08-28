package com.ik.android.assignmentappwithoutremotemediator.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class OwnerData(
  @Json(name = "id")
  val ownerId: String,

  @Json(name = "avatar_url")
  val avatar: String
) : Parcelable
