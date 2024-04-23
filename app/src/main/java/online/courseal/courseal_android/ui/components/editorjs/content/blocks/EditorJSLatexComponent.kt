package online.courseal.courseal_android.ui.components.editorjs.content.blocks

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSLatexData

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EditorJSLatexComponent(
    modifier: Modifier = Modifier,
    data: EditorJSLatexData
) {
    val isDark = isSystemInDarkTheme()

    AndroidView(
        modifier = modifier,
        factory = {
            return@AndroidView WebView(it).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(false)

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun getFormula(): String {
                        return data.math
                    }

                    @JavascriptInterface
                    fun isDark(): Boolean {
                        return isDark
                    }
                }, "Android")

                setBackgroundColor(Color.Transparent.toArgb())
            }
        },
        update = {
            it.loadUrl("file:///android_asset/latex.html")
        }
    )
}