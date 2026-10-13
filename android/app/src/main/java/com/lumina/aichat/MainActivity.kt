package com.lumina.aichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.lumina.aichat.data.LocalStorage
import com.lumina.aichat.model.AgentConfig
import com.lumina.aichat.model.AppState
import com.lumina.aichat.model.ChatMessage
import com.lumina.aichat.model.UserProfile
import com.lumina.aichat.ui.*
import com.lumina.aichat.ui.theme.LuminaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val storage = LocalStorage(this)

        setContent {
            LuminaTheme {
                var appState by remember { mutableStateOf(AppState.CHARACTER_CARDS) }
                var agents by remember { mutableStateOf(storage.getAgents()) }
                var userProfile by remember { mutableStateOf(storage.getUserProfile()) }
                var selectedAgent by remember { mutableStateOf(agents.firstOrNull() ?: AgentConfig()) }
                var editingAgent by remember { mutableStateOf<AgentConfig?>(null) }
                var messages by remember { mutableStateOf(storage.getMessages(selectedAgent.id)) }

                when (appState) {
                    AppState.CHARACTER_CARDS -> {
                        CharacterCardsScreen(
                            agents = agents,
                            onSelectAgent = { agent ->
                                selectedAgent = agent
                                messages = storage.getMessages(agent.id)
                                appState = AppState.CHAT
                            },
                            onStartCall = { agent ->
                                selectedAgent = agent
                                appState = AppState.CALL
                            },
                            onNavigate = { state ->
                                appState = state
                            },
                            onEditAgent = { agent ->
                                editingAgent = agent
                                appState = AppState.WIZARD
                            },
                            onCreateNew = {
                                editingAgent = null
                                appState = AppState.WIZARD
                            }
                        )
                    }
                    AppState.CHAT -> {
                        ChatScreen(
                            agent = selectedAgent,
                            userProfile = userProfile,
                            messages = messages,
                            onSendMessage = { msg ->
                                val updated = messages + msg
                                messages = updated
                                storage.saveMessages(selectedAgent.id, updated)
                            },
                            onBack = {
                                appState = AppState.CHARACTER_CARDS
                            },
                            onStartCall = {
                                appState = AppState.CALL
                            }
                        )
                    }
                    AppState.CALL -> {
                        CallScreen(
                            agent = selectedAgent,
                            onEndCall = {
                                appState = AppState.CHAT
                            }
                        )
                    }
                    AppState.WIZARD -> {
                        CharacterWizardScreen(
                            agent = editingAgent,
                            onSave = { updatedAgent ->
                                val list = if (agents.any { it.id == updatedAgent.id }) {
                                    agents.map { if (it.id == updatedAgent.id) updatedAgent else it }
                                } else {
                                    agents + updatedAgent
                                }
                                agents = list
                                storage.saveAgents(list)
                                selectedAgent = updatedAgent
                                appState = AppState.CHARACTER_CARDS
                            },
                            onBack = {
                                appState = AppState.CHARACTER_CARDS
                            }
                        )
                    }
                    AppState.SETTINGS -> {
                        SettingsScreen(
                            userProfile = userProfile,
                            onSave = { updatedProfile ->
                                userProfile = updatedProfile
                                storage.saveUserProfile(updatedProfile)
                                appState = AppState.CHARACTER_CARDS
                            },
                            onBack = {
                                appState = AppState.CHARACTER_CARDS
                            }
                        )
                    }
                    AppState.USER_PROFILE -> {
                        UserProfileScreen(
                            userProfile = userProfile,
                            onSave = { updatedProfile ->
                                userProfile = updatedProfile
                                storage.saveUserProfile(updatedProfile)
                                appState = AppState.CHARACTER_CARDS
                            },
                            onBack = {
                                appState = AppState.CHARACTER_CARDS
                            }
                        )
                    }
                }
            }
        }
    }
}
