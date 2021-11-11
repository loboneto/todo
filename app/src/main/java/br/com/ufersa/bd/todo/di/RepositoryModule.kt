package br.com.ufersa.bd.todo.di

import br.com.ufersa.bd.todo.domain.repository.SubtaskRepository
import br.com.ufersa.bd.todo.domain.repository.SubtaskRepositoryImpl
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
    abstract fun bindsTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Singleton
    @Binds
    abstract fun bindsSubtaskRepository(
        subtaskRepositoryImpl: SubtaskRepositoryImpl
    ): SubtaskRepository
}
