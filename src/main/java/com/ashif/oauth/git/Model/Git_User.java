package com.ashif.oauth.git.Model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Git_User {

    @JsonProperty("login")
    private String login;

    @JsonProperty("name")
    private String name;

    @JsonProperty("public_repos")
    private String public_repos;

    @JsonProperty("public_gists")
    private String public_gists;

    @JsonProperty("followers")
    private String followers;

    @JsonProperty("following")
    private String following;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("repos_url")
    private String reposUrl;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(String public_repos) {
        this.public_repos = public_repos;
    }

    public String getPublic_gists() {
        return public_gists;
    }

    public void setPublic_gists(String public_gists) {
        this.public_gists = public_gists;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    @Override
    public String toString() {
        return "Git_User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", public_repos='" + public_repos + '\'' +
                ", public_gists='" + public_gists + '\'' +
                ", followers='" + followers + '\'' +
                ", following='" + following + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", reposUrl='" + reposUrl + '\'' +
                '}';
    }
}
