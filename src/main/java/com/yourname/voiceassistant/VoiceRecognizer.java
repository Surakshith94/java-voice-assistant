package com.yourname.voiceassistant;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VoiceRecognizer {
    private static final Logger logger = LoggerFactory.getLogger(VoiceRecognizer.class);
    private final LiveSpeechRecognizer recognizer;
    private final String wakeWord;

    public VoiceRecognizer() {
        try {
            logger.info("Initializing voice recognizer...");
            this.wakeWord = AppConfig.getWakeWord().toLowerCase();

            Configuration configuration = new Configuration();

            // Model configuration
            configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
            configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");

            // Grammar configuration
            configuration.setGrammarPath("resource:/grammar");
            configuration.setGrammarName("grammar");
            configuration.setUseGrammar(true);
            configuration.setSampleRate(16000);

            recognizer = new LiveSpeechRecognizer(configuration);
            logger.info("Voice recognizer initialized successfully");
        } catch (Exception e) {
            logger.error("Failed to initialize voice recognizer", e);
            throw new RuntimeException("Voice recognizer initialization failed", e);
        }
    }

    public void listenForWakeWord() {
        logger.info("Waiting for wake word: '{}'", wakeWord);
        recognizer.startRecognition(false);

        SpeechResult result;
        while ((result = recognizer.getResult()) != null) {
            String utterance = result.getHypothesis().toLowerCase().trim();
            logger.debug("Heard: {}", utterance);

            if (utterance.equals(wakeWord)) {
                recognizer.stopRecognition();
                logger.info("Wake word detected");
                return;
            }
        }
    }

    public String listen() {
        try {
            recognizer.startRecognition(true);
            SpeechResult result = recognizer.getResult();
            recognizer.stopRecognition();

            return result != null ? result.getHypothesis() : "";
        } catch (Exception e) {
            logger.error("Error in recognition", e);
            return "";
        }
    }

    public String getWakeWord() {
        return wakeWord;
    }
}