package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

import io.github.StarvingValley.models.types.ItemTrade;

public class TradingComponent implements Component {
    public ItemTrade trade;

    public TradingComponent(ItemTrade trade) {
        this.trade = trade;
    }
}
