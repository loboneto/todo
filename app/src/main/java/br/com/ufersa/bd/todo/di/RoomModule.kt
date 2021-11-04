package br.com.ufersa.bd.todo.di

import android.content.Context
import androidx.room.Room
import br.com.ufersa.bd.todo.domain.local.TasksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context): TasksDatabase {
        return Room
            .databaseBuilder(context, TasksDatabase::class.java, "Tasks.db")
            .build()
    }
}
