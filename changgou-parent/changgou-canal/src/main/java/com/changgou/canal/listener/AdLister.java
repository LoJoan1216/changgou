package com.changgou.canal.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.canal.util.CanalUtil;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @author Joan
 * @date 2019-11-14 15:06
 */
@CanalEventListener
public class AdLister {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @ListenPoint(schema = "changgou_business", table = {"tb_ad"})
    public void adUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        System.err.println("广告数据发生变化");

        Map<String, String> beforeMap = CanalUtil.convertToMap(rowData.getBeforeColumnsList());
        System.out.println("改变前的数据:"+JSON.toJSONString(beforeMap));
        Map<String, String> afterMap = CanalUtil.convertToMap(rowData.getAfterColumnsList());
        System.out.println("改变后的数据:"+JSON.toJSONString(afterMap));
        String beforePosition = beforeMap.get("position");
        if (beforePosition!=null){
            System.out.println("beforePosition发送消息队列到mq");
            rabbitTemplate.convertAndSend("","ad_update_queue",beforePosition);
        }
        String afterPosition = afterMap.get("position");
        if (afterPosition!=null){
            System.out.println("afterPosition发送消息队列到mq");
            rabbitTemplate.convertAndSend("","ad_update_queue",afterPosition);

        }

    }
}
