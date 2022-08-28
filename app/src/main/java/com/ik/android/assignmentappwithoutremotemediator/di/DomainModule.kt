package com.ik.android.assignmentappwithoutremotemediator.di

import com.ik.android.assignmentappwithoutremotemediator.data.paging.RepoListPagingSource
import com.ik.android.assignmentappwithoutremotemediator.domain.RepoListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

  @Singleton
  @Provides
  fun provideRepoListUseCase(pagingData: RepoListPagingSource) = RepoListUseCase(pagingData)
}