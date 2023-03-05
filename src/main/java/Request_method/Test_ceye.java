package Request_method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_ceye {
    public static boolean ceye( String filter){
        RequestTemplate requestTemplate = new RequestTemplate();
        boolean flag=false;
        String token="0f1d1578ec202efb07bc09f2bc4b8a53";
        Map<String ,Object> ceyeobject=requestTemplate.GetBodyparam(new HashMap<String, String>(),"api.ceye.io/v1/records","token="+token+"&type=dns&filter="+filter,8000);
        Object content=ceyeobject.get("Content");
        System.out.println(content);
        JSONObject jsonObject = JSON.parseObject((String) content);
        List data =(List) jsonObject.get("data");
        if (data.size()>0)
            return flag=true;
        else
            return flag;
    }
}
