package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteComponent implements Component {
  public Sprite sprite;

  public SpriteComponent(String texturePath) {
    this.sprite = new Sprite(new Texture(texturePath));
  }

  public Sprite getSprite() {
    return sprite;
  }

  public void setSprite(Sprite sprite) {
    this.sprite = sprite;
  }
}
