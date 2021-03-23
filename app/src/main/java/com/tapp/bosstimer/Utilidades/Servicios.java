package com.tapp.bosstimer.Utilidades;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Servicios {
    @GET("{url}")
    Call<apiBoss> listaBoss(@Path(value = "url", encoded = true) String url);

}
