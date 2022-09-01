package com.ik.android.assignmentappwithoutremotemediator.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


fun Activity.openChromeTab(url: String) {
  val webpage: Uri = Uri.parse(url)
  val intent = Intent(Intent.ACTION_VIEW, webpage)
  if (intent.resolveActivity(this.packageManager) != null) {
    startActivity(this, intent, null)
  }
}