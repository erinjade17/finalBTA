package com.example.budgettrackerapp


import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgettrackerapp.databinding.FragmentLoginBinding
import com.example.budgettrackerapp.ui.theme.BudgetTrackerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BudgetTrackerAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login", // Set login as the start destination
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable("login") {
                        AndroidView(
                            factory = { context ->
                                val binding = FragmentLoginBinding.inflate(LayoutInflater.from(context))
                                binding.root
                            },
                            update = { /* No need to update directly here */ },
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    // Other composable destinations will be defined here as needed
                    // composable("home") { /* ... */ }
                    // composable("register") { /* ... */ }
                }
            }
        }
    }
}

