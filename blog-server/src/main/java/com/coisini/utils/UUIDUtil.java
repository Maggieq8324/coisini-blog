package com.coisini.utils;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDUtil {

    public String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
