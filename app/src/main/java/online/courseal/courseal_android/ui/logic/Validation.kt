package online.courseal.courseal_android.ui.logic

fun validateUsertag(usertag: String): Boolean {
    return Regex("[a-z0-9-_.]+").matches(usertag)
}