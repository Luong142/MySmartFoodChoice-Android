package com.example.myfoodchoice.ModelSignUp;

public enum ActivityLevel {
    SEDENTARY("Sedentary"),
    LIGHTLY_ACTIVE("Lightly Active"),
    MODERATELY_ACTIVE("Moderately Active"),
    VERY_ACTIVE("Very Active"),
    EXTREMELY_ACTIVE("Extremely Active");

    private final String displayName;

    ActivityLevel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
