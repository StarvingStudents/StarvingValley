package io.github.StarvingValley.models.components;

import java.util.function.Supplier;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;

public class TextComponent implements Component {
    private String text;
    public float offsetX = 0f;
    public float offsetY = 0f;

    public Color color;
    public Float scale;

    public Supplier<String> dynamicText;

    public TextComponent(String text, float offsetX, float offsetY) {
        this.text = text;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public TextComponent(Supplier<String> dynamicText, float offsetX, float offsetY) {
        this.dynamicText = dynamicText;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public TextComponent(Supplier<String> dynamicText, float offsetX, float offsetY, Color color) {
        this.dynamicText = dynamicText;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.color = color;
    }
    
    public String getText() {
        return dynamicText != null ? dynamicText.get() : text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
