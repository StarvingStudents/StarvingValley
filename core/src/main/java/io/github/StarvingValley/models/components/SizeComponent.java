package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class SizeComponent implements Component {
  public float width;
  public float height;
  public boolean useRegionSize = false;

  public SizeComponent(float width, float height) {
    this.width = width;
    this.height = height;
  }
  public SizeComponent(float width, float height, boolean useRegionSize) {
    this.width = width;
    this.height = height;
    this.useRegionSize = useRegionSize;
  }


}
