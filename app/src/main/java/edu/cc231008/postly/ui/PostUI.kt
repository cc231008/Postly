package edu.cc231008.postly.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.rememberAsyncImagePainter
import edu.cc231008.postly.data.repo.PostTemplate
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//Routes for navigation
enum class Routes(val route: String) {
    Main("main"),
    Insert("insert"),
    Edit("edit/{postId}")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostUI(
    navController: NavHostController = rememberNavController(),
           ) {

    Scaffold(
        topBar = {
            // TopAppBar to display title and the "Add" button
            TopAppBar(
                title = {
                    Text(
                        text = "Postly",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold, // Bold for emphasis
                            fontSize = 20.sp, // Customize the font size as needed
                        )
                    )
                },
                actions = {
                    //This button will navigate user to the screen where he can add a post.
                    IconButton(
                        onClick = { navController.navigate(Routes.Insert.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Post",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.Main.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Routes.Main.route) {
                Column(modifier = Modifier.fillMaxSize()) {
                    //ListOfPosts shows the list of posts in the column.
                    ListOfPosts { postId -> navController.navigate("edit/$postId") }
                }
            }
            composable(Routes.Insert.route) {
                AddPostScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(
                Routes.Edit.route,
                listOf(navArgument("postId")
                { type = NavType.IntType }
                )) {
                EditPostScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun ListOfPosts(
    //PostViewModel includes updated information about user's last state.
    postViewModel: PostViewModel = viewModel(factory = AppViewModelProvider.Factory),
    //Lambda function that expects to fetch an id of a particular post for navigation purposes.
    onPostClick: (Int) -> Unit
) {

    val state by postViewModel.postUiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        itemsIndexed(state) { index, post ->
            PostCard(
                post,
                onEditClick = { onPostClick(post.id)},
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

//A function that contains a structure of a post.
@Composable
fun PostCard(
    posts: PostTemplate,
    //Lambda function for navigation.
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val createdAt = formatTimestamp(posts.createdAt)

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Ensures spacing between items
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Added: $createdAt",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                IconButton(
                    onClick = { onEditClick() },
                    modifier = Modifier
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
                        .clip(MaterialTheme.shapes.small)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Post",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            DisplayImage(posts.image)

            Spacer(modifier = Modifier.height(8.dp))

            DisplayDescription(posts.description)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AddPostScreen(
    //PostViewModel contains onAddPost function that will help to Insert a post in database.
    postViewModel: PostViewModel = viewModel(factory = AppViewModelProvider.Factory),
    //Lambda function helps to navigate back, when post is created.
    onNavigateBack: () -> Unit
) {
    //Variables that will contain data about a new post.
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
                postViewModel.onAddButtonClick(imageUrl, description)
                onNavigateBack()
            }
        )
        {
            Text("Submit Input")
        }

    }
}

@Composable
fun EditPostScreen(
    postStateViewModel: PostStateViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit
) {
    var imageUrl by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


    val state = postStateViewModel.postUiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.value.post.id) {
        imageUrl = state.value.post.image
        description = state.value.post.description
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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
                postStateViewModel.onEditButtonClick(PostTemplate(
                    id = state.value.post.id,
                    image = imageUrl,
                    description = description,
                    createdAt = state.value.post.createdAt,
                ))
                onNavigateBack()
            }
        ) {
            Text("Edit")
        }

        DeletePostButton(
          onDeleteClick = {
              postStateViewModel.onDeleteButtonClick(state.value.post)
              onNavigateBack()
          }
        )
    }
}

@Composable
fun DeletePostButton(
    onDeleteClick: () -> Unit
) {
    Button(
        onClick = { onDeleteClick() }
    ) {
        Text("Delete")
    }
}

//A reusable function that serves as Input Field
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

//A reusable function that helps to display images by a given URL
@Composable
fun DisplayImage(url: String) {
    if (url.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(url),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Image Available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

//A reusable function that helps to display text of the description.
@Composable
fun DisplayDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 8.dp)
    )
}

//As time of post's creation is originally received in milliseconds, formatTimestamp helps to format milliseconds into date.
fun formatTimestamp(timestamp: Long): String {
    //If timestamp is 1672531199000, instant would represent 2023-01-01T00:00:00Z.
    val instant = Instant.ofEpochMilli(timestamp)
    //Uses pattern to display in certain way
    val formatter = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm")
        .withZone(ZoneId.systemDefault())

    return formatter.format(instant)
}



