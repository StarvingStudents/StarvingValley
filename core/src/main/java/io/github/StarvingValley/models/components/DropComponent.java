package io.github.StarvingValley.models.components;

import java.util.List;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.ItemStack;

// TODO: Rename to clearer name
public class DropComponent implements Component {
    public List<ItemStack> drops;

    public DropComponent(List<ItemStack> drops) {
        this.drops = drops;
    }
}
