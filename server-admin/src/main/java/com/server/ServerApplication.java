package com.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动程序
 *
 */
@SpringBootApplication
@EnableScheduling
public class ServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ServerApplication.class, args);
        System.out.println(" " +
                "██████  ██████  ███████ ███████ ███    ██ ██   ██  ██████  ██    ██ ███████ ███████       ███    ███  █████  ███    ██  █████   ██████  ███████ ███    ███ ███████ ███    ██ ████████ \n" +
                "██       ██   ██ ██      ██      ████   ██ ██   ██ ██    ██ ██    ██ ██      ██            ████  ████ ██   ██ ████   ██ ██   ██ ██       ██      ████  ████ ██      ████   ██    ██    \n" +
                "██   ███ ██████  █████   █████   ██ ██  ██ ███████ ██    ██ ██    ██ ███████ █████   █████ ██ ████ ██ ███████ ██ ██  ██ ███████ ██   ███ █████   ██ ████ ██ █████   ██ ██  ██    ██    \n" +
                "██    ██ ██   ██ ██      ██      ██  ██ ██ ██   ██ ██    ██ ██    ██      ██ ██            ██  ██  ██ ██   ██ ██  ██ ██ ██   ██ ██    ██ ██      ██  ██  ██ ██      ██  ██ ██    ██    \n" +
                " ██████  ██   ██ ███████ ███████ ██   ████ ██   ██  ██████   ██████  ███████ ███████       ██      ██ ██   ██ ██   ████ ██   ██  ██████  ███████ ██      ██ ███████ ██   ████    ██    \n" +
                "                                                                                                                                                                                       \n" +
                "                                                                                                                                                                                       ");


    }
}
