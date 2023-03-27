package com.andrescoulson.userceibalist.di

import android.content.Context
import androidx.room.Room
import com.andrescoulson.userceibalist.data.resources.local.db.UserDatabase
import com.andrescoulson.userceibalist.data.resources.local.db.dao.PostDao
import com.andrescoulson.userceibalist.data.resources.local.db.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideUserDao(database: UserDatabase): UserDao{
        return database.userDao()
    }

    @Provides
    fun providePostDao(database: UserDatabase): PostDao{
        return database.postDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): UserDatabase {
        return Room.databaseBuilder(
            appContext,
            UserDatabase::class.java,
            "UserDatabase"
        ).build()
    }


}