package online.courseal.courseal_android.ui.components.editorjs.editor

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onDataChange: (EditorJSContent) -> Unit,
    saveEndpoint: String
) {
    val isDark = isSystemInDarkTheme()

    var pickCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
        pickCallback?.onReceiveValue(it?.let { result -> arrayOf(result) })
    }

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
                settings.allowFileAccess = true
                settings.allowContentAccess = true

                webChromeClient = object: WebChromeClient() {
                    override fun onShowFileChooser(
                        webView: WebView?,
                        filePathCallback: ValueCallback<Array<Uri>>?,
                        fileChooserParams: FileChooserParams?
                    ): Boolean {
                        pickCallback = filePathCallback
                        pickMedia.launch(PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly))
                        return true
                    }
                }

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
                    fun getSaveEndpoint(): String {
                        return saveEndpoint
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