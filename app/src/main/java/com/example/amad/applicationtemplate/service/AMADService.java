package com.example.amad.applicationtemplate.service;

import android.support.annotation.NonNull;

import com.example.amad.applicationtemplate.model.Project;

import java.util.List;

/**
 * Created by btloc on 11/25/16.
 */

public interface AMADService {

    void loadData(@NonNull final AMADServiceCallback<Integer> callback);

    void createProject(@NonNull final Project project,
                       @NonNull final AMADServiceCallback<Project> callback);

    void readProject(@NonNull final Long projectId,
                     @NonNull final AMADServiceCallback<Project> callback);

    void readManyProjects(int page,
                          int size,
                          @NonNull final AMADServiceCallback<List<Project>> callback);

    void updateProject(@NonNull final Project project,
                       @NonNull final AMADServiceCallback<Project> callback);

    void deleteProject(@NonNull final Long projectId,
                       @NonNull final AMADServiceCallback<Boolean> callback);

}
