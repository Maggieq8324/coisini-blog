package com.coisini.utils;

/**
 * @Description UUID 工具类
 * @author coisini
 * @date Jan 21, 2022
 * @version 1.0
 */
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class UUIDUtil {

    /**
     * 获取UUID
     * @return
     */
    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
