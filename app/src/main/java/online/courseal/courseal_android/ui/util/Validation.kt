package online.courseal.courseal_android.ui.util

fun validateUsertag(usertag: String): Boolean {
    return Regex("[a-z0-9-_.]+").matches(usertag)
}