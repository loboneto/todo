package br.com.ufersa.bd.todo.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.ufersa.bd.todo.domain.local.TasksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context): TasksDatabase {
        return Room
            .databaseBuilder(context, TasksDatabase::class.java, "Tasks.db")
            .addCallback(trigger)
            .build()
    }

    private val trigger = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.execSQL("CREATE TRIGGER IF NOT EXISTS delete_trigger AFTER INSERT ON User " +
                    "WHEN (SELECT COUNT(*) FROM User)>3 " +
                    "BEGIN " +
                    "DELETE FROM User WHERE id NOT IN(SELECT id FROM User ORDER BY id DESC LIMIT 5); " +
                    "END")
        }
    }
}
