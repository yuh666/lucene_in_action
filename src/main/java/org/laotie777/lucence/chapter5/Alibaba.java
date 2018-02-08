package org.laotie777.lucence.chapter5;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * @Author yuh
 * @Date Created in 下午12:55 2018/2/8
 * @Description 将一个数字传入后向右移动6位取得索引 然后将1移动自身为存入数组 判断并设置最大索引值
 *              取数字时 先判断索引是否大于最大索引 然后在判断索引位置是否是自己的值
 */
public class Alibaba {
    public static void main(String[] args) {
        /*Random random = new Random();

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10000000; i++) {
            int randomResult = random.nextInt(100000000);
            list.add(randomResult);
        }
        *//*System.out.println("产生的随机数有");
        for(int i=0;i<list.size();i++)
        {
            System.out.println(list.get(i));
        }*//*
        BitSet bitSet = new BitSet(100000000);
        for (int i = 0; i < 10000000; i++) {
            bitSet.set(list.get(i));
        }

        System.out.println("0~1亿不在上述随机数中有" + bitSet.size());
        for (int i = 0; i < 100000000; i++) {
            if (!bitSet.get(i)) {
                System.out.println(i);
            }
        }*/
        int a = 1;
        int index = a >> 6;
        System.out.println(index);
        long cun = 1L << a;
        System.out.println(cun);
        System.out.println(cun & cun);
    }
}