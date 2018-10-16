package com.ashif.oauth.git.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Permission {

    @JsonProperty("admin")
    private boolean admin;

    @JsonProperty("push")
    private boolean push;

    @JsonProperty("pull")
    private boolean pull;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isPull() {
        return pull;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }
}
