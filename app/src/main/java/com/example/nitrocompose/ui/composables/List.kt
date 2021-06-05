package com.example.nitrocompose.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.google.gson.JsonObject
import mondia.artifact.rendering.dynamicComposition
import mondia.artifact.rendering.modifier

@Composable
fun HorizontalListWithHeader(data: JsonObject) {
    Column(modifier = Modifier.dynamicComposition(data.modifier)) {
        val customProperties = data["customProperties"]?.asJsonObject
        ListHeader(listCustomProperties = customProperties)
        val walletItems = data["wallet"]?.asJsonArray?.toList()?.map { it as JsonObject }
        GamesList(games = walletItems)
    }
}

private fun Modifier.testModifier(): Modifier {
    return this.padding(0.dp, 100.dp, 0.dp, 0.dp)
}

@Composable
fun ListHeader(listCustomProperties: JsonObject?) {
    if (listCustomProperties == null) return
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
    ) {
        listCustomProperties["title"]?.asString?.let {
            Text(text = it, modifier = Modifier.align(Alignment.CenterStart))
        }
        listCustomProperties["moreButton"]?.asJsonObject?.let { buttonProperties ->
            buttonProperties["title"]?.asString?.let { title ->
                TextButton(onClick = {}, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(text = title)
                }
            }
        }
    }
}

@Composable
fun GamesList(games: List<JsonObject>?) {
    if (games.isNullOrEmpty()) return
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
    ) {
        items(games) { game -> GameItem(game = game["article"]?.asJsonObject) }
    }
}

@Composable
fun GameItem(game: JsonObject?) {
    if (game == null) return
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageUrl = game["imageUrl"]?.asString?.let { composeImageUrl(it) }
        Image(
            painter = rememberCoilPainter(request = imageUrl),
            contentDescription = "gameImage",
            modifier = Modifier.size(100.dp, 100.dp)
        )
        game["title"]?.asString?.let { title ->
            Text(
                text = title,
                modifier = Modifier.width(100.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VerticalList(data: JsonObject, content: @Composable () -> Unit) {
    Column { content() }
}

private fun composeImageUrl(unformattedUrl: String): String {
    val formattedUrl = unformattedUrl.replace("{width}", "100")
        .replace("{height}", "100")
        .replace("{suffix}", "png")
    return "https:$formattedUrl"
}