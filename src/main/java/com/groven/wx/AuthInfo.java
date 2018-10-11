package com.groven.wx;

/**
 * @Author: groven
 * @Date: 2018/10/11 15:37
 * @Description:
 * @Company: 迅捷微风
 */
public class AuthInfo {
    private String code;
    private String state;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "AuthInfo{" +
                "code='" + code + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
