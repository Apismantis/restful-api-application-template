package com.example.amad.applicationtemplate.service;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;

import com.example.amad.applicationtemplate.app.AMADApplication;
import com.example.amad.applicationtemplate.exception.AMADException;
import com.example.amad.applicationtemplate.model.Project;
import com.example.amad.applicationtemplate.model.response.EndPointResponse;
import com.example.amad.applicationtemplate.model.response.ProjectListResponse;
import com.example.amad.applicationtemplate.model.response.ProjectResponse;
import com.example.amad.applicationtemplate.service.retrofit.AMADRetrofitService;
import com.example.amad.applicationtemplate.service.retrofit.AddHeaderInterceptor;
import com.example.amad.applicationtemplate.service.retrofit.GsonDateFormatAdapter;
import com.example.amad.applicationtemplate.service.retrofit.RetryInterceptor;
import com.example.amad.applicationtemplate.service.retrofit.TranslateRetrofitCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by btloc on 11/25/16.
 */

public class AMADServiceImpl implements AMADService {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private Context context;
    private AMADRetrofitService defaultService;
    private final HttpLoggingInterceptor defaultLogging;
    private final Gson defaultGson;

    private static final long timeOut = 60; // 60 seconds

    private AMADServiceImpl() {
        // Don't allow default constructor outside
        defaultLogging = null;
        defaultGson = null;
        defaultService = null;
    }

    public AMADServiceImpl(AMADApplication application) {

        context = application.getApplicationContext();
        defaultLogging = newDefaultLogging();
        defaultGson = newDefaultGson();
        defaultService = null;
    }

    private HttpLoggingInterceptor newDefaultLogging() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (AMADApplication.isInDebugMode()) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return logging;
    }

    private Gson newDefaultGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonDateFormatAdapter())
                .create();
    }

    private String getToken() {
        final String encodeString = "AnyUserName" + ":" + "AnyPassword";
        final String token = Base64.encodeToString(encodeString.getBytes(), Base64.NO_WRAP);
        return token;
    }

    private AMADRetrofitService getService(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new AddHeaderInterceptor(token))
                .addInterceptor(defaultLogging)
                .addInterceptor(new RetryInterceptor())
                .readTimeout(timeOut, TimeUnit.SECONDS)
                .connectTimeout(timeOut, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AMADRetrofitService.API_END_POINT_FORMAT)
                .addConverterFactory(GsonConverterFactory.create(defaultGson))
                .client(client)
                .build();

        return retrofit.create(AMADRetrofitService.class);
    }

    private AMADRetrofitService getService() {
        if (defaultService == null) {
            defaultService = getService(getToken());
        }
        return defaultService;
    }

    @Override
    public void loadData(@NonNull final AMADServiceCallback<Integer> callback) {
        new AsyncTask<Void, Void, Integer>() {
            private Exception exception;

            @Override
            protected void onPreExecute() {
                callback.setAsyncTask(this);
                callback.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    // Normally we would do some work here, like load data from server.
                    // For our sample, we just sleep for 3 seconds.
                    long endTime = System.currentTimeMillis() + 3*1000;
                    while (System.currentTimeMillis() < endTime) {
                        synchronized (this) {
                            wait(endTime - System.currentTimeMillis());
                        }
                    }
                } catch (Exception exception) {
                    LOG.error(exception.getMessage());
                    this.exception = new AMADException("Undefined exception", exception);
                    return 0;
                }

                String token = getToken();
                LOG.debug("Token " + token);

                if (TextUtils.isEmpty(token)) {
                    return 0;
                } else {
                    return  1;
                }
            }

            @Override
            protected void onPostExecute(Integer result) {
                callback.onPostExecute(result, exception);
            }
        }.execute();
    }

    @Override
    public void createProject(@NonNull final Project project,
                              @NonNull final AMADServiceCallback<Project> callback) {

        Call<ProjectResponse> callCreateProject = getService().createProject(project);
        callback.setCall(callCreateProject);

        callback.onPreExecute();
        callCreateProject.enqueue(new TranslateRetrofitCallback<ProjectResponse>(){
            @Override
            public void onFinish(Call<ProjectResponse> call,
                                 ProjectResponse responseObject,
                                 AMADException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExecute(responseObject.getResponse(), exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    @Override
    public void readProject(@NonNull final Long projectId,
                            @NonNull final AMADServiceCallback<Project> callback) {
        Call<ProjectResponse> callReadProject = getService().readProject(projectId);
        callback.setCall(callReadProject);

        callback.onPreExecute();
        callReadProject.enqueue(new TranslateRetrofitCallback<ProjectResponse>(){
            @Override
            public void onFinish(Call<ProjectResponse> call,
                                 ProjectResponse responseObject,
                                 AMADException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExecute(responseObject.getResponse(), exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    @Override
    public void readManyProjects(int page,
                                 int size,
                                 @NonNull final AMADServiceCallback<List<Project>> callback) {
        Call<ProjectListResponse> callReadManyProjects = getService().readManyProject(page, size);
        callback.setCall(callReadManyProjects);

        callback.onPreExecute();
        callReadManyProjects.enqueue(new TranslateRetrofitCallback<ProjectListResponse>(){
            @Override
            public void onFinish(Call<ProjectListResponse> call,
                                 ProjectListResponse responseObject,
                                 AMADException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    if (responseObject.getResponse() == null) {
                        callback.onPostExecute(new Vector<Project>(), exception);
                    } else {
                        callback.onPostExecute(responseObject.getResponse(), exception);
                    }
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    @Override
    public void updateProject(@NonNull final Project project,
                              @NonNull final AMADServiceCallback<Project> callback) {
        Call<ProjectResponse> callUpdateProject = getService().updateProject(project);
        callback.setCall(callUpdateProject);

        callback.onPreExecute();
        callUpdateProject.enqueue(new TranslateRetrofitCallback<ProjectResponse>(){
            @Override
            public void onFinish(Call<ProjectResponse> call,
                                 ProjectResponse responseObject,
                                 AMADException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    callback.onPostExecute(responseObject.getResponse(), exception);
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

    @Override
    public void deleteProject(@NonNull final Long projectId,
                              @NonNull final AMADServiceCallback<Boolean> callback) {
        Call<EndPointResponse> callDeleteProject = getService().deleteProject(projectId);
        callback.setCall(callDeleteProject);

        callback.onPreExecute();
        callDeleteProject.enqueue(new TranslateRetrofitCallback<EndPointResponse>(){
            @Override
            public void onFinish(Call<EndPointResponse> call,
                                 EndPointResponse responseObject,
                                 AMADException exception) {
                super.onFinish(call, responseObject, exception);

                if (exception == null && responseObject != null) {
                    if ("ok".equals(responseObject.getResult())) {
                        callback.onPostExecute(true, exception);
                    } else {
                        callback.onPostExecute(false, exception);
                    }
                } else {
                    callback.onPostExecute(null, exception);
                }
            }
        });
    }

}
