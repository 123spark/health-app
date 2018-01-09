
package com.lhj.classic.bluetooth.ble.utils;

import java.util.HashMap;


public class GattUtils {

    private static HashMap<String, String> attributes = new HashMap();
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String CLIENT_CHARACTERISTIC_CONFIG2 = "00002a02-0000-1000-8000-00805f9b34fb";

    static {
        // Sample Services.
        attributes.put("00001800-0000-1000-8000-00805f9b34fb", "自定义的ServiceName");
        // Sample Characteristics.
        attributes.put("0783b03e-8535-b5a0-7140-a304d2495cb1", "自定义CharacteristicsName");
        attributes.put("00002a02-0000-1000-8000-00805f9b34fb", "可写入CharacteristicsName");
    }

    /**
     * byte[]转string
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return "";
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 与自定义的uuid赋值name
     *
     * @param uuid
     * @param defaultName
     * @return
     */
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null || name.equals("") ? defaultName : name;
    }
}
