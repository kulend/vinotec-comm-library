package cn.vinotec.app.android.comm.https;

import android.content.Context;
import cn.vinotec.app.android.comm.entity.ApiReply;
import cn.vinotec.app.android.comm.entity.AppVersionEntity;
import cn.vinotec.app.android.comm.utils.RequestCacheUtil;
import org.codehaus.jackson.type.TypeReference;

public class AppCommDao extends BaseDao 
{
	public AppCommDao(Context context)
	{
		super(context);
	}

	/* 取得App版本详情 */
	public ApiReply<AppVersionEntity> DoGetAppVersionInfo(String appkey, String version)
	{
		ApiReply<AppVersionEntity> entity;
		try
		{
			String result = RequestCacheUtil.getRequestContent(mContext, String.format("https://datacenter.vinotec.cn:8070/api/version/check?appkey=%s&version=%s", appkey, version), "json", "0", false);
			entity = mObjectMapper.readValue(result, new TypeReference<ApiReply<AppVersionEntity>>() {
			});
			return entity;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
