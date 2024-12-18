package org.aicha.hunters_leagues.domain.enums;

public enum Permission {
    CAN_PARTICIPATE("can_participate"),
    CAN_VIEW_RANKINGS("can_view_rankings"),
    CAN_VIEW_COMPETITIONS("can_view_competitions"),
    CAN_SCORE("can_score"),
    CAN_MANAGE_COMPETITIONS("can_manage_competitions"),
    CAN_MANAGE_USERS("can_manage_users"),
    CAN_MANAGE_SPECIES("can_manage_species"),
    CAN_MANAGE_SETTINGS("can_manage_settings");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}