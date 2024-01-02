package com.example.felveteli.domain.enums;

public enum VotingType {
    j("JELENLETI"),
    e("EGYSZERU"),
    m("MINOSITETT");

    private final String displayName;

    VotingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
