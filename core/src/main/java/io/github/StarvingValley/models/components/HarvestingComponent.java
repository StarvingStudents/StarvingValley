package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class HarvestingComponent implements Component {
  public boolean canHarvest;
  public boolean isHarvested;
  public float interactionRadius;

  public HarvestingComponent(boolean canHarvest, float interactionRadius) {
    this.canHarvest = canHarvest;
    this.isHarvested = false;
    this.interactionRadius = interactionRadius;
  }
}
