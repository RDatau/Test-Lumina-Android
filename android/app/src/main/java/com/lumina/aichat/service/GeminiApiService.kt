package com.lumina.aichat.service

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonArray
import com.lumina.aichat.model.ChatMessage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiApiService {
    private val client = OkHttpClient()
    private val gson = Gson()

    suspend fun generateResponse(
        apiKey: String,
        model: String,
        systemInstruction: String,
        history: List<ChatMessage>,
        userMessage: String
    ): String = withContext(Dispatchers.IO) {
        val effectiveApiKey = if (apiKey.isNotBlank()) apiKey else "AIzaSyDummyKeyForTestingOrProvided"
        val modelName = if (model.isNotBlank()) model else "gemini-3.1-flash-lite"
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$effectiveApiKey"

        val root = JsonObject()

        // System Instruction
        if (systemInstruction.isNotBlank()) {
            val sysObj = JsonObject()
            val partsArr = JsonArray()
            val partObj = JsonObject()
            partObj.addProperty("text", systemInstruction)
            partsArr.add(partObj)
            sysObj.add("parts", partsArr)
            root.add("systemInstruction", sysObj)
        }

        // Contents
        val contentsArr = JsonArray()
        for (msg in history) {
            val contentObj = JsonObject()
            contentObj.addProperty("role", if (msg.role == "user") "user" else "model")
            val partsArr = JsonArray()
            val partObj = JsonObject()
            partObj.addProperty("text", msg.text)
            partsArr.add(partObj)
            contentObj.add("parts", partsArr)
            contentsArr.add(contentObj)
        }

        // Current message
        val currentObj = JsonObject()
        currentObj.addProperty("role", "user")
        val currentParts = JsonArray()
        val currentPart = JsonObject()
        currentPart.addProperty("text", userMessage)
        currentParts.add(currentPart)
        currentObj.add("parts", currentParts)
        contentsArr.add(currentObj)

        root.add("contents", contentsArr)

        val bodyString = gson.toJson(root)
        val body = bodyString.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                val responseBody = response.body?.string() ?: ""
                if (!response.isSuccessful) {
                    return@withContext "Maaf, terjadi kendala koneksi dengan server AI (Error ${response.code}): $responseBody"
                }

                val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
                val candidates = jsonResponse.getAsJsonArray("candidates")
                if (candidates != null && candidates.size() > 0) {
                    val candidate = candidates.get(0).asJsonObject
                    val content = candidate.getAsJsonObject("content")
                    val parts = content.getAsJsonArray("parts")
                    if (parts != null && parts.size() > 0) {
                        return@withContext parts.get(0).asJsonObject.get("text").asString
                    }
                }
                return@withContext "Maaf, Lumina tidak dapat memproses jawaban saat ini."
            }
        } catch (e: Exception) {
            // Fallback mock response if API key is invalid or network fails
            return@withContext "Halo Sayang! Senang sekali bisa mengobrol denganmu hari ini. (Catatan: Sambungan API Gemini memerlukan API Key aktif di pengaturan, namun aku tetap menemanimu!)"
        }
    }
}
