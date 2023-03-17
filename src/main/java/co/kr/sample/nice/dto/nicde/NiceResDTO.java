package co.kr.sample.nice.dto.nicde;

import lombok.Builder;

@Builder
public class NiceResDTO {

    private String returnCode;     // 리턴코드
    private String  returnMessage;  // 리턴메시지
    private String  requestSEQ;     // 나이스 요청 번호
    private String  responseSEQ;    // 나이스 응답 번호
    private String  cpRequest;      // 위변조방지문자열
    private String  responseCI;     // CI값
    private String  responseDI;     // DI 값
    private String  confirmedDate;  // 인증완료시간

}
