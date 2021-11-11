package br.com.ufersa.bd.todo.di

import br.com.ufersa.bd.todo.domain.source.SubtaskDataSource
import br.com.ufersa.bd.todo.domain.source.SubtaskDataSourceImpl
import br.com.ufersa.bd.todo.domain.source.TaskDataSource
import br.com.ufersa.bd.todo.domain.source.TaskDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindsTaskDataSource(
        taskDataSourceImpl: TaskDataSourceImpl
    ): TaskDataSource

    @Singleton
    @Binds
    abstract fun bindsSubtaskDataSource(
        subtaskDataSourceImpl: SubtaskDataSourceImpl
    ): SubtaskDataSource
}
