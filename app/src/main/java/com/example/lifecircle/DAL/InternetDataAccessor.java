package com.example.lifecircle.DAL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

/**
 * construct the url and message while initialize an object
 * get the response
 * Created by LisaiZhang on 2016/4/29.
 */
public class InternetDataAccessor{
    public String sendMsg(String url, List<NameValuePair> params){
        //send message to url
        try {
            if(params.size() != 0){
                    url += "?" + params.get(0).getName() + "=" + URLEncoder.encode(params.get(0).getValue(), "utf-8");
                for(int i = 1;i < params.size();i++){
                    url += "&" + params.get(i).getName()+"="+ URLEncoder.encode(params.get(i).getValue(), "utf-8");
                }
            }
        }catch (Exception e){
            e.getCause();
        }
        /**HttpParams**/
        int timeOutConnection = 3000;
        int timeOutSocket = 5000;
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams,timeOutConnection);
        HttpConnectionParams.setSoTimeout(httpParams,timeOutSocket);
        /**HttpClient**/
        HttpClient httpClient = new DefaultHttpClient();
        String result = "error:internet";
        try{
            /**HttpGet**/
            HttpGet httpGet = new HttpGet(url);
            /**HttpResponse**/
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if(statusCode == HttpStatus.SC_OK){
                result = EntityUtils.toString(httpResponse.getEntity());
            }
            else{
                result = "error:"+ statusCode;
            }
        }catch (Exception e){
            e.getCause();
            e.printStackTrace();
        }
        return result;
    }

}
