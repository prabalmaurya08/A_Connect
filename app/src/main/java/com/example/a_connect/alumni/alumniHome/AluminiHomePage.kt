package com.example.a_connect.alumni.alumniHome

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAlumniHomePageBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView


class AluminiHomePage : Fragment() {
    private lateinit var binding: FragmentAlumniHomePageBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerButton: ImageButton

    private var listener: OnItemClickedInsideViewPager? = null




    //Implementing Voice Assistant
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var fabVoiceInput: FloatingActionButton
    private lateinit var tvTranscription: TextView
    private lateinit var micIcon: ImageView
    private lateinit var bottomSheetContent: LinearLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnItemClickedInsideViewPager -> {
                listener=context


            }



            else -> {
                throw ClassCastException("$context must implement OnSignupClickListener")
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlumniHomePageBinding.inflate(layoutInflater)

        drawerSetUp()

        binding.aluminiHomePageSearchBar.setOnClickListener {
           listener?.onSearchClicked()
        }


        return binding.root
    }


    private fun drawerSetUp() {
        // Set up Navigation Drawer
        drawerLayout = binding.aluminiHomeDrawerLayout
        navView = binding.aluminiHomeNavigationView

        // Set up the toolbar and drawer Button
        toolbar = binding.aluminiHomeToolbar
        drawerButton = binding.aluminiHomePageDrawer


        binding.aluminiHomePageChat.setOnClickListener {
            listener?.onChatButtonClicked()
        }
        binding.aluminiHomePageNotification.setOnClickListener {
            listener?.onNotificationButtonClicked()
        }


        drawerButton.setOnClickListener {
            drawerLayout.open()
        }
}
    interface OnItemClickedInsideViewPager {
        fun onChatButtonClicked()
        fun onNotificationButtonClicked()
        fun onSearchClicked()




    }






//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // Initialize UI components
//        fabVoiceInput = view.findViewById(R.id.fabVoiceInput)
//        tvTranscription = view.findViewById(R.id.tvTranscription)
//        micIcon = view.findViewById(R.id.micIcon)
//        bottomSheetContent = view.findViewById(R.id.bottomSheetContent)
//
//        // Initialize Speech Recognizer
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
//
//        // Set up Bottom Sheet behavior
//
//        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContent)
//
//        // Initially hide the bottom sheet content
//        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//
//        // FAB to trigger Bottom Sheet and start voice input
//        fabVoiceInput.setOnClickListener {
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED // Open the Bottom Sheet
//            startListening() // Start the voice recognition process
//        }
//
//        // Set up recognition listener
//        speechRecognizer.setRecognitionListener(object : RecognitionListener {
//            override fun onReadyForSpeech(params: Bundle?) {
//                // Optional: Indicate when ready for speech
//                micIcon.setImageResource(R.drawable.sound) // Change mic icon to show it's ready
//            }
//
//            override fun onBeginningOfSpeech() {
//                // Indicate speech has started
//                tvTranscription.text = "Listening..."
//                micIcon.setImageResource(R.drawable.sound) // Animation or glowing mic
//            }
//
//            override fun onRmsChanged(rmsdB: Float) {}
//            override fun onBufferReceived(buffer: ByteArray?) {}
//            override fun onEndOfSpeech() {
//                // Optional: Reset mic icon to default
//                micIcon.setImageResource(R.drawable.microphone)
//            }
//
//            override fun onError(error: Int) {
//                // Handle error and provide feedback
//                tvTranscription.text = "Error occurred"
//                micIcon.setImageResource(R.drawable.microphone)
//            }
//
//            override fun onResults(results: Bundle?) {
//                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                if (!matches.isNullOrEmpty()) {
//                    val transcription = matches[0]
//                    tvTranscription.text = transcription // Update with the transcribed text
//                }
//            }
//
//            override fun onPartialResults(partialResults: Bundle?) {}
//            override fun onEvent(eventType: Int, params: Bundle?) {}
//        })
//    }
//
//    // Start listening for speech input
//    private fun startListening() {
//        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
//        speechRecognizer.startListening(intent)
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        if (speechRecognizer != null) {
//            speechRecognizer.destroy() // Clean up the SpeechRecognizer when the fragment is destroyed
//        }
//    }

}