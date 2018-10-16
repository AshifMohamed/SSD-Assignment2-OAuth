package com.ashif.oauth.git.Model;

import java.util.ArrayList;

public class RepositoryWrapper {

    private ArrayList<Repository> repositoryList = new ArrayList<>();

    public ArrayList<Repository> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(Repository[] repository) {

        for(Repository rep: repository){

            this.repositoryList.add(rep);
        }

    }

    @Override
    public String toString() {
        return "RepositoryWrapper{" +
                "repositoryList=" + repositoryList +
                '}';
    }
}
