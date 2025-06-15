package com.yourname.voiceassistant;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yourname.voiceassistant.services.DatabaseService;
import com.yourname.voiceassistant.gui.MainApplication;
import com.yourname.voiceassistant.gui.MainController;
import javafx.application.Platform;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String VOICE_NAME = AppConfig.getDefaultVoice();
    private static MainController guiController;
    private static Voice voice;
    private static CommandManager commandManager;

    public static void setGuiController(MainController controller) {
        guiController = controller;
    }

    public static String getAssistantName() {
        return AppConfig.getAssistantName();
    }

    public static String getWakeWord() {
        return AppConfig.getWakeWord();
    }

    public static void processCommand(String command) {
        if (commandManager != null) {
            commandManager.processCommand(command);
        } else {
            logger.warn("Command manager not initialized");
        }
    }

    public static void main(String[] args) {
        logger.info("Starting voice assistant");

        // Check if GUI should be launched
        if (args.length == 0 || !args[0].equals("--no-gui")) {
            MainApplication.launch(MainApplication.class, args);
        } else {
            // CLI mode
            initializeComponents();
            runCliMode();
        }
    }

    private static void initializeComponents() {
        // Set the path to freetts.jar resources
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        try {
            // Initialize database first
            DatabaseService.initialize();

            // Initialize TTS
            initializeVoice();

            // Initialize command manager
            commandManager = new CommandManager();

        } catch (Exception e) {
            logger.error("Initialization failed", e);
            System.exit(1);
        }
    }

    private static void initializeVoice() {
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice(VOICE_NAME);

        if (voice != null) {
            voice.allocate();
        } else {
            logger.error("Cannot find voice '{}'", VOICE_NAME);
            logger.error("Available voices: {}", java.util.Arrays.toString(vm.getVoices()));
        }
    }

    private static void runCliMode() {
        VoiceRecognizer recognizer = new VoiceRecognizer();
        speak("Hello, I am " + AppConfig.getAssistantName() + ". Say '" +
                AppConfig.getWakeWord() + "' to activate me.");

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

                processCommand(command);
            }
        }
    }

    public static void speak(String text) {
        logger.info("[ASSISTANT] {}", text);

        // Update GUI if available
        if (guiController != null) {
            Platform.runLater(() -> guiController.appendToConversation(text));
        }

        // Speak through TTS
        try {
            if (voice != null) {
                voice.speak(text);
            } else {
                logger.warn("Voice not initialized, using fallback for: {}", text);
                useFallbackTTS(text);
            }
        } catch (Exception e) {
            logger.error("TTS failed: {}", e.getMessage());
            useFallbackTTS(text);
        }
    }

    private static void useFallbackTTS(String text) {
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