package com.ik.android.assignmentappwithoutremotemediator.di

import com.ik.android.assignmentappwithoutremotemediator.common.Constants
import com.ik.android.assignmentappwithoutremotemediator.data.service.RepoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Singleton
  @Provides
  fun provideRepoApiInterceptor() = Interceptor { chain ->
    val requestBuilder = chain.request().newBuilder()
    val newRequest = requestBuilder
    /*
    requestBuilder.addHeader("Authorization", "Bearer XX")
     */

    chain.proceed(newRequest.build())
  }

  @Singleton
  @Provides
  fun provideHttpClient(interceptor: Interceptor) : OkHttpClient {
    val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    return OkHttpClient.Builder()
      .addInterceptor(logger)
      .addInterceptor(interceptor)
      .build()
  }

  @Singleton
  @Provides
  fun provideRetrofit(client: OkHttpClient): Retrofit =
    Retrofit.Builder()
      .baseUrl(Constants.BASE_URL)
      .client(client)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

  @Singleton
  @Provides
  fun provideRepoApiService(retrofit: Retrofit): RepoApiService =
    retrofit.create(RepoApiService::class.java)
}