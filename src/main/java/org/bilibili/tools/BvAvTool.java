package org.bilibili.tools;

import java.util.HashMap;

/**
 * BV/AV号转换工具.
 *
 * @author DarthCY
 */
public class BvAvTool {
    private static String table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
    private static HashMap<String, Integer> mp = new HashMap<>();
    private static HashMap<Integer, String> mp2 = new HashMap<>();
    static int ss[] = {11, 10, 3, 8, 4, 6, 2, 9, 5, 7};
    static long xor = 177451812;
    static long add = 8728348608l;

    /**
     * 根据传来的视频需求操作和id来进行转换.
     *
     * @param mode 需求转换，b2v或v2b
     * @param vid  视频vid
     * @return 转换好的视频vid
     */
    public static String convert(String mode, String vid) {
        if("b2v".equals(mode)){
            System.out.println("BvAvTool:::bv->av号转换成功");
            return b2v(vid);
        }
        else if("v2b".equals(mode)){
            System.out.println("BvAvTool:::av->bv号转换成功");
            return v2b(vid);
        }
        return null;
    }

    private static long power(int a, int b) {
        long power = 1;
        for (int c = 0; c < b; c++) {
            power *= a;
        }
        return power;
    }

    private static String b2v(String s) {
        long r = 0;
        for (int i = 0; i < 58; i++) {
            String s1 = table.substring(i, i + 1);
            mp.put(s1, i);
        }
        for (int i = 0; i < 6; i++) {
            r = r + mp.get(s.substring(ss[i], ss[i] + 1)) * power(58, i);
        }
        return "av" + ((r - add) ^ xor);
    }

    private static String v2b(String st) {
        long s = Long.valueOf(st.split("av")[1]);
        StringBuffer sb = new StringBuffer("BV1  4 1 7  ");
        s = (s ^ xor) + add;
        for (int i = 0; i < 58; i++) {
            String s1 = table.substring(i, i + 1);
            mp2.put(i, s1);
        }
        for (int i = 0; i < 6; i++) {
            String r = mp2.get((int) (s / power(58, i) % 58));
            sb.replace(ss[i], ss[i] + 1, r);
        }
        return sb.toString();
    }
}
