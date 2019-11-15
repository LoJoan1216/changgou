package com.changgou.canal.util;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Joan
 * @date 2019-11-14 15:27
 */
public class CanalUtil {
    public static Map<String, String> convertToMap(List<CanalEntry.Column> columnList) {

        Map<String, String> map = new HashMap<>();
         columnList.forEach(column -> map.put(column.getName(), column.getValue()));
        return map;
    }
}
