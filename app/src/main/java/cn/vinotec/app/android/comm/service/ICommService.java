package cn.vinotec.app.android.comm.service;

import cn.vinotec.app.android.comm.entity.ApiReply;
import cn.vinotec.app.android.comm.entity.AppVersionEntity;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ICommService {

    @GET
    Call<ApiReply<AppVersionEntity>> getAppVersion(@Url String url);
}
