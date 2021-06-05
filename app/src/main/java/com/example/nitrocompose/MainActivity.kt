package com.example.nitrocompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.nitrocompose.ui.composables.TemplatesDrawer
import com.example.nitrocompose.ui.theme.NitroComposeTheme
import com.google.accompanist.coil.rememberCoilPainter
import com.google.gson.Gson
import mondia.artifact.rendering.DynamicRenderer
import mondia.artifact.rendering.StructureRetriever
import mondia.artifact.rendering.UIStructure
import mondia.artifact.rendering.ViewStateProvider
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {

    private val dynamicRenderer: DynamicRenderer by lazy {
        DynamicRenderer(TemplatesDrawer(), object : StructureRetriever {
            override suspend fun getStructure(url: String): UIStructure? {
                return getUIStructure()
            }
        }, object : ViewStateProvider {
            override val loadingView: @Composable () -> Unit
                get() = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) { CircularProgressIndicator() }
                }
            override val errorView: @Composable () -> Unit
                get() = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) { Text(text = "Error") }
                }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NitroComposeTheme {
                dynamicRenderer.getDrawableWithState("", lifecycleScope).invoke()
            }
        }
    }


    private fun getUIStructure(): UIStructure {
        val reader = BufferedReader(InputStreamReader(assets.open("NestedTwoHorizontalLists.json")))
        return Gson().fromJson(reader, UIStructure::class.java)
    }
}

@Composable
fun MyScreenContent() {
    Column {
        TopAppBar {}
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
            )
            StdPaddedContent { SubscriptionCard() }
            Divider()
            GamesList()
            Divider()
            StdPaddedContent {
                Column {
                    for (i in 1..5) {
                        ProfileOption()
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileOption() {
    TextButton(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp, vertical = 10.dp)
    ) {
        Text(text = "My Subscription", modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.Share, contentDescription = "")
    }
}

@Composable
fun SubscriptionCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.53f)
    ) {
        Image(
            painter = rememberCoilPainter(request = "http://i.mondiamedia.com/int/mm-games/orange-es-games/Group.png"),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Please Subscription lw sm7t gdn")
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Subscribe")
            }
        }
    }
}

@Composable
fun GamesList() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(List(100) { "Game Name" }) {
            GameItem(gameName = it)
        }
    }
}

@Composable
fun GameItem(gameName: String) {
    Column {
        Image(
            painter = rememberCoilPainter(request = ""),
            contentDescription = "",
            modifier = Modifier.size(100.dp, 100.dp)
        )
        Text(text = gameName, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun StdPaddedContent(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
        content()
    }
}


@Composable
fun CounterText() {
    Column {
        var counter by remember { mutableStateOf(0) }
        Text(text = "$counter")
        Button(onClick = { counter++ }) {

        }
    }
}

@Composable
fun HelloContent() {
    Column(modifier = Modifier.padding(16.dp)) {
        var name by remember { mutableStateOf("") }
        if (name.isNotEmpty()) {
            Text(
                text = "Hello, $name!",
                modifier = Modifier.padding(bottom = 8.dp),
                style = MaterialTheme.typography.h5
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NitroComposeTheme {
        MyScreenContent()
    }
}