package com.server.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime 自定义序列化器
 * 始终输出完整日期时间格式 "yyyy-MM-dd HH:mm:ss"
 * 前端可以根据需要自行决定显示格式
 *
 * @author bxwy
 */
public class LocalDateTimeSerializer extends StdSerializer<LocalDateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public LocalDateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        // 始终输出完整日期时间格式
        gen.writeString(value.format(DATE_TIME_FORMATTER));
    }
}

