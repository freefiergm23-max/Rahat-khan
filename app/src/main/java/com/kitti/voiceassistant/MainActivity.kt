package com.kitti.voiceassistant

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var textToSpeech: TextToSpeech
    private var commandHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale("bn", "BD")
            }
        }

        setContent {
            KittiTheme {
                KittiVoiceAssistant(
                    onMicClick = { startSpeechRecognition() },
                    onClearHistory = { commandHistory.clear() },
                    commandHistory = commandHistory
                )
            }
        }
    }

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "bn-BD")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "কিছু বলুন...")
        }
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            results?.get(0)?.let { spokenText ->
                commandHistory.add(spokenText)
                processCommand(spokenText)
            }
        }
    }

    private fun processCommand(command: String) {
        val lowerCommand = command.lowercase()
        when {
            "ইউটিউব" in lowerCommand || "youtube" in lowerCommand -> {
                openApp("https://youtube.com", "ইউটিউব খুলছি")
            }
            "ওয়াটসঅ্যাপ" in lowerCommand || "whatsapp" in lowerCommand -> {
                openApp("https://web.whatsapp.com", "হোয়াটসঅ্যাপ খুলছি")
            }
            "ফেসবুক" in lowerCommand || "facebook" in lowerCommand -> {
                openApp("https://facebook.com", "ফেসবুক খুলছি")
            }
            "ম্যাপ" in lowerCommand || "maps" in lowerCommand -> {
                openApp("https://maps.google.com", "ম্যাপ খুলছি")
            }
            "ইনস্টাগ্রাম" in lowerCommand || "instagram" in lowerCommand -> {
                openApp("https://instagram.com", "ইনস্টাগ্রাম খুলছি")
            }
            "গুগল" in lowerCommand || "google" in lowerCommand -> {
                openApp("https://google.com", "গুগল খুলছি")
            }
            else -> {
                textToSpeech.speak("আমি বুঝতে পারছি না। আবার চেষ্টা করুন", TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun openApp(url: String, message: String) {
        startActivity(Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)))
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null)
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

    companion object {
        private const val SPEECH_REQUEST_CODE = 100
    }
}

@Composable
fun KittiVoiceAssistant(
    onMicClick: () -> Unit,
    onClearHistory: () -> Unit,
    commandHistory: List<String>
) {
    val isDarkMode = isSystemInDarkTheme()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isDarkMode) Color(0xFF121212) else Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎤 Kitti",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        Text(
            text = "Smart Voice Assistant",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        Button(
            onClick = onMicClick,
            modifier = Modifier.size(120.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6200EE)
            ),
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Microphone",
                modifier = Modifier.size(48.dp),
                tint = Color.White
            )
        }

        Text(
            text = "টিপুন এবং কথা বলুন",
            fontSize = 16.sp,
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "কমান্ড হিস্ট্রি",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDarkMode) Color.White else Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )

        if (commandHistory.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                items(commandHistory.reversed()) { command ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color(0xFFF5F5F5)
                        )
                    ) {
                        Text(
                            text = command,
                            modifier = Modifier.padding(16.dp),
                            fontSize = 14.sp,
                            color = if (isDarkMode) Color.White else Color.Black
                        )
                    }
                }
            }

            Button(
                onClick = onClearHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5252)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear",
                    modifier = Modifier.size(18.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("হিস্ট্রি মুছুন", color = Color.White)
            }
        } else {
            Text(
                text = "এখনো কোন কমান্ড নেই",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 32.dp)
            )
        }
    }
}

@Composable
fun KittiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) {
            darkColorScheme(
                primary = Color(0xFF6200EE),
                secondary = Color(0xFF03DAC6),
                tertiary = Color(0xFF03DAC6)
            )
        } else {
            lightColorScheme(
                primary = Color(0xFF6200EE),
                secondary = Color(0xFF03DAC6),
                tertiary = Color(0xFF03DAC6)
            )
        },
        content = content
    )
}
