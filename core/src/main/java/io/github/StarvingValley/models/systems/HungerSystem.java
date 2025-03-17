package io.github.StarvingValley.models.systems;

public class HungerSystem {
    private int hungerPoints; 
    private int maxHungerPoints; 

    public HungerSystem(int maxHungerPoints) {
        this.maxHungerPoints = maxHungerPoints; 
        this.hungerPoints = maxHungerPoints; 
    }

    public int getHungerPoints() {
        return hungerPoints;
    }

    public void setHungerPoints(int hungerPoints) {
        if (hungerPoints < 0) {
            this.hungerPoints = 0; 
        }
        else if (hungerPoints > maxHungerPoints) {
            this.hungerPoints = maxHungerPoints; 
        }
        else {
            this.hungerPoints = hungerPoints;
        }
    }

    public int getMaxHungerPoints() {
        return maxHungerPoints;
    }

    public void setMaxHungerPoints(int maxHungerPoints) {
        this.maxHungerPoints = maxHungerPoints;
    }

    public void decreaseHungerPoints(int amount) {
        setHungerPoints(getHungerPoints() - amount); 
    }

    public void increaseHungerPoints(int amount) {
        setHungerPoints(getHungerPoints() + amount);
    }
}
