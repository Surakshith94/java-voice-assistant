package com.yourname.voiceassistant;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Main {
    private static final String VOICE_NAME = AppConfig.getDefaultVoice(); // Try "kevin" if this fails
    private static Voice voice;

    public static void main(String[] args) {
        // Set the path to find voice files
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        System.setProperty("freetts.voicespath", "libs"); // Point to your libs folder

        initializeVoice();
        speak("Hello, I am "+ AppConfig.getAssistantName() + ". How can I help you today?");
    }

    private static void initializeVoice() {
        VoiceManager voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice(VOICE_NAME);
        if (voice != null) {
            voice.allocate();
        } else {
            System.err.println("Cannot find voice: " + VOICE_NAME);
            System.exit(1);
        }
    }

    public static void speak(String text) {
        if (voice != null) {
            voice.speak(text);
        } else {
            System.out.println("Voice not initialized: " + text);
        }
    }
}