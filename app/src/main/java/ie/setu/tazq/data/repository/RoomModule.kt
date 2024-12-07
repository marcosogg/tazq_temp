package ie.setu.tazq.data.repository

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ie.setu.tazq.data.room.AppDatabase
import ie.setu.tazq.data.room.TaskDAO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTaskDAO(appDatabase: AppDatabase): TaskDAO =
        appDatabase.getTaskDAO()

    @Provides
    fun provideTaskRepository(taskDAO: TaskDAO): TaskRepository =
        TaskRepository(taskDAO)
}
