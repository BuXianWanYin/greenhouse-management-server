package com.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Modbus指令解析工具类
 * 用于解析Modbus-RTU读取指令（功能码03）
 * 
 * @author server
 * @date 2025-01-XX
 */
public class ModbusCommandParser {
    
    private static final Logger log = LoggerFactory.getLogger(ModbusCommandParser.class);
    
    /**
     * 解析Modbus-RTU读取指令（功能码03），返回各字段值
     * 
     * @param cmdHex 空格分隔的十六进制指令字符串，如"01 03 00 00 00 01 84 0A"
     * @return 解析后的字段Map，失败返回null
     *         Map包含的key: functionCode, regStart, regLength, crcLow, crcHigh, deviceAddr
     */
    public static Map<String, Object> parseModbusCommand(String cmdHex) {
        if (cmdHex == null || cmdHex.trim().isEmpty()) {
            log.error("指令为空，无法解析");
            return null;
        }
        
        // 分割为字节列表
        String[] bytesList = cmdHex.trim().split("\\s+");
        
        // 基础校验：指令必须为8字节（功能码03的读取指令固定长度）
        if (bytesList.length != 8) {
            log.error("指令长度不正确，功能码03的读取指令应为8字节，当前为: {}", bytesList.length);
            return null;
        }
        
        try {
            // 解析各字段（十六进制转十进制）
            int functionCode = Integer.parseInt(bytesList[1], 16);  // 功能码（字节1）
            long regStart = Long.parseLong(bytesList[2] + bytesList[3], 16);  // 起始地址（字节2-3，大端模式）
            long regLength = Long.parseLong(bytesList[4] + bytesList[5], 16);  // 寄存器数量（字节4-5，大端模式）
            long crcLow = Long.parseLong(bytesList[6], 16);  // CRC低位（字节6）
            long crcHigh = Long.parseLong(bytesList[7], 16);  // CRC高位（字节7）
            
            // 校验功能码（仅支持03）
            if (functionCode != 3) {
                log.error("仅支持功能码03，当前为: {}", functionCode);
                return null;
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("functionCode", (long) functionCode);
            result.put("regStart", regStart);
            result.put("regLength", regLength);
            result.put("crcLow", crcLow);
            result.put("crcHigh", crcHigh);
            result.put("deviceAddr", bytesList[0]);  // 设备地址（用于生成device_id）
            
            log.debug("解析Modbus指令成功: 功能码={}, 起始地址={}, 寄存器数量={}, CRC={}{}", 
                     functionCode, regStart, regLength, String.format("%02X", crcLow), String.format("%02X", crcHigh));
            
            return result;
            
        } catch (NumberFormatException e) {
            log.error("解析失败：无法将十六进制字符串转换为数字", e);
            return null;
        } catch (Exception e) {
            log.error("解析失败：发生未知错误", e);
            return null;
        }
    }
    
    /**
     * 校验设备回复的指令是否符合Modbus协议
     * Modbus-RTU回复格式（功能码03）：
     * - 设备地址（1字节）
     * - 功能码（1字节，与请求相同）
     * - 数据长度（1字节）
     * - 数据（N字节）
     * - CRC16校验（2字节，低字节在前）
     * 
     * @param responseHex 设备回复的十六进制字符串（空格分隔）
     * @param expectedDeviceAddr 期望的设备地址（十六进制字符串，如"01"）
     * @param expectedFunctionCode 期望的功能码（十进制，如3）
     * @return 校验结果，包含isValid和message
     */
    public static Map<String, Object> validateModbusResponse(String responseHex, String expectedDeviceAddr, int expectedFunctionCode) {
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", false);
        
        if (responseHex == null || responseHex.trim().isEmpty()) {
            result.put("message", "回复指令为空");
            return result;
        }
        
        String[] bytesList = responseHex.trim().split("\\s+");
        
        // 最小长度校验：至少需要5字节（设备地址+功能码+数据长度+CRC）
        if (bytesList.length < 5) {
            result.put("message", String.format("回复指令长度不足，至少需要5字节，当前为: %d", bytesList.length));
            return result;
        }
        
        try {
            // 校验设备地址
            if (!expectedDeviceAddr.equalsIgnoreCase(bytesList[0])) {
                result.put("message", String.format("设备地址不匹配，期望: %s, 实际: %s", expectedDeviceAddr, bytesList[0]));
                return result;
            }
            
            // 校验功能码
            int functionCode = Integer.parseInt(bytesList[1], 16);
            if (functionCode != expectedFunctionCode) {
                result.put("message", String.format("功能码不匹配，期望: %d, 实际: %d", expectedFunctionCode, functionCode));
                return result;
            }
            
            // 校验数据长度
            int dataLength = Integer.parseInt(bytesList[2], 16);
            // 回复总长度 = 设备地址(1) + 功能码(1) + 数据长度(1) + 数据(N) + CRC(2) = 5 + dataLength
            int expectedLength = 5 + dataLength;
            if (bytesList.length != expectedLength) {
                result.put("message", String.format("回复指令长度不匹配，期望: %d, 实际: %d", expectedLength, bytesList.length));
                return result;
            }
            
            // 校验通过
            result.put("isValid", true);
            result.put("message", "校验通过");
            result.put("dataLength", dataLength);
            result.put("functionCode", functionCode);
            result.put("deviceAddr", bytesList[0]);
            
            log.debug("Modbus回复校验通过: 设备地址={}, 功能码={}, 数据长度={}", 
                     bytesList[0], functionCode, dataLength);
            
            return result;
            
        } catch (NumberFormatException e) {
            result.put("message", "解析失败：无法将十六进制字符串转换为数字");
            log.error("校验回复指令时发生解析错误", e);
            return result;
        } catch (Exception e) {
            result.put("message", "校验失败：发生未知错误");
            log.error("校验回复指令时发生未知错误", e);
            return result;
        }
    }
    
    /**
     * 从发送的指令中提取设备地址
     * 
     * @param cmdHex 发送的指令十六进制字符串
     * @return 设备地址（十六进制字符串），失败返回null
     */
    public static String extractDeviceAddr(String cmdHex) {
        if (cmdHex == null || cmdHex.trim().isEmpty()) {
            return null;
        }
        
        String[] bytesList = cmdHex.trim().split("\\s+");
        if (bytesList.length > 0) {
            return bytesList[0];
        }
        
        return null;
    }
}

