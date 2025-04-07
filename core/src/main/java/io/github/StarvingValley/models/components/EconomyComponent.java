package io.github.StarvingValley.models.components;

import com.badlogic.ashley.core.Component;

public class EconomyComponent implements Component {
    public float balance;

	public EconomyComponent(float balance) {
		this.balance = balance;
	}
}
