package com.lumina.aichat.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lumina.aichat.model.AgentConfig
import com.lumina.aichat.model.ChatMessage
import com.lumina.aichat.model.ChatSession
import com.lumina.aichat.model.UserProfile

class LocalStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lumina_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getUserProfile(): UserProfile {
        val json = prefs.getString("user_profile", null)
        return if (json != null) {
            try { gson.fromJson(json, UserProfile::class.java) } catch (e: Exception) { UserProfile() }
        } else {
            UserProfile()
        }
    }

    fun saveUserProfile(profile: UserProfile) {
        val json = gson.toJson(profile)
        prefs.edit().putString("user_profile", json).apply()
    }

    fun getAgents(): List<AgentConfig> {
        val json = prefs.getString("agents_list", null)
        if (json != null) {
            try {
                val type = object : TypeToken<List<AgentConfig>>() {}.type
                val list: List<AgentConfig> = gson.fromJson(json, type)
                if (list.isNotEmpty()) return list
            } catch (e: Exception) {
                // fallback
            }
        }
        // Default Agents
        val defaults = listOf(
            AgentConfig(
                id = "1",
                name = "Lumina",
                personality = "Kamu adalah Lumina, sahabat AI yang setia, ramah, puitis, dan selalu ada untuk mendengarkan serta menemani hari-hari Sayang dengan penuh kehangatan.",
                profilePic = "https://images.unsplash.com/photo-1529626455594-4ff0802cfb7e?auto=format&fit=crop&q=80&w=600"
            ),
            AgentConfig(
                id = "2",
                name = "Luna",
                personality = "Kamu adalah Luna, asisten AI yang cerdas, futuristik, logis, namun sangat perhatian dan siap membantu dalam segala hal.",
                profilePic = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&q=80&w=600"
            ),
            AgentConfig(
                id = "3",
                name = "Aris",
                personality = "Kamu adalah Aris, mentor dan teman diskusi yang bijaksana, santai, dan memberikan sudut pandang inspiratif.",
                profilePic = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&q=80&w=600"
            )
        )
        saveAgents(defaults)
        return defaults
    }

    fun saveAgents(agents: List<AgentConfig>) {
        val json = gson.toJson(agents)
        prefs.edit().putString("agents_list", json).apply()
    }

    fun getSessions(agentId: String): List<ChatSession> {
        val json = prefs.getString("sessions_$agentId", null)
        if (json != null) {
            try {
                val type = object : TypeToken<List<ChatSession>>() {}.type
                return gson.fromJson(json, type)
            } catch (e: Exception) {}
        }
        val defaultSession = ChatSession(id = "sess_default", title = "Obrolan Perdana")
        saveSessions(agentId, listOf(defaultSession))
        return listOf(defaultSession)
    }

    fun saveSessions(agentId: String, sessions: List<ChatSession>) {
        val json = gson.toJson(sessions)
        prefs.edit().putString("sessions_$agentId", json).apply()
    }

    fun getMessages(sessionId: String): List<ChatMessage> {
        val json = prefs.getString("messages_$sessionId", null)
        if (json != null) {
            try {
                val type = object : TypeToken<List<ChatMessage>>() {}.type
                return gson.fromJson(json, type)
            } catch (e: Exception) {}
        }
        return emptyList()
    }

    fun saveMessages(sessionId: String, messages: List<ChatMessage>) {
        val json = gson.toJson(messages)
        prefs.edit().putString("messages_$sessionId", json).apply()
    }
}
