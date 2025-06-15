package com.yourname.voiceassistant.gui;

import com.yourname.voiceassistant.Main;
import com.yourname.voiceassistant.VoiceRecognizer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {
    @FXML private Label statusLabel;
    @FXML private Button listenButton;
    @FXML private Button stopButton;
    @FXML private TextArea conversationArea;
    @FXML private TextField commandField;
    @FXML private Button sendButton;
    @FXML private TextField nameField;
    @FXML private TextField wakeWordField;
    @FXML private ComboBox<String> voiceComboBox;
    @FXML private VoiceVisualizer voiceVisualizer;

    private VoiceRecognizer recognizer;
    private boolean isListening = false;
    private final double[] amplitudes = new double[30];
    private final Random random = new Random();
    private AnimationTimer animationTimer;

    @FXML
    public void initialize() {
        // Initialize voice recognizer
        recognizer = new VoiceRecognizer();

        // Set up event handlers
        listenButton.setOnAction(e -> startListening());
        stopButton.setOnAction(e -> stopListening());
        sendButton.setOnAction(e -> processCommand());

        // Allow pressing Enter in command field to send
        commandField.setOnAction(e -> processCommand());

        // Populate voice combo box
        voiceComboBox.getItems().addAll("kevin16", "alan", "mbrola");
        voiceComboBox.getSelectionModel().selectFirst();

        // Load initial configuration
        nameField.setText(Main.getAssistantName());
        wakeWordField.setText(Main.getWakeWord());

        // Setup voice visualizer animation
        setupVisualizerAnimation();
    }

    private void startListening() {
        isListening = true;
        listenButton.setDisable(true);
        stopButton.setDisable(false);
        statusLabel.setText("Listening...");

        new Thread(() -> {
            while (isListening) {
                String command = recognizer.listen();
                if (!command.isEmpty()) {
                    Platform.runLater(() -> {
                        conversationArea.appendText("You: " + command + "\n");
                        Main.processCommand(command);
                    });
                }
            }
        }).start();
    }

    private void stopListening() {
        isListening = false;
        listenButton.setDisable(false);
        stopButton.setDisable(true);
        statusLabel.setText("Ready");
    }

    private void processCommand() {
        String command = commandField.getText().trim();
        if (!command.isEmpty()) {
            conversationArea.appendText("You: " + command + "\n");
            commandField.clear();
            Main.processCommand(command);
        }
    }

    public void appendToConversation(String text) {
        Platform.runLater(() -> {
            conversationArea.appendText("Assistant: " + text + "\n");
        });
    }
    private void setupVisualizerAnimation() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isListening) {
                    // Simulate voice amplitudes (replace with real audio analysis later)
                    for (int i = 0; i < amplitudes.length; i++) {
                        amplitudes[i] = Math.max(0, amplitudes[i] - 0.05);
                        if (random.nextDouble() < 0.3) {
                            amplitudes[i] = random.nextDouble();
                        }
                    }
                    voiceVisualizer.update(amplitudes);
                } else {
                    voiceVisualizer.reset();
                }
            }
        };
        animationTimer.start();
    }

    private void startListening() {
        isListening = true;
        listenButton.setDisable(true);
        stopButton.setDisable(false);
        statusLabel.setText("Listening...");

        new Thread(() -> {
            while (isListening) {
                String command = recognizer.listen();
                if (!command.isEmpty()) {
                    Platform.runLater(() -> {
                        conversationArea.appendText("You: " + command + "\n");
                        Main.processCommand(command);
                    });
                }
            }
        }).start();
    }
}