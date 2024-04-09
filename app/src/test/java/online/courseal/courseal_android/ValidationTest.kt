package online.courseal.courseal_android

import online.courseal.courseal_android.ui.logic.validateUsertag
import org.junit.Assert
import org.junit.Test

class ValidationTest {

    @Test
    fun `check simple usertag`() {
        Assert.assertEquals(true, validateUsertag("username"))
    }

    @Test
    fun `check valid usertag with symbols`() {
        Assert.assertEquals(true, validateUsertag("who_makes.such-usertags123"))
    }

    @Test
    fun `check invalid empty usertag`() {
        Assert.assertEquals(false, validateUsertag(""))
    }

    @Test
    fun `check invalid usertag with capital letters`() {
        Assert.assertEquals(false, validateUsertag("I_WANT_MY_USERTAG_TO_ATTRACT_ATTENTION"))
    }

    @Test
    fun `check invalid usertag with forbidden symbols`() {
        Assert.assertEquals(false, validateUsertag("hello my usertag is !@#$%^&*()"))
    }

    @Test
    fun `check invalid usertag with unicode`() {
        Assert.assertEquals(false, validateUsertag("мой_юзертег_\uD83E\uDD70"))
    }

}