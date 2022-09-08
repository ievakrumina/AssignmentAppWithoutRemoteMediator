package com.ik.android.assignmentappwithoutremotemediator.testdata

import com.ik.android.assignmentappwithoutremotemediator.data.model.OwnerData
import com.ik.android.assignmentappwithoutremotemediator.data.model.RepoData

object TestData {

  private val testOwner = OwnerData("id", "avatar")
  val testRepo = RepoData("id", "name","full name", "description", "url", testOwner, true, "Private")

}