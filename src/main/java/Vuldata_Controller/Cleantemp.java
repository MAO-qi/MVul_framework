package Vuldata_Controller;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cleantemp {
    public void clean() throws IOException, InterruptedException, FileNotFoundException {
        Thread.sleep(60000);
        System.out.println("-------------------------------------------------------------------------------------\n" +
                           "-------------------------------------------------------------------------------------\n"+
                           "----------------------------漏洞扫描结束&&&当前样本存在以下漏洞----------------------------\n");
        String filename= "temp.txt";
        String line;
        final List<String> list = new ArrayList<String>();
        File file =new File(filename);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader br = new BufferedReader(inputStreamReader);
        while ((line=br.readLine())!=null){
            System.out.println(line);
        }

        /*
         * 覆盖原文件
         * */
        FileWriter fileWriter = new FileWriter("temp.txt");
        fileWriter.write("");
        fileWriter.flush();
        fileWriter.close();
    }
}
