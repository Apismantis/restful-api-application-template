package com.example.amad.applicationtemplate.service.retrofit;

import com.example.amad.applicationtemplate.exception.AMADException;
import com.example.amad.applicationtemplate.model.response.EndPointResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by btloc on 12/2/16.
 */

public abstract class TranslateRetrofitCallback<T extends EndPointResponse> implements Callback<T> {

    protected final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        AMADException exception = TranslateRetrofitException.translateServiceException(call, response);
        onFinish(call, response.body(), exception);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        AMADException exception = TranslateRetrofitException.translateRetrofitException(call, t);
        onFinish(call, null, exception);
    }

    public void onFinish(Call<T> call, T responseObject, AMADException exception) {
        if (exception == null) {
            LOG.debug("Response is OK. Start parsing json data");
        }
        // TODO: override this method in runtime sub class
    }
}
