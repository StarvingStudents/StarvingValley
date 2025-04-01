package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class HarvestingComponent implements Component {
  public boolean canHarvest;

  public HarvestingComponent(boolean canHarvest) {
    this.canHarvest = canHarvest;
  }
}
