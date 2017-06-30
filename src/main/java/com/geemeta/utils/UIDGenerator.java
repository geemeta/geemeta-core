package com.geemeta.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author itechgee@126.com
 * @date 2017/6/4.
 */
public class UIDGenerator {

    private static final long BEGIN_DATE = new Date(116, 6, 6, 6, 6, 6).getTime();
    private static final int localMachineAppend = Integer.parseInt(ip());

    private static AtomicInteger atomicInteger = new AtomicInteger(0);

    public static long generate(int businessType) {
        StringBuilder binaryStr = new StringBuilder("0");

        binaryStr.append(wrapTimeBinaryStr());
        binaryStr.append(wrapBusinessBinaryStr(businessType));

        binaryStr.append(wrapRoomBinaryStr(1));
        binaryStr.append(wrapMachineBinaryStr(localMachineAppend));

        binaryStr.append(wrapSequencePeyMachine());
//        System.out.println(">" + binaryStr.toString());
        return Long.parseLong(binaryStr.toString(), 2);
    }

    /**
     * 39 bit
     */
    private static String wrapTimeBinaryStr() {
        long currentTime = System.currentTimeMillis();
        long timeElipse = currentTime - BEGIN_DATE;
        return StringUtils.leftPad(Long.toBinaryString(timeElipse), 39, '0');
    }

    /**
     * 10 bit
     */
    private static String wrapMachineBinaryStr(int machineIp) {
        return StringUtils.leftPad(Integer.toBinaryString(machineIp), 10, '0');
    }

    /**
     * 5 bit
     */
    private static String wrapBusinessBinaryStr(int businessType) {
        return StringUtils.leftPad(Integer.toBinaryString(businessType), 5, '0');
    }

    /**
     * 4 bit
     */
    private static String wrapRoomBinaryStr(int room) {
        return StringUtils.leftPad(Integer.toBinaryString(room), 4, '0');
    }

    /**
     * 5 bit
     */
    private static String wrapSequencePeyMachine() {
        if (atomicInteger.get() == Integer.MAX_VALUE) {
            atomicInteger = new AtomicInteger();
        }
        return StringUtils.leftPad(Integer.toBinaryString(atomicInteger.incrementAndGet() % 32), 5, '0');
    }

    private static String ip() {
        try {
            return InetAddress.getLocalHost().getHostAddress().split("\\.")[3];
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "1";
    }

    public static void main(String[] args) {
        System.out.println(generate(1));
        System.out.println(generate(1));
        System.out.println(generate(2));
        System.out.println(generate(2));
        System.out.println(generate(2));
        System.out.println(generate(4));
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress().split("\\.")[3]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
