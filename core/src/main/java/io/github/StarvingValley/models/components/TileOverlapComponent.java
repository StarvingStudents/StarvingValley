package io.github.StarvingValley.models.components;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.GridPoint2;

public class TileOverlapComponent implements Component {
    public Set<GridPoint2> overlappingTiles = new HashSet<>();
}
