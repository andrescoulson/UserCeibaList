package com.andrescoulson.userceibalist.di

import com.andrescoulson.userceibalist.data.resources.remote.api.UserApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ApiModule {

    @Provides
    fun provideMovieApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}