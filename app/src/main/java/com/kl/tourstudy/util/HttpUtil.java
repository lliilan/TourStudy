package com.kl.tourstudy.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kl.tourstudy.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kl.tourstudy.util.PreferenceUtil.IP;
import static com.kl.tourstudy.util.PreferenceUtil.PROJECT;

/**
 * Created by Administrator on 2016/12/18.
 */
public class HttpUtil {
    public static String HttpURL=IP+PROJECT;
    private static String nameTest="用户";
    private static int imgTest= R.mipmap.icon;
    public static int count=0;
    public static void sendHttpRequest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    url.openStream();
                    Log.e("请求的地址是",address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法

                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    public static void sendHttpRequestTest(final String address,
                                       final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (listener != null) {
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        // 回调onError()方法
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     *多文件上传
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     *   <FORM METHOD=POST ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     <INPUT TYPE="text" NAME="name">
     <INPUT TYPE="text" NAME="id">
     <input type="file" name="imagefile"/>
     <input type="file" name="zip"/>
     </FORM>
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param files 上传文件
     */
    public static void postTest(final String path, final Map<String, String> params, final FormFile[] files){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(path);
                    final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
                    final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

                    int fileDataLength = 0;
                    for(FormFile uploadFile : files){//得到文件类型数据的总长度
                        StringBuilder fileExplain = new StringBuilder();
                        fileExplain.append("--");
                        fileExplain.append(BOUNDARY);
                        fileExplain.append("\r\n");
                        fileExplain.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
                        fileExplain.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
                        fileExplain.append("\r\n");
                        fileDataLength += fileExplain.length();
                        if(uploadFile.getInStream()!=null){
                            fileDataLength += uploadFile.getFile().length();
                        }else{
                            fileDataLength += uploadFile.getData().length;
                        }
                    }
                    StringBuilder textEntity = new StringBuilder();
                    for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
                        textEntity.append("--");
                        textEntity.append(BOUNDARY);
                        textEntity.append("\r\n");
                        textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
                        textEntity.append(entry.getValue());
                        textEntity.append("\r\n");
                    }
                    //计算传输给服务器的实体数据总长度
                    int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;
//        int port = url.getPort()==-1 ? 80 : url.getPort();
//        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
//        OutputStream outStream = socket.getOutputStream();
                    HttpURLConnection connection = null;
                    try {
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.setUseCaches(false);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Connection", "Keep-Alive");
                        connection.setRequestProperty("Charset", "utf-8");
                        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
                        OutputStream outStream=connection.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
//        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
//        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
                        for(FormFile uploadFile : files){
                            StringBuilder fileEntity = new StringBuilder();
                            fileEntity.append("--");
                            fileEntity.append(BOUNDARY);
                            fileEntity.append("\r\n");
                            fileEntity.append("Content-Disposition: form-data;name=\""+ uploadFile.getParameterName()+"\";filename=\""+ uploadFile.getFilname() + "\"\r\n");
                            fileEntity.append("Content-Type: "+ uploadFile.getContentType()+"\r\n\r\n");
                            outStream.write(fileEntity.toString().getBytes());
                            if(uploadFile.getInStream()!=null){
                                byte[] buffer = new byte[1024];
                                int len = 0;
                                while((len = uploadFile.getInStream().read(buffer, 0, 1024))!=-1){
                                    outStream.write(buffer, 0, len);
                                }
                                uploadFile.getInStream().close();
                            }else{
                                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
                            }
                            outStream.write("\r\n".getBytes());
                        }
                        //下面发送数据结束标志，表示数据已经结束
                        outStream.write(endline.getBytes());

//        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        outStream.flush();
                        outStream.close();
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("有异常","异常");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * url下载图片
     * @param path
     * @return
     */
    public static List<ImageItem> download(final List<String> path){
        final List<ImageItem> imageItemList = new ArrayList<ImageItem>();
        final Bitmap[] bitmap = new Bitmap[9];
        Log.e("准备下载","准备"+path);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0;i<path.size();i++){
                    bitmap[i] = HttpUtil.downloadTest(path.get(i),i);
                    ImageItem imageItem=new ImageItem();
                    imageItem.setBitmap(bitmap[i]);
                    imageItemList.add(imageItem);
                    count=count+1;
                }
            }
        }).start();
        return imageItemList;
    }

    public static Bitmap downloadTest(String path,int i){
        Log.e("准备下载","准备下载"+path);
        Bitmap bitmap=null;
        try {
            Log.e("下载完成","下载");
            URL url=new URL(path);
            Log.e("下载","下载");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            Log.e("下载完成","载");
            InputStream in=connection.getInputStream();
            Log.e("完成","下载");
            bitmap =BitmapFactory.decodeStream(in);
            FileUtils.saveBitmap(bitmap,"testdownload"+i);
            Log.e("下载完成","下载完成");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
