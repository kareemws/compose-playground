package com.example.nitrocompose.ui.composables

import androidx.compose.runtime.Composable
import com.google.gson.JsonObject
import mondia.artifact.rendering.UIComponentMapper

class TemplatesDrawer : UIComponentMapper {

    override fun isDrawable(name: String) {
    }

    @Composable
    override fun Draw(name: String, data: JsonObject, content: @Composable () -> Unit) {
        when (name) {
            "android.horizontal_list_with_header" -> HorizontalListWithHeader(data = data)
        }
    }

    override fun getDynamicTemplate(
        name: String,
        data: JsonObject,
        content: @Composable () -> Unit
    ): (@Composable () -> Unit)? = when (name) {
        "android.horizontal_list_with_header" -> {
            { HorizontalListWithHeader(data = data) }
        }
        "android.vertical_list" -> {
            { VerticalList(data = data, content = content) }
        }
        else -> null
    }
}