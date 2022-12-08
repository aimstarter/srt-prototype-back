package com.example.hodolog.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ReserveVo {
    private String reserveType;
    private String jobId;
    private String jrnyCnt;
    private String jrnyTpCd;
    private String jrnySqno1;
    private String stndFlg;
    private String trnGpCd1;
    private String stlbTrnClsfCd1;
    private String dptDt1;
    private String dptTm1;
    private String runDt1;
    private String trnNo1;
    private String dptRsStnCd1;
    private String dptRsStnCdNm1;
    private String arvRsStnCd1;
    private String arvRsStnCdNm1;
    private String psgTpCd1;
    private String psgInfoPerPrnb1;
    private String locSeatAttCd1;
    private String rqSeatAttCd1;
    private String dirSeatAttCd1;
    private String smkSeatAttCd1;
    private String etcSeatAttCd1;
    private String psrmClCd1;
    private String totPrnb;
    private String psgGridcnt;
}
