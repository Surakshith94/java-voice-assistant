package com.yourname.voiceassistant;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;



public class Main {
    private static Voice voice;

    public static void main(String[] args) {
        // Set the path to freetts.jar resources
        System.setProperty("freetts.voices",
                "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");

        // Initialize TTS
        VoiceManager vm = VoiceManager.getInstance();
        voice = vm.getVoice("kevin16");

        // Initialize database first
        DatabaseService.initialize();

        if (voice != null) {
            voice.allocate();
            speak("Hello, I am " + AppConfig.getAssistantName() + ". Say '" +
                    AppConfig.getWakeWord() + "' to activate me.");
        } else {
            System.err.println("[ERROR] Cannot find voice 'kevin16'");
            System.err.println("Available voices: " +
                    java.util.Arrays.toString(vm.getVoices()));
            return;
        }

        VoiceRecognizer recognizer = new VoiceRecognizer();
        CommandManager commandManager = new CommandManager();

        while (true) {
            System.out.println("Listening for wake word...");
            recognizer.listenForWakeWord();
            speak("Yes? How can I help you?");

            String command = recognizer.listen();
            if (!command.isEmpty()) {
                System.out.println("You said: " + command);

                if (command.toLowerCase().contains("exit") || command.toLowerCase().contains("quit")) {
                    speak("Goodbye!");
                    System.exit(0);
                }

                commandManager.processCommand(command);
            }
        }

    }

    public static void speak(String text) {
        System.out.println("[ASSISTANT] " + text);
        try {
            voice.speak(text);
        } catch (Exception e) {
            System.err.println("[ERROR] TTS failed: " + e.getMessage());
            // Fallback to Windows TTS if available
            try {
                Runtime.getRuntime().exec(
                        "powershell.exe -Command \"Add-Type -AssemblyName System.Speech; " +
                                "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                                "$speak.Speak('" + text.replace("\"", "\\\"") + "');\""
                );
            } catch (Exception ex) {
                System.err.println("[FALLBACK ERROR] Couldn't use Windows TTS");
            }
        }
    }
}