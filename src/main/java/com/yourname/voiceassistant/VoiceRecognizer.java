package com.yourname.voiceassistant;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import java.io.File;

public class VoiceRecognizer {
    private LiveSpeechRecognizer recognizer;
    private String wakeWord;

    public VoiceRecognizer() {
        try {
            System.out.println("[SYSTEM] Initializing voice recognizer...");
            this.wakeWord = AppConfig.getWakeWord().toLowerCase();

            Configuration configuration = new Configuration();

            // Model configuration
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

            // Grammar configuration - using your grammar.gram file
            configuration.setGrammarPath("resource:/grammar");
            configuration.setGrammarName("grammar");  // Must match your filename without extension
            configuration.setUseGrammar(true);

            configuration.setSampleRate(16000);

            recognizer = new LiveSpeechRecognizer(configuration);
            System.out.println("[SYSTEM] Recognizer ready!");
        } catch (Exception e) {
            System.err.println("[ERROR] Initialization failed: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize voice recognizer", e);
        }
    }

    public void listenForWakeWord() {
        System.out.println("\n[SYSTEM] Waiting for wake word: '" + wakeWord + "'...");
        recognizer.startRecognition(false);

        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            String utterance = result.getHypothesis().toLowerCase().trim();
            System.out.println("[DEBUG] Heard: " + utterance);

            if (utterance.equals(wakeWord)) {
                recognizer.stopRecognition();
                System.out.println("[SYSTEM] Wake word detected!");
                return;
            }
        }
    }

    public String listen() {
        System.out.println("[SYSTEM] Listening for command...");
        recognizer.startRecognition(false);
        SpeechResult result = recognizer.getResult();
        recognizer.stopRecognition();

        if (result != null) {
            String command = result.getHypothesis().trim();
            if (!command.isEmpty() && !command.equals("<unk>")) {
                System.out.println("[USER] Command: " + command);
                return command.toLowerCase();
            }
        }
        return "";
    }
}