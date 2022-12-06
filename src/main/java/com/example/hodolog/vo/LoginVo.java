package com.example.hodolog.vo;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginVo {
    private String srchDvCd;
    private String srchDvNm;
    private String hmpgPwdCphd;
    private String auto;
    private String check;
    private String page;
    private String deviceKey;
    private String customerYn;
    private String login_referer;
}
