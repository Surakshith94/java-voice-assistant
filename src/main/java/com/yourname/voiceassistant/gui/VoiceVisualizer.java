package com.yourname.voiceassistant.gui;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class VoiceVisualizer extends Pane {
    private static final int NUM_LINES = 30;
    private static final double SPACING = 5;
    private static final double MIN_HEIGHT = 2;
    private static final double MAX_HEIGHT = 50;

    private final Line[] lines = new Line[NUM_LINES];

    public VoiceVisualizer(double width, double height) {
        setPrefSize(width, height);

        double lineWidth = (width - (NUM_LINES - 1) * SPACING) / NUM_LINES;

        for (int i = 0; i < NUM_LINES; i++) {
            Line line = new Line();
            line.setStartX(i * (lineWidth + SPACING));
            line.setEndX(i * (lineWidth + SPACING) + lineWidth);
            line.setStartY(height / 2);
            line.setEndY(height / 2);
            line.setStroke(Color.web("#4CAF50"));
            line.setStrokeWidth(lineWidth);

            lines[i] = line;
            getChildren().add(line);
        }
    }

    public void update(double[] amplitudes) {
        for (int i = 0; i < Math.min(amplitudes.length, NUM_LINES); i++) {
            double normalized = Math.min(1, Math.max(0, amplitudes[i]));
            double height = MIN_HEIGHT + normalized * (MAX_HEIGHT - MIN_HEIGHT);

            Line line = lines[i];
            line.setStartY(getHeight() / 2 - height / 2);
            line.setEndY(getHeight() / 2 + height / 2);
        }
    }

    public void reset() {
        for (Line line : lines) {
            line.setStartY(getHeight() / 2);
            line.setEndY(getHeight() / 2);
        }
    }
}