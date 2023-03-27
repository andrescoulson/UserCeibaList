package com.andrescoulson.userceibalist.di

import com.andrescoulson.userceibalist.data.repository.UserRepositoryImpl
import com.andrescoulson.userceibalist.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository
}