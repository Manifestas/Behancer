package com.elegion.test.behancer.ui.projects;

import com.elegion.test.behancer.common.BasePresenter;
import com.elegion.test.behancer.data.Storage;

public class ProjectsPresenter extends BasePresenter {

    private ProjectsView view;
    private Storage storage;

    public ProjectsPresenter(ProjectsView view, Storage storage) {
        this.view = view;
        this.storage = storage;
    }

    public void getProjects() {

    }

    public void openProfileFragment() {

    }
}
