package com.yourname.voiceassistant;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String VOICE_NAME = AppConfig.getDefaultVoice();
    private static Voice voice;

    public static void main(String[] args) {
        logger.info("Starting voice assistant");

        // Set the path to freetts.jar resources
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        try {
            // Initialize database first
            DatabaseService.initialize();

            // Initialize TTS
            VoiceManager vm = VoiceManager.getInstance();
            voice = vm.getVoice(VOICE_NAME);

            if (voice != null) {
                voice.allocate();
                speak("Hello, I am " + AppConfig.getAssistantName() + ". Say '" +
                        AppConfig.getWakeWord() + "' to activate me.");
            } else {
                logger.error("Cannot find voice '{}'", VOICE_NAME);
                logger.error("Available voices: {}", java.util.Arrays.toString(vm.getVoices()));
                return;
            }

            VoiceRecognizer recognizer = new VoiceRecognizer();
            CommandManager commandManager = new CommandManager();

            while (true) {
                logger.info("Listening for wake word...");
                recognizer.listenForWakeWord();
                speak("Yes? How can I help you?");

                String command = recognizer.listen();
                if (!command.isEmpty()) {
                    logger.info("User command: {}", command);

                    if (command.toLowerCase().contains("exit") || command.toLowerCase().contains("quit")) {
                        speak("Goodbye!");
                        System.exit(0);
                    }

                    commandManager.processCommand(command);
                }
            }
        } catch (Exception e) {
            logger.error("Error in main loop", e);
        }
    }

    public static void speak(String text) {
        logger.info("[ASSISTANT] {}", text);
        try {
            voice.speak(text);
        } catch (Exception e) {
            logger.error("TTS failed: {}", e.getMessage());
            // Fallback to Windows TTS if available
            try {
                Runtime.getRuntime().exec(
                        "powershell.exe -Command \"Add-Type -AssemblyName System.Speech; " +
                                "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                                "$speak.Speak('" + text.replace("\"", "\\\"") + "');\""
                );
            } catch (Exception ex) {
                logger.error("Fallback TTS failed: {}", ex.getMessage());
            }
        }
    }
}