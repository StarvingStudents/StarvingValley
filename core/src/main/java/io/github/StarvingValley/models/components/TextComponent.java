package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class TextComponent implements Component {
    public String text;
    public float offsetX = 0f;
    public float offsetY = 0f;

    public TextComponent(String text, float offsetX, float offsetY) {
        this.text = text;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
