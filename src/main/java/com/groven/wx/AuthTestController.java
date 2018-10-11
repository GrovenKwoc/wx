package com.groven.wx;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: groven
 * @Date: 2018/10/11 15:34
 * @Description:
 * @Company: 迅捷微风
 */
@RestController
@RequestMapping("/auth")
public class AuthTestController {

    @RequestMapping("/test")
    public AuthInfo test(AuthInfo info) {
        return info;
    }
}
