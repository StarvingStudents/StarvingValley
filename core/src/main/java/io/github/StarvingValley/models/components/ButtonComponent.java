package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.ButtonType;

public class ButtonComponent implements Component {
    public ButtonType buttonType;

    public ButtonComponent(ButtonType buttonType) {
        this.buttonType = buttonType;
    }
    
}
