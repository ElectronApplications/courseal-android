package online.courseal.courseal_android.ui.components.editorjs.content

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.launch
import online.courseal.courseal_android.data.editorjs.EditorJSLatexData

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
                this.addJavascriptInterface(object {
                    @JavascriptInterface
                    fun getFormula(): String {
                        return data.math
                    }

                    @JavascriptInterface
                    fun isDark(): Boolean {
                        return isDark
                    }
                }, "Android")
                this.setBackgroundColor(Color.Transparent.toArgb())
            }
        },
        update = {
            it.loadUrl("file:///android_asset/latex.html")
        }
    )
}