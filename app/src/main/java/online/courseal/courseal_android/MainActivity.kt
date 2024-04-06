package online.courseal.courseal_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import online.courseal.courseal_android.data.database.dao.UserDao
import online.courseal.courseal_android.ui.theme.CoursealTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Might make it non-blocking later
        val currentUser = runBlocking { userDao.getCurrentUser() }
        val users = runBlocking { userDao.getAllUsers() }

        val startDestination = if (currentUser != null) {
            Routes.MAIN
        } else if (users.isNotEmpty()) {
            Routes.WELCOME /* TODO */
        } else {
            Routes.WELCOME
        }

        setContent {
            CoursealTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    MainApp(startDestination)
                }
            }
        }
    }
}