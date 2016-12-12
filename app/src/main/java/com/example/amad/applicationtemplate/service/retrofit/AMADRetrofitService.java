package com.example.amad.applicationtemplate.service.retrofit;

import com.example.amad.applicationtemplate.model.Project;
import com.example.amad.applicationtemplate.model.response.EndPointResponse;
import com.example.amad.applicationtemplate.model.response.ProjectListResponse;
import com.example.amad.applicationtemplate.model.response.ProjectResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by btloc on 11/25/16.
 */
public interface AMADRetrofitService {
    String API_END_POINT_FORMAT = "http://192.168.10.2:9000/v1/";

    @POST("projects")
    Call<ProjectResponse> createProject(@Body Project project);

    @GET("projects/{projectId}")
    Call<ProjectResponse> readProject(@Path("projectId") Long projectId);

    @GET("projects")
    Call<ProjectListResponse> readManyProject(@Query("page") int page, @Query("size") int size);

    @PUT("projects")
    Call<ProjectResponse> updateProject(@Body Project project);

    @DELETE("projects/{projectId}")
    Call<EndPointResponse> deleteProject(@Path("projectId") Long projectId);
}
