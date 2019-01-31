package com.elegion.test.behancer.utils;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.elegion.test.behancer.data.model.project.Project;
import com.elegion.test.behancer.ui.projects.ProjectsAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomBindingAdapter {

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView imageView, String imageUrl) {
        Picasso.with(imageView.getContext())
                .load(imageUrl)
                .into(imageView);
    }

    @BindingAdapter({"bind:data", "bind:clickHandler"})
    public static void configureRecyclerView(RecyclerView recyclerView, List<Project> projects,
                                             ProjectsAdapter.OnItemClickListener listener) {
        ProjectsAdapter adapter = new ProjectsAdapter(projects, listener);
        recyclerView.setAdapter(adapter);

    }

    @BindingAdapter({"bind:refreshState", "bind:onRefresh"})
    public static void configureSwipeRefreshLayout(SwipeRefreshLayout layout,
                                                   boolean isLoading,
                                                   SwipeRefreshLayout.OnRefreshListener listener) {
        layout.setOnRefreshListener(listener);
        layout.post(() -> layout.setRefreshing(isLoading));

    }
}
