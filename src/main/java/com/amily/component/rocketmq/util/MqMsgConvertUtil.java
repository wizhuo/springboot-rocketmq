package com.amily.component.rocketmq.util;

import java.io.*;


/**
 * @author lizhuo
 * @since 2019/1/5 下午9:22
 **/
public class MqMsgConvertUtil {

    public static final byte[] emptyBytes = new byte[0];
    public static final String emptyString = "";
    public static final String JMS_MSGMODEL = "jmsMsgModel";
    public static final String COMPATIBLE_FIELD_MSGMODEL = "notifyOutMsgModel";
    public static final String MSGMODEL_TEXT = "textMessage";
    public static final String MSGMODEL_BYTES = "bytesMessage";
    public static final String MSGMODEL_OBJ = "objectMessage";
    public static final String MSG_TOPIC = "msgTopic";
    public static final String MSG_TYPE = "msgType";

    public MqMsgConvertUtil() {
    }

    public static byte[] objectSerialize(Object object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        baos.close();
        return baos.toByteArray();
    }

    public static Serializable objectDeserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ois.close();
        bais.close();
        return (Serializable)ois.readObject();
    }

    public static final byte[] string2Bytes(String s, String charset) {
        if (null == s) {
            return emptyBytes;
        } else {
            byte[] bs = null;

            try {
                bs = s.getBytes(charset);
            } catch (Exception var4) {
                ;
            }

            return bs;
        }
    }

    public static final String bytes2String(byte[] bs, String charset) {
        if (null == bs) {
            return "";
        } else {
            String s = null;

            try {
                s = new String(bs, charset);
            } catch (Exception var4) {
                ;
            }

            return s;
        }
    }
}
