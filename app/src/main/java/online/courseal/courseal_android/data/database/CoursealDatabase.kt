package online.courseal.courseal_android.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import online.courseal.courseal_android.data.database.dao.ServerDao
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.data.database.entities.CurrentUser
import online.courseal.courseal_android.data.database.entities.Server
import online.courseal.courseal_android.data.database.entities.User
import online.courseal.courseal_android.data.database.entities.UserCookie

@Database(entities = [
        User::class,
        CurrentUser::class,
        UserCookie::class,
        Server::class
    ],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 2, to = 3)
    ]
)
@TypeConverters(Converters::class)
abstract class CoursealDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun serverDao(): ServerDao
}