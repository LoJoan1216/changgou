package com.changgou.listener;

import okhttp3.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Joan
 * @date 2019-11-14 15:41
 */

/**
 * 监听rebbitmq队列中发送来的广告数据
 */
@Component
@RabbitListener(queues = "ad_update_queue")
public class AdListener {

    /**
     * 接受广告队列中的数据进行处理
     * 业务是，模式http请求到nginx，让nginx调用lua脚本进行大广告redis中的数据更新
     *
     * @param position
     */
    public void messageHandler(String position) {

        String url = "http://192.168.200.128/ad_update?position=" + position;

        //创建okhttp对象，对象是目前最快的发送请求的技术
        OkHttpClient okHttpClient = new OkHttpClient();
        //创建请求对象
        Request request = new Request.Builder().url(url).build();
        //发送请求
        Call call = okHttpClient.newCall(request);
        //调用发送请求后的回调方法，获取发送成功或者是失败的信息
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                System.out.println("发送失败");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("发送成功:" + response.message());
            }
        });

    }
}
