package com.ik.android.assignmentappwithoutremotemediator.ui.singleRepo

import com.ik.android.assignmentappwithoutremotemediator.common.Visibility
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SingleRepoViewModelTest {
  private lateinit var viewModel:SingleRepoViewModel

  @Before
  fun setUp() {
    viewModel = SingleRepoViewModel()
  }

  @Test
  fun `test getVisibility when private`() {
    val res = viewModel.getVisibility("private")
    assertEquals(Visibility.PRIVATE.value,res.value)
  }

  @Test
  fun `test getVisibility when public`() {
    val res = viewModel.getVisibility("public")
    assertEquals(Visibility.PUBLIC.value,res.value)
  }

  @Test
  fun `test getVisibility when unknown status`() {
    val res = viewModel.getVisibility(null)
    assertEquals(Visibility.PRIVATE.value,res.value)
  }
}