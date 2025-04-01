package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class CropTypeComponent implements Component {
  public CropType cropType;

  public CropTypeComponent(CropType cropType) {
    this.cropType = cropType;
  }

  public enum CropType {
    TOMATO,
    POTATO
  }
}
