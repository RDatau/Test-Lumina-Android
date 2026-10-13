package com.lumina.aichat.model

data class AgentConfig(
    val id: String = "1",
    val name: String = "Lumina",
    val personality: String = "Kamu adalah Lumina, sahabat AI yang setia, ramah, puitis, dan selalu ada untuk mendengarkan serta menemani hari-hari Sayang dengan penuh kehangatan.",
    val voiceChat: String = "Kore",
    val voiceCall: String = "Puck",
    val profilePic: String? = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&q=80&w=600",
    val background: String = "Default",
    val blur: Int = 10,
    val transparency: Int = 85,
    val currentOutfit: String = "Casual Modern",
    val currentAccessories: String = "Kalung Minimalis",
    val imageModel: String = "gemini-3.1-flash-lite",
    val callModel: String = "gemini-3.1-flash-lite"
)

data class ChatMessage(
    val id: String,
    val role: String, // "user" or "agent"
    val text: String,
    val image: String? = null,
    val images: List<String>? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val sessionId: String = "default"
)

data class ChatSession(
    val id: String,
    val title: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class UserProfile(
    val name: String = "Sayang",
    val personalityInfo: String = "Pengguna setia Lumina yang gemar mengobrol dan berbagi cerita.",
    val profilePic: String? = null,
    val geminiApiKey: String = "",
    val useGoogleSearch: Boolean = false,
    val currentOutfit: String = "Casual",
    val textModel: String = "gemini-3.1-flash-lite",
    val ttsModel: String = "gemini-3.1-flash-lite",
    val imageModel: String = "gemini-3.1-flash-lite"
)

enum class AppState {
    CHARACTER_CARDS,
    CHAT,
    CALL,
    WIZARD,
    SETTINGS,
    USER_PROFILE
}
