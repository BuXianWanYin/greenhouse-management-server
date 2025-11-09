package com.server.service;

import com.server.domain.vo.AiMessageVO;

/**
 * DeepSeek API服务接口
 */
public interface DeepSeekService {
    /**
     * 调用DeepSeek API进行对话
     *
     * @param aiMessageVO AI消息对象
     * @return AI响应内容
     */
    String chat(AiMessageVO aiMessageVO);
    
    /**
     * 调用DeepSeek API进行对话（带提示词）
     *
     * @param prompt 提示词
     * @return AI响应内容
     */
    String chat(String prompt);
}

