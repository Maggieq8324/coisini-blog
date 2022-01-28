package com.coisini.utils;

import org.springframework.stereotype.Component;
import java.util.Random;

/**
 * @Description 随机数工具类
 * @author coisini
 * @date Jan 21, 2022
 * @version 1.0
 */
@Component
public class RandomUtil {

    private Random random = new Random();

    /**
     * 返回指定范围内的随机数
     *
     * @param start 开始 --包括
     * @param end   结束 -- 包括
     * @return
     */
    public synchronized int nextInt(int start, int end) {
        return random.nextInt(end - start) + start + 1;  // +1:包括右界值
    }

}
