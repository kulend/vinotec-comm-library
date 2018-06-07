package cn.vinotec.app.android.comm.https;

import cn.vinotec.app.android.comm.VinoApplication;
import cn.vinotec.app.android.comm.entity.ApiReply;
import cn.vinotec.app.android.comm.entity.PaginationResponse;
import cn.vinotec.app.android.comm.utils.AESUtil;
import cn.vinotec.app.android.comm.utils.HttpUtils;
import cn.vinotec.app.android.comm.utils.RequestCacheUtil;
import cn.vinotec.app.android.comm.utils.StringUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class BaseDao {
	
	protected ObjectMapper mObjectMapper = new ObjectMapper();

	protected Context mContext;

	public BaseDao(){
		mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mObjectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mObjectMapper.getDeserializationConfig().setDateFormat(format);

        mContext = VinoApplication.getContext();
	};
	
	public BaseDao(Context context)
	{
		mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mObjectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mObjectMapper.getDeserializationConfig().setDateFormat(format);

        mContext = context;
	}

	protected <T> ApiReply<T> decrypt(String result, TypeReference valueTypeRef)
	{
		ApiReply<T> entity = null;
		try {
			ApiReply<String> encode_data = mObjectMapper.readValue(result, new TypeReference<ApiReply<String>>() {});
			if(encode_data != null)
			{
				String decodeData = AESUtil.decrypt(encode_data.getData());
				entity = new ApiReply<T>();
				entity.setCode(encode_data.getCode());
				entity.setMessage(encode_data.getMessage());
				if(!StringUtil.isBlank(decodeData))
				{
					entity.setData((T) mObjectMapper.readValue(decodeData, valueTypeRef));
				}
				return entity;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public class VinoJsonDateDeserializer extends JsonDeserializer<Date> {
//
//        public VinoJsonDateDeserializer() {
//        }
//
//        @Override
//		public Date deserialize(JsonParser jsonparser,
//								DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String date = jsonparser.getText();
//			try {
//				return format.parse(date);
//			} catch (ParseException e) {
//				throw new RuntimeException(e);
//			}
//		}
//	}

	protected <T> ApiReply<T> get(TypeReference trf, String url)
	{
        ApiReply<T> result = get(trf, url, null);
		return result;
	}

    protected <T> ApiReply<T> get(TypeReference trf, String url, String cacheTag)
    {
        try
        {
            boolean useCache = !StringUtil.isBlank(cacheTag);
            String result = RequestCacheUtil.getRequestContent(mContext, url, "json", cacheTag, useCache);
            return (ApiReply<T>) mObjectMapper.readValue(result, trf);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    protected <T> ApiReply<T> post(String url, NameValuePair... data)
    {
        ApiReply<T> result = post(null, url, data);
        return result;
    }

	protected <T> ApiReply<T> post(TypeReference trf, String url, NameValuePair... data)
	{
        try
        {
            if(trf == null)
            {
                trf = new TypeReference<ApiReply<String>>() {};
            }
            String result = HttpUtils.postByHttpClient(mContext, url, data);
            return (ApiReply<T>) mObjectMapper.readValue(result, trf);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
	}
}
