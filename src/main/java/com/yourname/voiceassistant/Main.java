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

        if (voice != null) {
            voice.allocate();
            speak("Hello I am " + AppConfig.getAssistantName());
        } else {
            System.err.println("[ERROR] Cannot find voice 'kevin16'");
            System.err.println("Available voices: " +
                    java.util.Arrays.toString(vm.getVoices()));
            return;
        }

        VoiceRecognizer recognizer = new VoiceRecognizer();

        while (true) {
            recognizer.listenForWakeWord();
            speak("How can I help you?");

            String command = recognizer.listen();

            if (!command.isEmpty()) {
                System.out.println("[SYSTEM] Processing: " + command);

                if (command.matches(".*(time|clock).*")) {
                    String time = java.time.LocalTime.now()
                            .format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
                    speak("The time is " + time);
                }
                else if (command.matches(".*(exit|quit|stop).*")) {
                    speak("Goodbye");
                    System.exit(0);
                }
                else {
                    speak("You said: " + command);
                }
            }
        }
    }

    private static void speak(String text) {
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