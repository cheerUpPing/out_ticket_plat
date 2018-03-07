package com.harmony.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by CM on 2017/7/7.
 */
public class JingCaiContants {

    public static final Map<String, String> JC_TICKEMODE = new HashMap<String, String>(); //1D2D注红球位置

    static {
        JC_TICKEMODE.put("J0x1", "0");
        JC_TICKEMODE.put("J单关", "1");
        JC_TICKEMODE.put("J2x1", "2");

        JC_TICKEMODE.put("J3x1", "3");
        JC_TICKEMODE.put("J3x3", "4");
        JC_TICKEMODE.put("J3x4", "5");

        JC_TICKEMODE.put("J4x1", "6");
        JC_TICKEMODE.put("J4x4", "7");
        JC_TICKEMODE.put("J4x5", "8");
        JC_TICKEMODE.put("J4x6", "9");
        JC_TICKEMODE.put("J4x11", "10");

        JC_TICKEMODE.put("J5x1", "11");
        JC_TICKEMODE.put("J5x5", "12");
        JC_TICKEMODE.put("J5x6", "13");
        JC_TICKEMODE.put("J5x10", "14");
        JC_TICKEMODE.put("J5x16", "15");
        JC_TICKEMODE.put("J5x20", "16");
        JC_TICKEMODE.put("J5x26", "17");

        JC_TICKEMODE.put("J6x1", "18");
        JC_TICKEMODE.put("J6x6", "19");
        JC_TICKEMODE.put("J6x7", "20");
        JC_TICKEMODE.put("J6x15", "21");
        JC_TICKEMODE.put("J6x20", "22");
        JC_TICKEMODE.put("J6x22", "23");
        JC_TICKEMODE.put("J6x35", "24");
        JC_TICKEMODE.put("J6x42", "25");
        JC_TICKEMODE.put("J6x50", "26");
        JC_TICKEMODE.put("J6x57", "27");

        JC_TICKEMODE.put("J7x1", "28");
        JC_TICKEMODE.put("J7x7", "29");
        JC_TICKEMODE.put("J7x8", "30");
        JC_TICKEMODE.put("J7x21", "31");
        JC_TICKEMODE.put("J7x35", "32");
        JC_TICKEMODE.put("J7x120", "33");
        JC_TICKEMODE.put("J7x127", "34");//现在票机没有

        JC_TICKEMODE.put("J8x1", "35");
        JC_TICKEMODE.put("J8x8", "36");
        JC_TICKEMODE.put("J8x9", "37");
        JC_TICKEMODE.put("J8x28", "38");
        JC_TICKEMODE.put("J8x56", "39");
        JC_TICKEMODE.put("J8x70", "40");
        JC_TICKEMODE.put("J8x247", "41");
        JC_TICKEMODE.put("J8x255", "42");//现在机器没有
    }
}
