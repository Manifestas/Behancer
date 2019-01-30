package com.elegion.test.behancer.ui.projects;

import com.elegion.test.behancer.data.model.project.Project;
import com.elegion.test.behancer.utils.DateUtils;

public class ProjectListItemViewModel {

    private static final int FIRST_OWNER_INDEX = 0;

    private String imageUrl;
    private String name;
    private String userName;
    private String publishedOn;

    public ProjectListItemViewModel(Project project) {
        imageUrl = project.getCover().getPhotoUrl();
        name = project.getName();
        userName = project.getOwners().get(FIRST_OWNER_INDEX).getUsername();
        publishedOn = DateUtils.format(project.getPublishedOn());
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPublishedOn() {
        return publishedOn;
    }
}
