package online.courseal.courseal_android

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.serialization.kotlinx.json.json
import online.courseal.courseal_android.data.api.UserCookiesStorage
import online.courseal.courseal_android.data.database.CoursealDatabase
import online.courseal.courseal_android.data.database.dao.UserDao
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

    @Singleton
    @Provides
    fun provideHttpClient(userDao: UserDao) = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        install(HttpCookies) {
            storage = UserCookiesStorage(userDao)
        }
    }
}