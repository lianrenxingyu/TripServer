package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by chenggong on 18-5-6.
 *
 * @author chenggong
 */

public class StringUtil {

    /**
     * 返回的是一个32位的md5加密字符串
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        return md5(str, 32);
    }

    /**
     * 返回一定长度的字符串,如果length不符合规则,返回32的md5字符串
     *
     * @param length 返回的字符串长度
     * @return
     */
    public static String md5(String str, int length) {
        if (length > 32 || length < 0) {
            length = 32;
        }
        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] bytes = digest.digest(str.getBytes());//bytes数组的大小是16位的,摘要返回一个固定长度为16的数组
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : bytes) {
                // 与运算加盐
                String string = Integer.toHexString(b & 0xff);//这句话返回的最多是一个两位字符,可能一位
                if (string.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(string);
            }
            // 标准的md5加密后的结果
            return buffer.substring(0, length);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
