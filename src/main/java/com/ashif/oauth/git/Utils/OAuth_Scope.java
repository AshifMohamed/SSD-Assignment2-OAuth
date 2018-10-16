package com.ashif.oauth.git.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class OAuth_Scope {

    private static final ArrayList<String> scopes =new ArrayList<>(Arrays.asList("repo","user"));

    public static ArrayList<String> getScopes() {
        return scopes;
    }
}
