package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class PulseAlphaComponent implements Component {
    public float baseAlpha = 0.3f;
    public float amplitude = 0.2f;
    public float speed = 3f;
    public float time = 0f;
}
