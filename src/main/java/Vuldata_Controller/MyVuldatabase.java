package Vuldata_Controller;

import com.zsj.poji.vuldatas;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class MyVuldatabase {
    public void select() throws IOException {
        String resource ="mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession =sqlSessionFactory.openSession();
        List<vuldatas> keda = sqlSession.selectList("com.zsj.mapper.VulMapper.select_keda");
        List<vuldatas> aps = sqlSession.selectList("com.zsj.mapper.VulMapper.select_Apssql");
        List<vuldatas> Dlink = sqlSession.selectList("com.zsj.mapper.VulMapper.select_Dlink");
        int vul_code;
        System.out.println("下一步程序结束or继续查询: -1(程序结束);0(apscms_sqli);1(kedafileread);2(Dlink)");
        Scanner scanner = new Scanner(System.in);

        while ((vul_code=scanner.nextInt())!=-1){
            switch (vul_code) {
                case 0:
                    for (vuldatas vuldatas :aps){
                        System.out.println(vuldatas.toString());
                    }
                    break;
                case 1:
                    for (vuldatas vuldatas :keda){
                        System.out.println(vuldatas.toString());
                    }
                    break;
                case 2:
                    for (vuldatas vuldatas :Dlink){
                        System.out.println(vuldatas.toString());
                    }
                    break;
                default:
                    System.out.println("暂无该漏洞的记录");
                    break;
            }
        }

    }
}
