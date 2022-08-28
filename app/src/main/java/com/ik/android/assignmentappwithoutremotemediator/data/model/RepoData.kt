package com.ik.android.assignmentappwithoutremotemediator.data.model

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ik.android.assignmentappwithoutremotemediator.common.Constants.REPO_TABLE
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Entity(tableName = REPO_TABLE)
@Parcelize
@JsonClass(generateAdapter = true)
data class RepoData(
  @Json(name = "id")
  @PrimaryKey
  @NonNull
  val id: String,

  @Json(name = "name")
  val name: String,

  @Json(name = "full_name")
  val fullName: String,

  @Json(name = "description")
  val description: String?,

  @Json(name = "html_url")
  val url: String,

  @Embedded
  @Json(name = "owner")
  val owner: OwnerData,

  @Json(name = "private")
  val privacy: Boolean,

  @Json(name = "visibility")
  val visibility: String
) : Parcelable
