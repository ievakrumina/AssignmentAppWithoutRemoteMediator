package com.ik.android.assignmentappwithoutremotemediator.di

import com.ik.android.assignmentappwithoutremotemediator.data.repository.MainRepository
import com.ik.android.assignmentappwithoutremotemediator.data.repository.MainRepositoryImpl
import com.ik.android.assignmentappwithoutremotemediator.data.service.RepoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.flowOf
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

  @Singleton
  @Provides
  fun provideMainRepositoryImpl(service: RepoApiService) = MainRepositoryImpl(service) as MainRepository
}