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
import com.lumina.aichat.model.AgentConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterWizardScreen(
    agent: AgentConfig?,
    onSave: (AgentConfig) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(agent?.name ?: "") }
    var personality by remember { mutableStateOf(agent?.personality ?: "") }
    var profilePic by remember { mutableStateOf(agent?.profilePic ?: "") }
    var outfit by remember { mutableStateOf(agent?.currentOutfit ?: "Casual") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (agent == null) "Buat Karakter AI" else "Edit Karakter AI", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val updated = AgentConfig(
                            id = agent?.id ?: System.currentTimeMillis().toString(),
                            name = name.ifBlank { "Lumina Companion" },
                            personality = personality.ifBlank { "Sahabat AI yang setia." },
                            profilePic = profilePic.ifBlank { null },
                            currentOutfit = outfit
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
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Karakter") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = profilePic,
                onValueChange = { profilePic = it },
                label = { Text("URL Foto Profil (Avatar)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = outfit,
                onValueChange = { outfit = it },
                label = { Text("Pakaian / Kostum Saat Ini") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = personality,
                onValueChange = { personality = it },
                label = { Text("Deskripsi Kepribadian (System Instruction)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
            )

            Button(
                onClick = {
                    val updated = AgentConfig(
                        id = agent?.id ?: System.currentTimeMillis().toString(),
                        name = name.ifBlank { "Lumina Companion" },
                        personality = personality.ifBlank { "Sahabat AI yang setia." },
                        profilePic = profilePic.ifBlank { null },
                        currentOutfit = outfit
                    )
                    onSave(updated)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Karakter")
            }
        }
    }
}
