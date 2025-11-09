package com.server.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * LocalDateTime 自定义反序列化器
 * 支持 "yyyy-MM-dd" 和 "yyyy-MM-dd HH:mm:ss" 两种格式
 *
 * @author bxwy
 */
public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTimeDeserializer() {
        super(LocalDateTime.class);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        // 先尝试解析完整日期时间格式 "yyyy-MM-dd HH:mm:ss"
        try {
            return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            // 如果失败，尝试解析日期格式 "yyyy-MM-dd"，并设置为当天的 00:00:00
            try {
                return LocalDateTime.parse(dateString + " 00:00:00", DATE_TIME_FORMATTER);
            } catch (DateTimeParseException e2) {
                throw new IOException("无法解析日期时间: " + dateString + "，支持的格式: yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss", e2);
            }
        }
    }
}

