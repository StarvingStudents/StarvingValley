package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import java.time.Duration;
import java.time.Instant;

public class TimeToGrowComponent implements Component {
    public Instant plantedTime;
    public Duration growthDuration;

    public TimeToGrowComponent(Instant plantedTime, Duration growthDuration) {
        this.plantedTime = plantedTime;
        this.growthDuration = growthDuration;
    }

    public TimeToGrowComponent(Duration growthDuration) {
        this.plantedTime = Instant.now();
        this.growthDuration = growthDuration;
    }

  public int getGrowthStage() {
      int GROWTH_STAGE_MATURE = 2;
      int GROWTH_STAGE_GROWNING = 1;
        int GROWTH_STAGE_SPROUT = 0;


      Instant now = Instant.now();
      Duration elapsed = Duration.between(plantedTime, now);

      if (elapsed.compareTo(growthDuration) >= 0) {
          return GROWTH_STAGE_MATURE;
      }

      double ratio = (double) elapsed.toMillis() / growthDuration.toMillis();
      if (ratio >= 0.5) {
          return GROWTH_STAGE_GROWNING;
      }

      return GROWTH_STAGE_SPROUT;
  }

  public boolean isMature() {
        return Instant.now().isAfter(plantedTime.plus(growthDuration));
  }
}
