package com.server.service;

import java.util.Map;

public interface AgricultureDataProcessingService {
    /**
     * 处理Map类型的数据，这是由SensorCommunicationService调用的主要方法。
     * @param parsedData 从传感器通信服务传递过来的，已解析的数据Map
     */
    void processAndStore(Map<String, Object> parsedData);
}