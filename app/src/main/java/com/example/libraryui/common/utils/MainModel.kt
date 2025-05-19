package com.example.libraryui.common.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import com.example.libraryui.common.App
import com.example.libraryui.data.dao.BookDao
import com.example.libraryui.data.dao.DiskDao
import com.example.libraryui.data.dao.NewspaperDao
import com.example.libraryui.data.database.LibDatabase
import com.example.libraryui.data.repository.LibraryRepositoryImpl
import com.example.libraryui.domain.usecase.Interactor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn (SingletonComponent::class)
object MainModel {
    @Provides
    @Singleton
    fun provideInteractor(repository: LibraryRepositoryImpl): Interactor{
        return Interactor(repository)
    }

    @Provides
    @Singleton
    fun provideRepository(
        bookDao: BookDao,
        diskDao: DiskDao,
        newspaperDao: NewspaperDao,
        prefs: SharedPreferences
    ): LibraryRepositoryImpl{
        return LibraryRepositoryImpl(bookDao, diskDao, newspaperDao, prefs)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LibDatabase {
        val db = Room.databaseBuilder(context, LibDatabase::class.java, "library.db").build()
        return db
    }

    @Provides
    @Singleton
    fun provideBookDao(database: LibDatabase): BookDao {
        return database.bookDao()
    }
    @Provides
    @Singleton
    fun provideNewspaperDao(database: LibDatabase): NewspaperDao {
        return database.newspaperDao()
    }
    @Provides
    @Singleton
    fun provideDiskDao(database: LibDatabase): DiskDao {
        return database.diskDao()
    }

    @Provides
    @Singleton
    fun providePrefs(@ApplicationContext context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

}