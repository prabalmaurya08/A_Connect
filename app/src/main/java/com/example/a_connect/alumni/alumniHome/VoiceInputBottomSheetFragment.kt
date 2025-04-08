package com.example.a_connect.alumni.alumniHome


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView

import com.example.a_connect.R
import com.example.a_connect.alumni.alumniHome.ai.ChatAdapter
import com.example.a_connect.alumni.alumniHome.ai.ChatMessageDataClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.Firebase


import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class VoiceInputBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var tvTranscription: TextView
    private lateinit var micWaveAnimation: LottieAnimationView
    private var listener: AlumniHomePage.VoiceInputListener? = null
    private lateinit var etMessage: EditText
    private var isListening = false

    // ✅ Correct Vertex AI Initialization
    private val vertexAI by lazy { Firebase.vertexAI }
    private val generativeModel by lazy { vertexAI.generativeModel("gemini-1.5-flash") }

    // ChatBot Integration
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessageDataClass>()

    // Text-to-Speech
    private lateinit var textToSpeech: TextToSpeech

    // ✅ Initialize Text-to-Speech
    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
            }
        }
    }

    private fun speakBotResponse(response: String) {
        textToSpeech.speak(response, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    companion object {
        fun newInstance(): VoiceInputBottomSheetFragment {
            return VoiceInputBottomSheetFragment()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AlumniHomePage.VoiceInputListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement VoiceInputListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_voice_input, container, false)

        tvTranscription = view.findViewById(R.id.tvTranscription)
        micWaveAnimation = view.findViewById(R.id.micWaveAnimation)
        etMessage = view.findViewById(R.id.etMessage)

        chatRecyclerView = view.findViewById(R.id.rvChat)
        chatRecyclerView.layoutManager = LinearLayoutManager(context)
        chatAdapter = ChatAdapter(messages)
        chatRecyclerView.adapter = chatAdapter

        setupSpeechRecognizer()
        setupTextToSpeech()

        // ✅ Start animation and listening automatically when BottomSheet opens
        micWaveAnimation.playAnimation()
        startListening()

        // ✅ Toggle speech recognition on tapping Lottie animation
        micWaveAnimation.setOnClickListener {
            if (isListening) {
                stopListening()
            } else {
                startListening()
            }
        }

        // ✅ Button to send message to Vertex AI
        view.findViewById<ImageView>(R.id.btnSend).setOnClickListener {
            val userMessage = etMessage.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                addMessage(userMessage, true) // Add user message to chat
                etMessage.text.clear()

                // Call Vertex AI
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = generativeModel.generateContent(userMessage)
                        val botResponse = response.text ?: "Sorry, I didn't understand that."

                        requireActivity().runOnUiThread {
                            addMessage(botResponse, false)
                            speakBotResponse(botResponse) // Speak full response
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                        requireActivity().runOnUiThread {
                            addMessage("Error: ${e.message}", false)
                        }
                    }
                }
            }
        }

        return view
    }






    private fun setupSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                micWaveAnimation.playAnimation()
            }

            override fun onBeginningOfSpeech() {
                tvTranscription.text = "Listening..."
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                stopListening()
            }

            override fun onError(error: Int) {
                tvTranscription.text = "Error occurred"
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val recognizedText = matches[0]
                    tvTranscription.text = recognizedText
                    etMessage.setText(recognizedText)
                    handleVoiceCommand(recognizedText)
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    private fun startListening() {
        isListening = true
        micWaveAnimation.playAnimation() // ✅ Start animation
        tvTranscription.text = "Listening..."

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        speechRecognizer.startListening(intent)
    }

    private fun stopListening() {
        isListening = false
        micWaveAnimation.pauseAnimation() // ✅ Stop animation
        speechRecognizer.stopListening()
        tvTranscription.text = "Tap to start listening"
    }

    // ✅ Process User Commands Correctly

    private fun handleVoiceCommand(command: String) {
        when {
            command.contains("open chat", ignoreCase = true) -> {
                listener?.onChatCommandReceived()
                dismissAllowingStateLoss() // Fixes dismissal issue
            }
            command.contains("open notification", ignoreCase = true) -> {
                listener?.onNotificationCommandReceived()
                dismissAllowingStateLoss()
            }
            command.contains("open search", ignoreCase = true) -> {
                listener?.onSearchCommandReceived()
                dismissAllowingStateLoss()
            }
            else -> {
                etMessage.setText(command)
                tvTranscription.text = ""
            }
        }
    }


    // ✅ Add Message to Chat RecyclerView
    private fun addMessage(message: String, isUserMessage: Boolean) {
        requireActivity().runOnUiThread {
            messages.add(ChatMessageDataClass(message, isUserMessage))
            chatAdapter.notifyItemInserted(messages.size - 1)
            chatRecyclerView.scrollToPosition(messages.size - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        speechRecognizer.destroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
