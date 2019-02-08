package com.elegion.test.behancer.ui.projects;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.widget.SwipeRefreshLayout;

import com.elegion.test.behancer.BuildConfig;
import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.data.model.project.ProjectResponse;
import com.elegion.test.behancer.data.model.project.RichProject;
import com.elegion.test.behancer.utils.ApiUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectsViewModel extends ViewModel {

    private Disposable mDisposable;
    private Storage mStorage;
    private ProjectsAdapter.OnItemClickListener itemClickListener;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isErrorVisible = new MutableLiveData<>();
    private LiveData<List<RichProject>> projects;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = this::updateProjects;

    public ProjectsViewModel(Storage storage, ProjectsAdapter.OnItemClickListener itemClickListener) {
        mStorage = storage;
        this.itemClickListener = itemClickListener;
        projects = storage.getProjectsLive();
        updateProjects();
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    private void updateProjects() {
        mDisposable = ApiUtils.getApiService().getProjects(BuildConfig.API_QUERY)
                .map(ProjectResponse::getProjects)
                .doOnSubscribe(disposable -> isLoading.postValue(true))
                .doFinally(() -> isLoading.postValue(false))
                .doOnSuccess(response -> isErrorVisible.postValue(false))
                .subscribeOn(Schedulers.io())
                .subscribe(
                        response -> mStorage.insertProjects(response),
                        throwable -> {
                            isErrorVisible.postValue(true);
                            boolean value = projects.getValue() == null || projects.getValue().size() == 0;
                            isErrorVisible.postValue(value);
                        });
    }

    public ProjectsAdapter.OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(MutableLiveData<Boolean> isLoading) {
        this.isLoading = isLoading;
    }

    public MutableLiveData<Boolean> getIsErrorVisible() {
        return isErrorVisible;
    }

    public void setIsErrorVisible(MutableLiveData<Boolean> isErrorVisible) {
        this.isErrorVisible = isErrorVisible;
    }

    public LiveData<List<RichProject>> getProjects() {
        return projects;
    }

    public void setProjects(LiveData<List<RichProject>> projects) {
        this.projects = projects;
    }

    @Override
    public void onCleared() {
        mStorage = null;
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }
}
