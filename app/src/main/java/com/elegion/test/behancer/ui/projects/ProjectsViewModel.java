package com.elegion.test.behancer.ui.projects;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.support.v4.widget.SwipeRefreshLayout;

import com.elegion.test.behancer.BuildConfig;
import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.data.model.project.Project;
import com.elegion.test.behancer.utils.ApiUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectsViewModel {

    private Disposable mDisposable;
    private Storage mStorage;
    private ProjectsAdapter.OnItemClickListener itemClickListener;
    private ObservableBoolean isLoading = new ObservableBoolean(false);
    private ObservableBoolean isErrorVisible = new ObservableBoolean(false);
    private ObservableArrayList<Project> projects = new ObservableArrayList<>();
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadProjects();
        }
    };

    public ProjectsViewModel(Storage storage, ProjectsAdapter.OnItemClickListener itemClickListener) {
        mStorage = storage;
        this.itemClickListener = itemClickListener;
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public void loadProjects() {
        mDisposable = ApiUtils.getApiService().getProjects(BuildConfig.API_QUERY)
                .doOnSuccess(response -> mStorage.insertProjects(response))
                .onErrorReturn(throwable ->
                        ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass()) ? mStorage.getProjects() : null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> isLoading.set(true))
                .doFinally(() -> isLoading.set(false))
                .subscribe(
                        response -> {
                            isErrorVisible.set(false);
                            projects.addAll(response.getProjects());
                        },
                        throwable -> {
                            isErrorVisible.set(true);
                        });
    }

    public ProjectsAdapter.OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public ObservableBoolean getIsErrorVisible() {
        return isErrorVisible;
    }

    public ObservableArrayList<Project> getProjects() {
        return projects;
    }

    public void dispatchDetach() {
        mStorage = null;
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
