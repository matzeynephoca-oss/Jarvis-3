package com.example.jarvis2

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.speech.RecognizerIntent
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var input: EditText
    private lateinit var output: TextView
    private lateinit var status: TextView
    private lateinit var tts: TextToSpeech
    private var recognizer: SpeechRecognizer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        output = findViewById(R.id.output)
        status = findViewById(R.id.status)
        tts = TextToSpeech(this, this)

        findViewById<Button>(R.id.ask).setOnClickListener {
            handleCommand(input.text.toString())
        }
        findViewById<Button>(R.id.mic).setOnClickListener { listen() }
        findViewById<Button>(R.id.whatsappAccess).setOnClickListener {
            startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
        }

        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.POST_NOTIFICATIONS), 10)
        } else {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 10)
        }
    }

    override fun onInit(statusCode: Int) {
        if (statusCode == TextToSpeech.SUCCESS) {
            tts.language = Locale("tr", "TR")
        }
    }

    private fun say(text: String) {
        output.text = text
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "jarvis2")
    }

    private fun listen() {
        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            say("Bu cihazda sesli tanıma kullanılamıyor.")
            return
        }

        recognizer?.destroy()
        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        status.text = "Dinliyorum..."

        recognizer!!.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                status.text = "Hazırım."
                val text = results?.getStringArrayList(
                    SpeechRecognizer.RESULTS_RECOGNITION
                )?.firstOrNull() ?: return
                input.setText(text)
                handleCommand(text)
            }
            override fun onError(error: Int) { status.text = "Dinleme hatası." }
            override fun onReadyForSpeech(p: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(r: Float) {}
            override fun onBufferReceived(b: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(b: Bundle?) {}
            override fun onEvent(t: Int, b: Bundle?) {}
        })

        recognizer!!.startListening(Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        })
    }

    private fun handleCommand(raw: String) {
        val q = raw.lowercase(Locale("tr", "TR")).trim()

        when {
            q.contains("saat") -> {
                val time = SimpleDateFormat("HH:mm", Locale("tr", "TR")).format(Date())
                say("Saat $time.")
            }

            q.contains("şarj") || q.contains("pil") -> {
                val battery = getSystemService(BATTERY_SERVICE) as android.os.BatteryManager
                val level = battery.getIntProperty(android.os.BatteryManager.BATTERY_PROPERTY_CAPACITY)
                say("Şarj yüzde $level.")
            }

            q.contains("whatsapp") -> openPackage("com.whatsapp", "WhatsApp bulunamadı.")
            q.contains("youtube") -> openUrl("https://www.youtube.com")
            q.contains("google") -> openUrl("https://www.google.com")
            q.contains("roblox") -> openPackage("com.roblox.client", "Roblox bulunamadı.")

            q.startsWith("ara ") -> {
                val search = Uri.encode(raw.substringAfter("ara ").trim())
                openUrl("https://www.google.com/search?q=$search")
            }

            q.contains("hava") -> {
                openUrl("https://www.google.com/search?q=hava+durumu+bugün")
            }

            q.contains("whatsapp mesaj") || q.contains("son mesaj") -> {
                val p = getSharedPreferences("jarvis2", MODE_PRIVATE)
                val sender = p.getString("sender", null)
                val text = p.getString("text", null)
                if (sender != null && text != null) {
                    say("$sender şöyle yazdı: $text")
                } else {
                    say("Henüz okunmuş bir WhatsApp bildirimi yok.")
                }
            }

            else -> say("Bu komutu henüz tanımıyorum. Saat, şarj, WhatsApp, YouTube, Roblox, hava durumu veya ara komutlarını deneyebilirsin.")
        }
    }

    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun openPackage(pkg: String, error: String) {
        packageManager.getLaunchIntentForPackage(pkg)?.let {
            startActivity(it)
        } ?: say(error)
    }

    override fun onDestroy() {
        recognizer?.destroy()
        tts.shutdown()
        super.onDestroy()
    }
}
