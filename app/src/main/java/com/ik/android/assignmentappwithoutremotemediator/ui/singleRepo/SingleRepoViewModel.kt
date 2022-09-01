package com.ik.android.assignmentappwithoutremotemediator.ui.singleRepo

import androidx.lifecycle.ViewModel
import com.ik.android.assignmentappwithoutremotemediator.common.Visibility

class SingleRepoViewModel : ViewModel() {

  fun getVisibility(value: String?) =
    if (value == Visibility.PUBLIC.value) Visibility.PUBLIC else Visibility.PRIVATE

}