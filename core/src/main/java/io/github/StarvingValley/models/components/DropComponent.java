package io.github.StarvingValley.models.components;

import java.util.List;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.ItemDrop;

public class DropComponent implements Component {
    public List<ItemDrop> drops;

    public DropComponent(List<ItemDrop> drops) {
        this.drops = drops;
    }
}
