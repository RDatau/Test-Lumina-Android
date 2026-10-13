package com.lumina.aichat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lumina.aichat.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    userProfile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var apiKey by remember { mutableStateOf(userProfile.geminiApiKey) }
    var textModel by remember { mutableStateOf(userProfile.textModel) }
    var useGoogleSearch by remember { mutableStateOf(userProfile.useGoogleSearch) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan & API", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val updated = userProfile.copy(
                            geminiApiKey = apiKey,
                            textModel = textModel,
                            useGoogleSearch = useGoogleSearch
                        )
                        onSave(updated)
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Simpan")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Kunci & Model AI Gemini", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it },
                label = { Text("Gemini API Key") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = textModel,
                onValueChange = { textModel = it },
                label = { Text("Model Teks (e.g. gemini-3.1-flash-lite)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gunakan Google Search Grounding", fontSize = 15.sp)
                Switch(checked = useGoogleSearch, onCheckedChange = { useGoogleSearch = it })
            }

            Button(
                onClick = {
                    val updated = userProfile.copy(
                        geminiApiKey = apiKey,
                        textModel = textModel,
                        useGoogleSearch = useGoogleSearch
                    )
                    onSave(updated)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Pengaturan")
            }
        }
    }
}
