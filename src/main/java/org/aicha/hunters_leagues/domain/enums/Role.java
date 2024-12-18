package org.aicha.hunters_leagues.domain.enums;

import java.util.Set;

public enum Role {
    MEMBER(Set.of(
            Permission.CAN_PARTICIPATE,
            Permission.CAN_VIEW_RANKINGS,
            Permission.CAN_VIEW_COMPETITIONS
    )),
    JURY(Set.of(
            Permission.CAN_PARTICIPATE,
            Permission.CAN_VIEW_RANKINGS,
            Permission.CAN_VIEW_COMPETITIONS,
            Permission.CAN_SCORE
    )),
    ADMIN(Set.of(
            Permission.CAN_PARTICIPATE,
            Permission.CAN_VIEW_RANKINGS,
            Permission.CAN_VIEW_COMPETITIONS,
            Permission.CAN_SCORE,
            Permission.CAN_MANAGE_COMPETITIONS,
            Permission.CAN_MANAGE_USERS,
            Permission.CAN_MANAGE_SPECIES,
            Permission.CAN_MANAGE_SETTINGS
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}