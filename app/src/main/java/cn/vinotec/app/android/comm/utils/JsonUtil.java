package cn.vinotec.app.android.comm.utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by kulend on 2015/12/17.
 */
public class JsonUtil {

    /**
     * Json 转成 Map<>
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> getMapForJson(String jsonStr){
        JSONObject jsonObject ;
        try {
            jsonObject = new JSONObject(jsonStr);

            Iterator<String> keyIter= jsonObject.keys();
            String key;
            Object value ;
            Map<String, Object> valueMap = new HashMap<String, Object>();
            while (keyIter.hasNext()) {
                key = keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value);
            }
            return valueMap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }

}
