package edu.cc231008.postly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import edu.cc231008.postly.ui.PostUI
import edu.cc231008.postly.ui.theme.PostlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostlyTheme {
                    PostUI()
            }
        }
    }
}

