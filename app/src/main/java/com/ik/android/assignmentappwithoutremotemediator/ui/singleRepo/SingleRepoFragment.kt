package com.ik.android.assignmentappwithoutremotemediator.ui.singleRepo

import android.os.Bundle
import android.view.View
import android.webkit.URLUtil
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.ik.android.assignmentappwithoutremotemediator.R
import com.ik.android.assignmentappwithoutremotemediator.common.Visibility
import com.ik.android.assignmentappwithoutremotemediator.databinding.FragmentSingleRepoBinding
import com.ik.android.assignmentappwithoutremotemediator.util.openChromeTab
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleRepoFragment: Fragment(R.layout.fragment_single_repo) {

  private val args  = navArgs<SingleRepoFragmentArgs>()
  private val viewModel: SingleRepoViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val binding = FragmentSingleRepoBinding.bind(view)
    binding.apply {
      singleRepoAvatarImage.load(args.value.singleRepo?.owner?.avatar?.toUri()?.buildUpon()?.scheme("https")?.build()) {
        crossfade(true)
        error(R.drawable.image_placeholder)
      }
      singleRepoFullName.text = args.value.singleRepo?.fullName
      singleRepoDescription.text = args.value.singleRepo?.description
      handlePrivacy()
      handleVisibility()

      singleRepoButton.setOnClickListener {
        handleOpenUrl(args.value.singleRepo?.url)
      }
    }
  }

  private fun FragmentSingleRepoBinding.handleVisibility() {
    when (viewModel.getVisibility(args.value.singleRepo?.visibility)) {
      Visibility.PUBLIC -> singleRepoVisibility.load(R.drawable.ic_visibility)
      Visibility.PRIVATE -> singleRepoVisibility.load(R.drawable.ic_visibility_off)
    }
  }

  private fun FragmentSingleRepoBinding.handlePrivacy() {
    if (args.value.singleRepo?.privacy == true) {
      singleRepoPrivacy.load(R.drawable.ic_private)
    } else {
      singleRepoPrivacy.load(R.drawable.ic_public)
    }
  }

  private fun handleOpenUrl(url: String?) {
    if (url != null && URLUtil.isValidUrl(url)) {
      activity?.openChromeTab(url)
    } else {
      Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
    }
  }
}