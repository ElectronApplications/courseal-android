package online.courseal.courseal_android

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSCode
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSCodeData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSContent
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSDelimiter
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeader
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeaderData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSHeaderLevel
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSList
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSListData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSListStyle
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSParagraph
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSParagraphData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSQuote
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSQuoteAlignment
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSQuoteData
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSWarning
import online.courseal.courseal_android.data.coursedata.editorjs.EditorJSWarningData
import org.junit.Assert
import org.junit.Test

class EditorJSTest {
    private val sampleContent = EditorJSContent(
        time = 1711507166253,
        version = "2.29.1",
        blocks = listOf(
            EditorJSHeader(
                id = "oi2FNWysnU",
                data = EditorJSHeaderData(
                    text = "Sample Header h1",
                    level = EditorJSHeaderLevel.H1
                )
            ),
            EditorJSHeader(
                id = "c03rFjLjll",
                data = EditorJSHeaderData(
                    text = "Sample Header h6",
                    level = EditorJSHeaderLevel.H6
                )
            ),
            EditorJSParagraph(
                id = "Y0FC-KXE9f",
                data = EditorJSParagraphData(
                    text = "Sample text<br>"
                )
            ),
            EditorJSParagraph(
                id = "nNEesxxc6H",
                data = EditorJSParagraphData(
                    text = "<i>Sample italic text</i><br>"
                )
            ),
            EditorJSList(
                id = "O6yNYMO2HR",
                data = EditorJSListData(
                    style = EditorJSListStyle.ORDERED,
                    items = listOf(
                        "ordered",
                        "list"
                    )
                )
            ),
            EditorJSList(
                id = "PodGxsipV_",
                data = EditorJSListData(
                    style = EditorJSListStyle.UNORDERED,
                    items = listOf(
                        "unordered",
                        "list"
                    )
                )
            ),
            EditorJSDelimiter(
                id = "SSIiQM8QW_",
                data = Unit
            ),
            EditorJSQuote(
                id = "3iWyZzWbmz",
                data = EditorJSQuoteData(
                    text = "A quote<br>",
                    caption = "caption",
                    alignment = EditorJSQuoteAlignment.LEFT
                )
            ),
            EditorJSWarning(
                id = "QK8ZIRhemb",
                data = EditorJSWarningData(
                    title = "WARNING",
                    message = "sample message"
                )
            ),
            EditorJSCode(
                id = "h9bxessZnC",
                data = EditorJSCodeData(
                    code = "print(\"Hello World!)"
                )
            )
        )
    )

    private val sampleJson = """
            {
              "time": 1711507166253,
              "blocks": [
                {
                  "id": "oi2FNWysnU",
                  "type": "header",
                  "data": { "text": "Sample Header h1", "level": 1 }
                },
                {
                  "id": "c03rFjLjll",
                  "type": "header",
                  "data": { "text": "Sample Header h6", "level": 6 }
                },
                {
                  "id": "Y0FC-KXE9f",
                  "type": "paragraph",
                  "data": { "text": "Sample text<br>" }
                },
                {
                  "id": "nNEesxxc6H",
                  "type": "paragraph",
                  "data": { "text": "<i>Sample italic text</i><br>" }
                },
                {
                  "id": "O6yNYMO2HR",
                  "type": "list",
                  "data": { "style": "ordered", "items": ["ordered", "list"] }
                },
                {
                  "id": "PodGxsipV_",
                  "type": "list",
                  "data": { "style": "unordered", "items": ["unordered", "list"] }
                },
                { "id": "SSIiQM8QW_", "type": "delimiter", "data": {} },
                {
                  "id": "3iWyZzWbmz",
                  "type": "quote",
                  "data": {
                    "text": "A quote<br>",
                    "caption": "caption",
                    "alignment": "left"
                  }
                },
                {
                  "id": "QK8ZIRhemb",
                  "type": "warning",
                  "data": { "title": "WARNING", "message": "sample message" }
                },
                {
                  "id": "h9bxessZnC",
                  "type": "code",
                  "data": { "code": "print(\"Hello World!)" }
                }
              ],
              "version": "2.29.1"
            }
        """

    @Test
    fun `check that deserialization works correctly`() {
        val deserialized = Json.decodeFromString<EditorJSContent>(sampleJson)
        Assert.assertEquals(sampleContent, deserialized)
    }

    @Test
    fun `check that serialization works correctly`() {
        val serialized = Json.encodeToString(sampleContent)

        // Gotta deserialize anyway - string comparison won't work
        val deserializedJson = Json.decodeFromString<EditorJSContent>(sampleJson)
        val deserializedSerialized = Json.decodeFromString<EditorJSContent>(serialized)

        Assert.assertEquals(deserializedJson, deserializedSerialized)
    }
}