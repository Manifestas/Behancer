package com.elegion.test.behancer.ui.projects;

import com.elegion.test.behancer.data.model.project.RichProject;
import com.elegion.test.behancer.utils.DateUtils;

public class ProjectListItemViewModel {

    private static final int FIRST_OWNER_INDEX = 0;

    private String imageUrl;
    private String name;
    private String userName;
    private String publishedOn;

    public ProjectListItemViewModel(RichProject item) {
        imageUrl = item.project.getCover().getPhotoUrl();
        name = item.project.getName();
        publishedOn = DateUtils.format(item.project.getPublishedOn());
        if (item.owners != null && item.owners.size() != 0) {
            userName = item.owners.get(FIRST_OWNER_INDEX).getUsername();
        }
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
