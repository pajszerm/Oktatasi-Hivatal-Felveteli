package com.example.felveteli.domain.enums;

public enum VotingResult {
    F("ELFOGADOTT"),
    U("ELUTASITOTT");

    private final String displayName;

    VotingResult(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
