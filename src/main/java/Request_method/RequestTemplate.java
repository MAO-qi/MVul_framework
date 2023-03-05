package Request_method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.util.URI;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RequestTemplate {
    /*自定义请求头*/
    static String[] user= new String[]{
            "Mozilla/5.0 (compatible; Baiduspider/2.0;+http://www.baidu.com/search/spider.html) safari 5.1 – MAC",
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "User-Agent:Mozilla/5.0 (Windows; U; Windows NT 6.1; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50",
            "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0",
            "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)",
            "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:2.0.1) Gecko/20100101 Firefox/4.0.1",
            "Opera/9.80 (Macintosh; Intel Mac OS X 10.6.8; U; en) Presto/2.8.131 Version/11.11",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_0) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Maxthon 2.0)",
            "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; TencentTraveler 4.0)",};
    public static int agent(){
        Random random =new Random();
        return   random.nextInt(user.length);
    }
    /*忽略证书安全性*/
    static {
        TrustManager[] trustManager=new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

            }
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try{
            SSLContext sslContext=SSLContext.getInstance("TLS");
            sslContext.init(null,trustManager,new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        }catch (Exception e){

        }
    }
    private final static HostnameVerifier Do_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    /*get无参数*/
    public static Map<String,Object> GetBody(Map<String ,String > requestproprety, String uri,int timeout){
        boolean isHttps= true;
        if (uri.indexOf("Http")==-1&&uri.indexOf("Https")==-1&&uri.indexOf("http")==-1&&uri.indexOf("https")==-1){
            HttpsURLConnection httpsURLConnection =null;
            try{
                URL url =new URL("Https://"+uri);
                httpsURLConnection=(HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(Do_NOT_VERIFY);   //忽略证书安全性
                httpsURLConnection.setConnectTimeout(2000);
                httpsURLConnection.setReadTimeout(1000);
                httpsURLConnection.connect();
            }catch (Exception E){
                isHttps=false;
            }finally {
                if (isHttps){
                    uri="Https://"+uri;
                }else {
                    uri="Http://"+uri;
                }
                if (null!=httpsURLConnection){
                    httpsURLConnection.disconnect();
                }
            }
        }
        Map<String,Object> response=new HashMap<String, Object>();
        StringBuilder result= new StringBuilder();

        /*协议连接*/

        HttpURLConnection connection =null;
        try {
            URL url = new URL(uri);
            if (url.getProtocol().toLowerCase().equals("https:")){
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(Do_NOT_VERIFY);
                connection=https;
            }else {
                connection= (HttpURLConnection) url.openConnection();
            }
            /*请求头设置*/

            if (requestproprety.get("User-Agent")==null){
                connection.setRequestProperty("User-Agent",user[agent()]);
            }

            if (null==requestproprety.get("Content-Type")){
                connection.setRequestProperty("Content-Type","applocation/x-www-form-urlencode;charset=UTF-8");
            }
            /*用户自定义请求头的添加*/
            for (Map.Entry<String,String> entry: requestproprety.entrySet()){
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            try {
                connection.connect();
                int rescode=connection.getResponseCode();
                Map<String, List<String>> resproperty = connection.getHeaderFields();
                InputStreamReader inputStreamReader=null;
                if (rescode==200){
                    inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                }else {
                    inputStreamReader=new InputStreamReader(connection.getErrorStream(),"UTF-8");
                }
                /*数据流的读取存储*/
                if (inputStreamReader!=null){
                    BufferedReader br =new BufferedReader(inputStreamReader);
                    String line=null;
                    while ((line=br.readLine())!=null){
                        result.append(line+"\n");
                        if (line==null){
                            inputStreamReader.close();
                        }
                    }
                }
                connection.disconnect();
                response.put("Code",rescode);
                response.put("Content",result.toString());
                response.put("resproperty",resproperty);
            }catch (Exception e) {

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /*get含参数*/
    public static Map<String,Object> GetBodyparam(Map<String ,String > requestproprety, String uri,String param,int timeout){
        boolean isHttps= true;
        if (uri.indexOf("Http")==-1&&uri.indexOf("Https")==-1&&uri.indexOf("http")==-1&&uri.indexOf("https")==-1){
            HttpsURLConnection httpsURLConnection =null;
            try{
                URL url =new URL("Https://"+uri);
                httpsURLConnection=(HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(Do_NOT_VERIFY);   //忽略证书安全性
                httpsURLConnection.setConnectTimeout(2000);
                httpsURLConnection.setReadTimeout(1000);
                httpsURLConnection.connect();
            }catch (Exception E){
                isHttps=false;
            }finally {
                if (isHttps){
                    uri="Https://"+uri+"?"+param;
                }else {
                    uri="Http://"+uri+"?"+param;
                }
                if (null!=httpsURLConnection){
                    httpsURLConnection.disconnect();
                }
            }
        }
        System.out.println(uri);
        Map<String,Object> response=new HashMap<String, Object>();
        StringBuilder result= new StringBuilder();

        /*协议连接*/

        HttpURLConnection connection =null;
        try {
            URL url = new URL(uri);
            if (url.getProtocol().toLowerCase().equals("https:")){
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(Do_NOT_VERIFY);
                connection=https;
            }else {
                connection= (HttpURLConnection) url.openConnection();
            }

            /*请求头设置*/

            if (requestproprety.get("User-Agent")==null){
                connection.setRequestProperty("User-Agent",user[agent()]);
            }

            if (null==requestproprety.get("Content-Type")){
                connection.setRequestProperty("Content-Type","applocation/x-www-form-urlencode;charset=UTF-8");
            }
            /*用户自定义请求头的添加*/
            for (Map.Entry<String,String> entry: requestproprety.entrySet()){
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }

            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            try {
                connection.connect();
                int rescode=connection.getResponseCode();
                Map<String, List<String>> resproperty = connection.getHeaderFields();
                InputStreamReader inputStreamReader=null;
                if (rescode==200){
                    inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                }else {
                    inputStreamReader=new InputStreamReader(connection.getErrorStream(),"UTF-8");
                }
                /*数据流的读取存储*/
                if (inputStreamReader!=null){
                    BufferedReader br =new BufferedReader(inputStreamReader);
                    String line=null;
                    while ((line=br.readLine())!=null){
                        result.append(line+"\n");
                        if (line==null){
                            inputStreamReader.close();
                        }
                    }
                }
                connection.disconnect();
                response.put("Code",rescode);
                response.put("Content",result.toString());
                response.put("resproperty",resproperty);
            }catch (Exception e){
                System.out.println(uri+"    连接超时...");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }


    /*Post请求*/
    public static Map<String,Object> PostBody(Map<String ,String > requestproprety, String uri,String param,int timeout){
        boolean isHttps= true;
        if (uri.indexOf("Http")==-1&&uri.indexOf("Https")==-1&&uri.indexOf("http")==-1&&uri.indexOf("https")==-1){
            HttpsURLConnection httpsURLConnection =null;
            try{
                URL url =new URL("Https://"+uri);
                httpsURLConnection=(HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(Do_NOT_VERIFY);   //忽略证书安全性
                httpsURLConnection.setConnectTimeout(2000);
                httpsURLConnection.setReadTimeout(1000);
                httpsURLConnection.connect();
            }catch (Exception E){
                isHttps=false;
            }finally {
                if (isHttps){
                    uri="Https://"+uri+"?"+param;
                }else {
                    uri="Http://"+uri+"?"+param;
                }
                if (null!=httpsURLConnection){
                    httpsURLConnection.disconnect();
                }
            }
        }
        Map<String,Object> response=new HashMap<String, Object>();
        StringBuilder result= new StringBuilder();

        /*协议连接*/

        HttpURLConnection connection =null;
        try {
            URL url = new URL(uri);
            if (url.getProtocol().toLowerCase().equals("https:")){
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(Do_NOT_VERIFY);
                connection=https;
            }else {
                connection= (HttpURLConnection) url.openConnection();
            }
            /*请求头设置*/

            if (requestproprety.get("User-Agent")==null){
                connection.setRequestProperty("User-Agent",user[agent()]);
            }

            if (null==requestproprety.get("Content-Type")){
                connection.setRequestProperty("Content-Type","applocation/x-www-form-urlencode;charset=UTF-8");
            }
            /*用户自定义请求头的添加*/
            for (Map.Entry<String,String> entry: requestproprety.entrySet()){
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }
            try {
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                PrintWriter printWriter =new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
                printWriter.write(param);
                printWriter.flush();
                printWriter.close();

                int rescode=connection.getResponseCode();
                Map<String, List<String>> resproperty = connection.getHeaderFields();
                InputStreamReader inputStreamReader=null;
                if (rescode==200){
                    inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                }else {
                    inputStreamReader=new InputStreamReader(connection.getErrorStream(),"UTF-8");
                }
                /*数据流的读取存储*/
                if (inputStreamReader!=null){
                    BufferedReader br =new BufferedReader(inputStreamReader);
                    String line=null;
                    while ((line=br.readLine())!=null){
                        result.append(line+"\n");
                        if (line==null){
                            inputStreamReader.close();
                        }
                    }
                }
                connection.disconnect();
                response.put("Code",rescode);
                response.put("Content",result.toString());
                response.put("resproperty",resproperty);
            }catch (Exception e){

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    /*json请求*/
    public static Map<String,Object> JsonBody(Map<String ,String > requestproprety, String uri,String param,int timeout){
        boolean isHttps= true;
        if (uri.indexOf("Http")==-1&&uri.indexOf("Https")==-1&&uri.indexOf("http")==-1&&uri.indexOf("https")==-1){
            HttpsURLConnection httpsURLConnection =null;
            try{
                URL url =new URL("Https://"+uri);
                httpsURLConnection=(HttpsURLConnection) url.openConnection();
                httpsURLConnection.setHostnameVerifier(Do_NOT_VERIFY);   //忽略证书安全性
                httpsURLConnection.setConnectTimeout(2000);
                httpsURLConnection.setReadTimeout(1000);
                httpsURLConnection.connect();
            }catch (Exception E){
                isHttps=false;
            }finally {
                if (isHttps){
                    uri="Https://"+uri+"?"+param;
                }else {
                    uri="Http://"+uri+"?"+param;
                }
                if (null!=httpsURLConnection){
                    httpsURLConnection.disconnect();
                }
            }
        }
        Map<String,Object> response=new HashMap<String, Object>();
        StringBuilder result= new StringBuilder();

        /*协议连接*/

        HttpURLConnection connection =null;
        try {
            URL url = new URL(uri);
            if (url.getProtocol().toLowerCase().equals("https:")){
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(Do_NOT_VERIFY);
                connection=https;
            }else {
                connection= (HttpURLConnection) url.openConnection();
            }

            /*请求头设置*/

            if (requestproprety.get("User-Agent")==null){
                connection.setRequestProperty("User-Agent",user[agent()]);
            }

            if (null==requestproprety.get("Content-Type")){
                connection.setRequestProperty("Content-Type","applocation/json;charset=UTF-8");
            }
            /*用户自定义请求头的添加*/
            for (Map.Entry<String,String> entry: requestproprety.entrySet()){
                connection.setRequestProperty(entry.getKey(),entry.getValue());
            }
            try {
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                PrintWriter printWriter =new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
                printWriter.write(param);
                printWriter.flush();
                printWriter.close();


                int rescode=connection.getResponseCode();
                Map<String, List<String>> resproperty = connection.getHeaderFields();
                InputStreamReader inputStreamReader=null;
                if (rescode==200){
                    inputStreamReader=new InputStreamReader(connection.getInputStream(),"UTF-8");
                }else {
                    inputStreamReader=new InputStreamReader(connection.getErrorStream(),"UTF-8");
                }
                /*数据流的读取存储*/
                if (inputStreamReader!=null){
                    BufferedReader br =new BufferedReader(inputStreamReader);
                    String line=null;
                    while ((line=br.readLine())!=null){
                        result.append(line+"\n");
                        if (line==null){
                            inputStreamReader.close();
                        }
                    }
                }
                connection.disconnect();
                response.put("Code",rescode);
                response.put("Content",result.toString());
                response.put("resproperty",resproperty);
            }catch (Exception e){
                System.out.println(uri+"   连接超时");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
