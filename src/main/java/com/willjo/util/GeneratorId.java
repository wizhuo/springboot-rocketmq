package com.willjo.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;

/**
 * <p>
 * 全局唯一ID生成器，31位：17位毫秒时间戳（格式化后）+9位机器码和进程号+5位循环序列号
 * </p>
 * <p>
 * 全局唯一ID生成器，27位：13位毫秒数时间戳（无格式化）+9位机器码和进程号+5位循环序列号
 * </p>
 *
 * @author lizhuo
 * @since 2019/2/19 11:35
 */

public class GeneratorId {


    public static void main(String[] args) {

        long a = System.currentTimeMillis();
        for (int i = 0; i < 2000000; i++) {
            nextFormatId();
        }
        System.out.println(System.currentTimeMillis() - a);

        System.out.println(nextFormatId());
        System.out.println(nextMillisId());

    }

    /**
     * 流水号起始值
     */
    private static final long MIN_SEQUENCE = 10000L;
    /**
     * 流水号最大值
     */
    private static final long MAX_SEQUENCE = 99999L;
    /**
     * 流水号，在最小最大值之间循环，避免唯一ID的安全性泄露，时间戳精确到毫秒，单机在1毫秒内也不可能生成近十万个业务ID
     */
    private static long sequence = MIN_SEQUENCE;
    /**
     * 机器码 加 进程号 会导致生成的序列号很长, 基于这两个值做一些截取
     */
    private static final String MP;

    static {
        try {
            // 机器码 --> 本机 mac 地址的 hashcode 值
            int machineIdentifier = createMachineIdentifier();
            // 进程号 --> 当前运行的 jvm 进程号的 hashcode 值
            int processIdentifier = createProcessIdentifier();

            int hashcode = (machineIdentifier + "" + processIdentifier).hashCode();
            String mp = "";
            if (hashcode != Integer.MIN_VALUE) {
                mp = Integer.toString(Math.abs(hashcode));
            } else {
                mp = Integer.toString(Integer.MIN_VALUE);
            }
            MP = mp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GeneratorId() {
    }

    ;

    /**
     * 全局唯一ID生成器，31位：17位毫秒时间戳（格式化后）+9位机器码和进程号+5位循环序列号
     */
    public static synchronized String nextFormatId() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS") + MP
            + nextSequence();
    }

    /**
     * 全局唯一ID生成器，27位：13位毫秒数时间戳（无格式化）+9位机器码和进程号+5位循环序列号
     */
    private static synchronized String nextMillisId() {
        return System.currentTimeMillis() + MP + nextSequence();
    }

    /**
     * 生成下一个序列号
     */
    private static synchronized long nextSequence() {
        if (sequence >= MAX_SEQUENCE) {
            sequence = MIN_SEQUENCE;
        }
        ++sequence;
        return sequence;
    }

    /**
     * 获取机器标识符
     */
    private static int createMachineIdentifier() {
        // build a 2-byte machine piece based on NICs info
        int machinePiece;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
                .getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                stringBuilder.append(networkInterface.toString());
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(mac);
                    try {
                        stringBuilder.append(byteBuffer.getChar());
                        stringBuilder.append(byteBuffer.getChar());
                        stringBuilder.append(byteBuffer.getChar());
                    } catch (BufferUnderflowException shortHardwareAddressException) { // NOPMD
                        // mac with less than 6 bytes. continue
                    }
                }
            }
            machinePiece = stringBuilder.toString().hashCode();
        } catch (Throwable t) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = new SecureRandom().nextInt();
        }
        return machinePiece;
    }


    /**
     * 获取JVM进程号，这并不是每个类装入器,因为必须是唯一的
     */
    private static int createProcessIdentifier() {
        int processId;
        try {
            String processName = ManagementFactory.getRuntimeMXBean().getName();
            if (processName.contains("@")) {
                processId = Integer.parseInt(processName.substring(0, processName.indexOf('@')));
            } else {
                processId = processName.hashCode();
            }
        } catch (Throwable t) {
            processId = new SecureRandom().nextInt();
        }
        return processId;
    }

}
