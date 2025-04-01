package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class TimeToGrowComponent implements Component {
  public int timeToGrow; // time required
  public int growthProgress; // time since planting
  public float growthTimeAccumulator;

  public TimeToGrowComponent(int timeToGrow) {
    this.timeToGrow = timeToGrow;
    this.growthProgress = 0;
    this.growthTimeAccumulator = 0.0f;
  }

  public void accumulateGrowth(float deltaTime) {
    growthTimeAccumulator += deltaTime;
    if (growthTimeAccumulator >= 1.0f) {
      growthProgress++;
      growthTimeAccumulator = 0.0f;
    }
  }

  public boolean isMature() {
    return growthProgress >= timeToGrow;
  }
}
