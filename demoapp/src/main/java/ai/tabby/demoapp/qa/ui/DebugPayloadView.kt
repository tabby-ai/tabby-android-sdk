package ai.tabby.demoapp.qa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Reformats a data class's generated `toString()` — `Foo(a=1, b=Bar(c=2))` — into an indented,
 * multi-line block so nested SDK payloads (TabbyPayment, TabbySession, TabbyResult, ...) are
 * readable on screen without hand-writing a pretty-printer per type.
 */
fun Any?.toPrettyString(): String {
    if (this == null) return "null"
    val raw = toString()
    val sb = StringBuilder()
    var indent = 0
    var i = 0
    fun newline() {
        sb.append('\n')
        repeat(indent) { sb.append("  ") }
    }
    while (i < raw.length) {
        when (val c = raw[i]) {
            '(', '[' -> {
                sb.append(c)
                indent++
                if (i + 1 < raw.length && raw[i + 1] != ')' && raw[i + 1] != ']') newline()
            }

            ')', ']' -> {
                indent = (indent - 1).coerceAtLeast(0)
                if (raw[i - 1] != '(' && raw[i - 1] != '[') newline()
                sb.append(c)
            }

            ',' -> {
                sb.append(c)
                newline()
                if (i + 1 < raw.length && raw[i + 1] == ' ') i++
            }

            else -> sb.append(c)
        }
        i++
    }
    return sb.toString()
}

/**
 * Shows the raw string form of an SDK callback/response payload with a copy-to-clipboard action —
 * used to visually verify exactly what the SDK returned, e.g. after a checkout result.
 */
@Composable
fun DebugPayloadView(
    title: String,
    payload: Any?,
    modifier: Modifier = Modifier,
    maxHeight: androidx.compose.ui.unit.Dp = 220.dp,
) {
    val clipboard = LocalClipboardManager.current
    val text = payload.toPrettyString()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.05f),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                TextButton(onClick = { clipboard.setText(AnnotatedString(text)) }) {
                    Text("Copy")
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            SelectionContainer {
                Text(
                    text = text,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(maxHeight)
                        .background(MaterialTheme.colors.background)
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                )
            }
        }
    }
}
