package com.groven.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @Author: groven
 * @Date: 2018/10/9 10:57
 * @Description:
 * @Company: 迅捷微风
 */
@RestController
public class WxController {
    private static final String TOKEN = "groven";
    private static final Logger logger = LoggerFactory.getLogger(WxController.class);
    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    @GetMapping("/wx")
    public String verifyWX(String signature, String timestamp, String nonce, String echostr) {
        logger.error("微信传来的signature是：{}", signature);
        logger.error("微信传来的timestamp是：{}", timestamp);
        logger.error("微信传来的nonce随机字符串是: {}", nonce);
        logger.error("微信说要返回给它的是：{}", echostr);
        String[] str = {TOKEN, timestamp, nonce};
        Arrays.sort(str); // 字典序排序
        String bigStr = str[0] + str[1] + str[2];
        // SHA1加密
        MessageDigest md;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(bigStr.getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 确认请求来至微信
        if (!StringUtils.isEmpty(tmpStr) && tmpStr.equals(signature.toLowerCase())) {
            return echostr;
        }
        return "验证失败,你传来了" + signature + "/" + timestamp + "/" + nonce + "/" + echostr;
    }

    @PostMapping("/wx")
    public void receiveMsg(String xmlPack) {
        logger.info(xmlPack);
    }
}
