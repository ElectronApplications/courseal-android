package online.courseal.courseal_android.ui.components.editorjs.editor

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun EditorJSEditorComponent(
    modifier: Modifier = Modifier,
    data: EditorJSContent,
    onDataChange: (EditorJSContent) -> Unit
) {
    val isDark = isSystemInDarkTheme()

    AndroidView(
        modifier = modifier
            .fillMaxSize(),
        factory = {
            return@AndroidView WebView(it).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()

                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true
                settings.setSupportZoom(false)

                addJavascriptInterface(object {
                    @JavascriptInterface
                    fun getData(): String {
                        return Json.encodeToString(data)
                    }

                    @JavascriptInterface
                    fun setData(data: String) {
                        onDataChange(Json.decodeFromString(data))
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
            it.loadUrl("file:///android_asset/editorjs.html")
        }
    )
}