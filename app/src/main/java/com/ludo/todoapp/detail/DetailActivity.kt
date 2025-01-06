package com.ludo.todoapp.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.copy
import com.ludo.todoapp.detail.ui.theme.TodoAppLudoTheme
import com.ludo.todoapp.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val task = intent.getSerializableExtra("task") as? Task

        setContent {
            TodoAppLudoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Detail(
                        modifier = Modifier.padding(innerPadding),
                        initialTask = task,
                        onValidate = { updatedTask ->
                            val resultIntent = Intent().apply {
                                putExtra("task", updatedTask)
                            }
                            setResult(RESULT_OK, resultIntent) // Pass the updated task back
                            finish() // Close the activity
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Detail(
    modifier: Modifier = Modifier,
    initialTask: Task?,
    onValidate: (Task) -> Unit
) {
    val mutableTask = remember { mutableStateOf(initialTask ?: Task(UUID.randomUUID().toString(), "New Task!")) }

    Column(modifier = modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(text = "Task Detail", style = MaterialTheme.typography.headlineLarge)

        OutlinedTextField(
            value = mutableTask.value.title,
            onValueChange = { mutableTask.value = mutableTask.value.copy(title=it) },
            label = { Text("Title") },
            modifier = modifier
        )

        OutlinedTextField(
            value = mutableTask.value.description,
            onValueChange = { mutableTask.value = mutableTask.value.copy(description=it) },
            label = { Text("Description") },
            modifier = modifier
        )

        Button(onClick = {
            onValidate(mutableTask.value)
        }) {
            Text("Validate")
        }
    }
}

