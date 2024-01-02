package com.example.felveteli.domain.enums;

public enum VoteOption {
    i("IGEN"),

    n("NEM"),

    t("TARTOZKODAS");

    private final String displayName;

    VoteOption(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
