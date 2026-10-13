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
fun UserProfileScreen(
    userProfile: UserProfile,
    onSave: (UserProfile) -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(userProfile.name) }
    var personalityInfo by remember { mutableStateOf(userProfile.personalityInfo) }
    var profilePic by remember { mutableStateOf(userProfile.profilePic ?: "") }
    var currentOutfit by remember { mutableStateOf(userProfile.currentOutfit) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil Pengguna (Sayang)", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val updated = userProfile.copy(
                            name = name.ifBlank { "Sayang" },
                            personalityInfo = personalityInfo,
                            profilePic = profilePic.ifBlank { null },
                            currentOutfit = currentOutfit
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
                label = { Text("Nama Panggilan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = profilePic,
                onValueChange = { profilePic = it },
                label = { Text("URL Foto Profil Pengguna") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = currentOutfit,
                onValueChange = { currentOutfit = it },
                label = { Text("Pakaian / Kostum Pengguna") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = personalityInfo,
                onValueChange = { personalityInfo = it },
                label = { Text("Tentang Pengguna (Bio)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Button(
                onClick = {
                    val updated = userProfile.copy(
                        name = name.ifBlank { "Sayang" },
                        personalityInfo = personalityInfo,
                        profilePic = profilePic.ifBlank { null },
                        currentOutfit = currentOutfit
                    )
                    onSave(updated)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Simpan Profil")
            }
        }
    }
}
