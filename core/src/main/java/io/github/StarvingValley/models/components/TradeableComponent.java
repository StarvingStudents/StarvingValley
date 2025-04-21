package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class TradeableComponent implements Component {
    public int price;

    public TradeableComponent(int price) {
        this.price = price;
    }
}
