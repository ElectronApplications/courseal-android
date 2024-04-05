package online.courseal.courseal_android

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import online.courseal.courseal_android.data.database.CoursealDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideCoursealDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        context = app,
        klass = CoursealDatabase::class.java,
        name = "courseal_android_db"
    ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: CoursealDatabase) = db.userDao()

    @Singleton
    @Provides
    fun provideServerDao(db: CoursealDatabase) = db.serverDao()

}