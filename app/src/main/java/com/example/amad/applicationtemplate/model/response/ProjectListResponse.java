package com.example.amad.applicationtemplate.model.response;

import com.example.amad.applicationtemplate.model.Project;

import java.util.List;

/**
 * Created by btloc on 12/2/16.
 */

public final class ProjectListResponse extends EndPointResponse {
    private List<Project> response;

    public List<Project> getResponse() {
        return response;
    }
}
