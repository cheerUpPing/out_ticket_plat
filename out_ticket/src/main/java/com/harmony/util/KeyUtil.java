package com.harmony.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 2017/9/20 17:37.
 * <p>
 * Email: cheerUpPing@163.com
 * <p>
 * 键盘码工具
 */
public class KeyUtil {

    private static String key;

    /**
     * 获取 键盘key编码code
     *
     * @param key 不分大小写
     * @return
     */
    public static byte[] getKeyCode(String key) {
        byte[] keys = null;
        if (StringUtils.isNotEmpty(key)) {
            switch (key.toUpperCase()) {
                case "F1":
                    keys = new byte[]{(byte) 0xf4, 0x09, (byte) 0xf4};
                    break;
                case "F2":
                    keys = new byte[]{(byte) 0xf3, 0x09, (byte) 0xf3};
                    break;
                case "F3":
                    keys = new byte[]{(byte) 0xf5, 0x09, (byte) 0xf5};
                    break;
                case "F4":
                    keys = new byte[]{(byte) 0xed, 0x09, (byte) 0xed};
                    break;
                case "F5":
                    keys = new byte[]{(byte) 0xf6, 0x09, (byte) 0xf6};
                    break;
                case "F6":
                    keys = new byte[]{(byte) 0xee, 0x09, (byte) 0xee};
                    break;
                case "F7":
                    keys = new byte[]{0x76, 0x09, 0x76};
                    break;
                case "F8":
                    keys = new byte[]{(byte) 0xef, 0x09, (byte) 0xef};
                    break;
                case "F9":
                    keys = new byte[]{(byte) 0xf8, 0x09, (byte) 0xf8};
                    break;
                case "F10":
                    keys = new byte[]{(byte) 0xf0, 0x09, (byte) 0xf0};
                    break;
                case "F11":
                    keys = new byte[]{(byte) 0x81, 0x09, (byte) 0x81};
                    break;
                case "F12":
                    keys = new byte[]{(byte) 0xf2, 0x09, (byte) 0xf2};
                    break;
                case "0":
                    keys = new byte[]{(byte) 0xb4, 0x09, (byte) 0xb4};
                    break;
                case "1":
                    keys = new byte[]{(byte) 0xe3, 0x09, (byte) 0xe3};
                    break;
                case "2":
                    keys = new byte[]{(byte) 0xdb, 0x09, (byte) 0xdb};
                    break;
                case "3":
                    keys = new byte[]{(byte) 0xd3, 0x09, (byte) 0xd3};
                    break;
                case "4":
                    keys = new byte[]{(byte) 0xd4, 0x09, (byte) 0xd4};
                    break;
                case "5":
                    keys = new byte[]{(byte) 0xcb, 0x09, (byte) 0xcb};
                    break;
                case "6":
                    keys = new byte[]{(byte) 0xc3, 0x09, (byte) 0xc3};
                    break;
                case "7":
                    keys = new byte[]{(byte) 0xbc, 0x09, (byte) 0xbc};
                    break;
                case "8":
                    keys = new byte[]{(byte) 0xbb, 0x09, (byte) 0xbb};
                    break;
                case "9":
                    keys = new byte[]{(byte) 0xb3, 0x09, (byte) 0xb3};
                    break;
                case "ESC":
                    keys = new byte[]{(byte) 0x83, 0x09, (byte) 0x83};
                    break;
                case "ENTER":
                    keys = new byte[]{(byte) 0x9f, 0x09, (byte) 0x9f};
                    break;
                case "+":
                    keys = new byte[]{(byte) 0x80, 0x09, (byte) 0x80};
                    break;
                case "-":
                    keys = new byte[]{(byte) 0x7e, 0x09, (byte) 0x7e};
                    break;
                case "F":
                    keys = new byte[]{(byte) 0xce, 0x09, (byte) 0xce};
                    break;
                case "H":
                    keys = new byte[]{(byte) 0xc6, 0x09, (byte) 0xc6};
                    break;
                case "BACK":
                    keys = new byte[]{(byte) 0x93, 0x09, (byte) 0x93};
                    break;
            }
        }
        return keys;
    }

    /**
     * 通过键盘串获取字节数组
     *
     * @param keys 5 1 5 6 3 0 0 2 1 F1 3 0 0 6 1 F1 F2 0 2
     * @return
     */
    public static byte[] getKeyCodesByKeys(String keys) {
        byte[] sum = null;
        if (StringUtils.isNotEmpty(keys)) {
            String[] keyArray = keys.split(" ");
            for (int i = 0; i < keyArray.length; i++) {
                String key = keyArray[i];
                byte[] keyCodes = getKeyCode(key);
                sum = (i == 0 ? getKeyCode(key) : CommmUtil.mergeBytes(sum, keyCodes, keyCodes.length));
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        String str = "5 1 5 6 3 0 0 2 1 F1 3 0 0 6 1 F1 F2 0 2";
        byte[] aa = getKeyCodesByKeys(str);
        for (byte a : aa)
            System.out.println(a);
    }

}
