package jetzt.jfdi.skyxr.ui.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import jetzt.jfdi.skyxr.R
import jetzt.jfdi.skyxr.ui.shared.viewmodel.SkyPost

@Composable
fun MainContent(
    modifier: Modifier,
    posts: List<SkyPost>,
    searchTerm: String,
    loading: Boolean,
    onSearchClicked: (searchTerm: String) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier) {
            item {
                SearchBar(
                    searchTerm,
                    onSearchClicked
                )
            }

            items(posts) { post ->
                SkyPostView(
                    post = post
                )
            }
        }

        if (loading) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .align(
                        Alignment.Companion.Center
                    ), horizontalAlignment = Alignment.Companion.CenterHorizontally

            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp)
                )

                Text(text = ".... Loading ....")
            }
        }
    }
}

@Preview
@Composable
private fun MainContentPreview() {
    MainContent(
        modifier = Modifier,
        posts = listOf(
            SkyPost(
                "PETEP",
                "petethesteet",
                "https://http.cat/202",
                "Hello, I'm PETE!",
                "https://http.cat/501"
            )
        ),
        searchTerm = "cats",
        loading = true,
        onSearchClicked = {

        },
    )
}

@Composable
fun SearchBar(
    searchTerm: String,
    onSearchClicked: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val tempSearchTerm: MutableState<String> = remember { mutableStateOf(searchTerm) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = tempSearchTerm.value,
        onValueChange = { tempSearchTerm.value = it },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Companion.Default.copy(imeAction = ImeAction.Companion.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearchClicked(tempSearchTerm.value)
            }
        ),
        trailingIcon = {
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = {
                    keyboardController?.hide()
                    onSearchClicked(tempSearchTerm.value)
                },
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null
                )
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun SearchBarPreview() {
    SearchBar(
        "cats",
        {}
    )
}
