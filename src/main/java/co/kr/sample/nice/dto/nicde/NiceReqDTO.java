package co.kr.sample.nice.dto.nicde;

import lombok.Getter;
import lombok.ToString;

public class NiceReqDTO {

    @ToString
    @Getter
    public static class Authentication {
        private String jumin;           // 주민등록번호 앞 7자리
        private String name;            // 성명
        private String mobileProvider;  // 이동통신사 구분
        private String mobileNo;        // 휴대폰번호
    }


    @ToString
    @Getter
    public static class Confirm {
        private String requestSeq;
        private String responseSeq;
        private String authNo;
    }



}
