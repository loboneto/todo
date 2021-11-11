package br.com.ufersa.bd.todo.di

import br.com.ufersa.bd.todo.domain.repository.TaskRepository
import br.com.ufersa.bd.todo.domain.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsTaskRepository(taskRepositoryImpl: TaskRepositoryImpl): TaskRepository
}
