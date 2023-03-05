package Vuldata_Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteFile {
    public  void writeFile(String filepath,String content) {
        try {
            // The path of target file must be existed, target file can not be existed
            FileWriter fileWriter = new FileWriter(filepath,true);
            String contentStr = content;
            fileWriter.write(contentStr);
            fileWriter.flush();
            fileWriter.close();
        }catch(Exception e) {
            e.toString();
        }finally {

            }
        }
    }

