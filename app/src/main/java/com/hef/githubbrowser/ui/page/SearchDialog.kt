package com.hef.githubbrowser.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Author: hefeng
 * Provider: rivotek
 * Date: 2025/06/17
 */

@Composable
fun SearchDialog(
    visible: Boolean,
    onConfirmed: (start: Boolean, key: String, language: String) -> Unit
) {
    var keyword by rememberSaveable { mutableStateOf("") }
    var language by rememberSaveable { mutableStateOf("") }

    if (visible) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 30.dp,
                        end = 20.dp,
                        bottom = 20.dp
                    )
                ) {

                    TextField(
                        value = keyword,
                        onValueChange = { newText -> keyword = newText },
                        label = { Text("Keyword") },
                        placeholder = { Text("Input keyword of the target") }
                    )

                    TextField(
                        value = language,
                        onValueChange = { newText -> language = newText },
                        label = { Text("Language") },
                        placeholder = { Text("Input kotlin, java, etc.") }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(modifier = Modifier.padding(
                            10.dp,
                            top = 10.dp,
                            end = 10.dp,
                            bottom = 0.dp
                        ),
                            onClick = { onConfirmed(false, "", "") }) {
                            Text(text = "Cancel")

                        }

                        Button(modifier = Modifier.padding(
                            10.dp,
                            top = 10.dp,
                            end = 10.dp,
                            bottom = 0.dp
                        ),
                            onClick = { onConfirmed(true, keyword, language) }) {
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}