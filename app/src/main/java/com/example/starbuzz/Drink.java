package com.example.starbuzz;

import org.jetbrains.annotations.NotNull;

public class Drink {

    private String name;
    private String description;
    private int imageResourceId;

    static final Drink[] drinks = {
            new Drink("Latte", "A couple of espresso shots with steamed milk",
                    R.drawable.latte),
            new Drink("Cappuccino", "Espresso, hot milk, and a steamed milk foam",
                    R.drawable.cappuccino),
            new Drink("Filter", "Highest quality beans roasted and brewed fresh",
                    R.drawable.filter)
    };


    private Drink(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    int getImageResourceId() {
        return imageResourceId;
    }

    @NotNull
    @Override
    public String toString() {
        return this.name;
    }
}
