package edu.cc231008.postly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import edu.cc231008.postly.data.repo.PostTemplate

enum class Routes(val route: String) {
    Main("main"),
    Insert("insert")
}

@Composable
fun PostUI(
    navController: NavHostController = rememberNavController(),
    postViewModel: PostViewModel = viewModel(factory = AppViewModelProvider.Factory)
           ) {

    val state by postViewModel.postUiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Routes.Main.route
    ) {
        composable(Routes.Main.route) {
            Column(modifier = Modifier.fillMaxSize()) {
                Button(
                    onClick = { navController.navigate(Routes.Insert.route) },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Add Post")
                }
                LazyColumn {
                    itemsIndexed(state) { index, posts ->
                        ListOfPosts(posts)
                    }
                }
            }
        }
        composable(Routes.Insert.route) {
            AddPost(
                onAddPost = { navController.popBackStack() }
            )
        }
    }
}

@Composable
fun AddPost(
    postViewModel: PostViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddPost: () -> Unit
) {
    var imageUrl by remember {mutableStateOf("")}
    var description by remember {mutableStateOf("")}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        InputField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = "Image URL"
        )

        Spacer(modifier = Modifier.height(8.dp))

        InputField(
            value = description,
            onValueChange = { description = it },
            label = "Description"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                postViewModel.onAddButtonClicked(imageUrl, description)
                onAddPost()
            }
        )
        {
            Text("Submit Input")
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun DisplayImage(url: String) {
    if (url.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = "Loaded Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun DisplayDescription(description: String) {
    Text(
        text = description,
        modifier = Modifier.padding(top = 8.dp),
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun ListOfPosts(posts: PostTemplate, modifier: Modifier = Modifier) {
    OutlinedCard(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Column(Modifier.padding(16.dp)) {
            DisplayImage(posts.image)
            DisplayDescription(posts.description)
        }
    }
}