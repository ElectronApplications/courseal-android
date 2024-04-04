package online.courseal.courseal_android.ui.components

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat
import online.courseal.courseal_android.ui.theme.LocalCoursealPalette

@Composable
fun TextHTML(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = LocalTextStyle.current,
    color: Color = MaterialTheme.colorScheme.onBackground
) {
    val spannableString = SpannableStringBuilder(text.replace("<br>", "")).toString()
    val spanned = HtmlCompat.fromHtml(spannableString, HtmlCompat.FROM_HTML_MODE_COMPACT)
    val annotated = spanned.toAnnotatedString(linkColor = LocalCoursealPalette.current.link)

    val uriHandler = LocalUriHandler.current
    ClickableText(
        modifier = modifier,
        text = annotated,
        style = style.copy(
            color = color
        ),
        onClick = {
            annotated
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    val url = if(stringAnnotation.item.startsWith("http://") || stringAnnotation.item.startsWith("https://"))
                        stringAnnotation.item
                    else "https://${stringAnnotation.item}"

                    uriHandler.openUri(url)
                }
        }
    )
}

fun Spanned.toAnnotatedString(linkColor: Color = Color.Unspecified): AnnotatedString = buildAnnotatedString {
    val spanned = this@toAnnotatedString
    append(spanned.toString())
    getSpans(0, spanned.length, Any::class.java).forEach { span ->
        val start = getSpanStart(span)
        val end = getSpanEnd(span)
        when (span) {
            is StyleSpan -> when (span.style) {
                Typeface.BOLD -> addStyle(SpanStyle(fontWeight = FontWeight.Bold), start, end)
                Typeface.ITALIC -> addStyle(SpanStyle(fontStyle = FontStyle.Italic), start, end)
                Typeface.BOLD_ITALIC -> addStyle(SpanStyle(fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic), start, end)
            }
            is UnderlineSpan -> addStyle(SpanStyle(textDecoration = TextDecoration.Underline), start, end)
            is URLSpan -> {
                addStyle(SpanStyle(color = linkColor), start, end)
                addStringAnnotation("URL",  span.url, start, end)
            }
        }
    }
}