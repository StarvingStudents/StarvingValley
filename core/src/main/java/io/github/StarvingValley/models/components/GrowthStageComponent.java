package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class GrowthStageComponent implements Component {
  public int growthStage; // 0, 1 or 2

  public GrowthStageComponent() {
    this.growthStage = 0; // sprout (should maybe be seed?)
  }

  public GrowthStageComponent(int growthStage) {
    this.growthStage = growthStage;
  }
}
