package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
    public Sprite sprite;
    private String _texturePath;
    public boolean textureChanged;
    public String previousTexturePath;

    public SpriteComponent(String texturePath) {
        setTexturePath(texturePath);

        sprite = new Sprite();
    }

    public void setTexturePath(String texturePath) {
        if (!textureChanged) {
            this.previousTexturePath = this._texturePath;
            textureChanged = true;
        }

        this._texturePath = texturePath;
    }

    public String getTexturePath() {
        return _texturePath;
    }
}
