package com.elegion.test.behancer.ui.projects;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v4.widget.SwipeRefreshLayout;

import com.elegion.test.behancer.BuildConfig;
import com.elegion.test.behancer.data.Storage;
import com.elegion.test.behancer.data.model.project.Project;
import com.elegion.test.behancer.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectsViewModel extends ViewModel {

    private Disposable mDisposable;
    private Storage mStorage;
    private ProjectsAdapter.OnItemClickListener itemClickListener;
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<Boolean> isErrorVisible = new MutableLiveData<>();
    private MutableLiveData<List<Project>> projects = new MutableLiveData<>();
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadProjects();
        }
    };

    public ProjectsViewModel(Storage storage, ProjectsAdapter.OnItemClickListener itemClickListener) {
        mStorage = storage;
        this.itemClickListener = itemClickListener;
        projects.setValue(new ArrayList<>());
        loadProjects();
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
                .doOnSubscribe(disposable -> isLoading.postValue(true))
                .doFinally(() -> isLoading.postValue(false))
                .subscribe(
                        response -> {
                            isErrorVisible.postValue(false);
                            projects.postValue(response.getProjects());
                        },
                        throwable -> {
                            isErrorVisible.postValue(true);
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

    public MutableLiveData<List<Project>> getProjects() {
        return projects;
    }

    public void setProjects(MutableLiveData<List<Project>> projects) {
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
