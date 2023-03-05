import Request_method.Test_ceye;
import Vul_check.VulRule;
import Vul_check.VulnerabilityDetector;
import Vul_type.APScms_Sqli;
import Vul_type.D_link_Outmessage;
import Vul_type.KedaFileread;
import Vuldata_Controller.Cleantemp;
import Vuldata_Controller.MyVuldatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Vulcheck_start{
    public static void main(String[] args) throws IOException, InterruptedException {
        VulnerabilityDetector vulnerabilityDetector =new VulnerabilityDetector();
        //漏洞检测器
        List<VulRule> vulRules =new ArrayList<VulRule>(); //创建检测规则表
        vulRules.add(new APScms_Sqli());
        vulRules.add(new KedaFileread());
        vulRules.add(new D_link_Outmessage());
        vulnerabilityDetector.setVulRules(vulRules);
        vulnerabilityDetector.detect();
        new Cleantemp().clean();//初始化漏洞temp
        new MyVuldatabase().select();
    }

    public  void test() throws IOException {
        String id = String.valueOf(Thread.currentThread().getId());//获取系统时间戳
        System.out.println(id);
        Runtime.getRuntime().exec("ping " +id+".e5jd12.ceye.io");
        boolean b= Test_ceye.ceye(id);
        System.out.println(b);
    }
}
