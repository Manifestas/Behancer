package com.elegion.test.behancer.ui.projects;

import android.support.v7.widget.RecyclerView;

import com.elegion.test.behancer.data.model.project.Project;
import com.elegion.test.behancer.databinding.ProjectBinding;

/**
 * Created by Vladislav Falzan.
 */

public class ProjectsHolder extends RecyclerView.ViewHolder {

    private ProjectBinding binding;

    public ProjectsHolder(ProjectBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Project item, ProjectsAdapter.OnItemClickListener onItemClickListener) {
        binding.setProject(new ProjectListItemViewModel(item));
        binding.executePendingBindings();
    }
}
