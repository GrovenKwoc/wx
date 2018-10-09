package com.groven.wx;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

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
        logger.error("wx has sent a signature：{}", signature);
        logger.error("wx has sent a timestamp：{}", timestamp);
        logger.error("wx has sent a nonce: {}", nonce);
        logger.error("wx wants you to send back：{}", echostr);
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
        return "authenticate failed" + signature + "/" + timestamp + "/" + nonce + "/" + echostr;
    }

    @PostMapping("/wx")
    public String receiveMsg(HttpServletRequest request) throws IOException, DocumentException {
        Document doc = new SAXReader().read(request.getInputStream());
        List<Element> elements = doc.getRootElement().elements();
        StringBuilder sb = new StringBuilder("\n");
        elements.stream().forEach(e -> sb.append(e.getName() + ":" + e.getStringValue() + "\n"));
        logger.error("received msg from user：{}", sb.toString());
        return "success";
    }
}
