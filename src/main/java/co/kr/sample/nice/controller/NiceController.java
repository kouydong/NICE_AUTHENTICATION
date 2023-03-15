package co.kr.sample.nice.controller;

import CheckPlus.nice.MCheckPlus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class NiceController {

    //─────────────────────────────────────────────────────────────────────────────────
    //  1단계 : 사용자 본인 인증 단계
    //─────────────────────────────────────────────────────────────────────────────────
    //  정상인 경우 아래의 응답 값 전송
    //  returnCode : 인증결과코드
    //  requestSEQ : 요청 고유번호
    //  responseSEQ : 응답 고유번호
    //─────────────────────────────────────────────────────────────────────────────────
    @PostMapping("/nice/authentication/code")
    public Flux<String> code() {

        String sSiteCode	= "";	// 사이트 코드 (NICE평가정보에서 발급한 사이트코드)
        String sSitePw		= "";   // 사이트 패스워드 (NICE평가정보에서 발급한 사이트패스워드)
        String sJumin		= "8008071";	// 주민등록번호 앞 7자리
        String sName		= "고의동";	// 성명
        String sMobileCo	= "3";	// 이통사 구분 (SKT : 1 / KT : 2 / LG : 3 / SKT알뜰폰 : 5 / KT알뜰폰 : 6 / LGU+알뜰폰 : 7)
        String sMobileNo	= "01091861725";	// 휴대폰 번호
        String sCPRequest   = "";   // 위변조 방지를 위한 식별 코드

        // 객체 생성
        MCheckPlus cpMobile = new MCheckPlus();

        int iReturn = -1;

        // Method 결과값(iReturn)에 따라, 프로세스 진행여부를 파악합니다.
        iReturn = cpMobile.fnRequestSafeAuth(sSiteCode, sSitePw, sJumin, sName, sMobileCo, sMobileNo, sCPRequest);

        // Method 결과값에 따른 처리사항
        if (iReturn == 0)
        {
            System.out.println("RETURN_CODE=" + cpMobile.getReturnCode());              // 인증결과코드
            System.out.println("REQ_SEQ=" + cpMobile.getRequestSEQ());                  // 요청 고유번호
            System.out.println("RES_SEQ=" + cpMobile.getResponseSEQ());                 // 응답 고유번호
        }
        else if (iReturn == -7 || iReturn == -8)
        {
            System.out.println("AUTH_ERROR=" + iReturn);
            System.out.println("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port를 오픈해 주셔야 이용 가능합니다.");
            System.out.println("IP : 121.131.196.200 / Port : 3700 ~ 3715");
        }
        else if (iReturn == -9 || iReturn == -10 || iReturn == 12)
        {
            System.out.println("AUTH_ERROR=" + iReturn);
            System.out.println("입력값 오류 : fnRequest 함수 처리시, 필요한 6개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
        }
        else
        {
            System.out.println("AUTH_ERROR=" + iReturn);
            System.out.println("iReturn 값 확인 후, NICE평가정보 전산 담당자에게 문의해 주세요.");
        }
        return Flux.just("OK");
    }

    //─────────────────────────────────────────────────────────────────────────────────
    //  2단계 : 인증 코드 확인 단계
    //─────────────────────────────────────────────────────────────────────────────────
    //  정상인 경우 아래의 응답 값 전송
    //  returnCode : 인증결과코드
    //  requestSEQ : 요청 고유번호
    //  responseSEQ : 응답 고유번호
    //─────────────────────────────────────────────────────────────────────────────────
    @PostMapping("/nice/authentication/confirm")
    public Flux<String> confirm() {
        String sSiteCode	= "";          // 사이트 코드 (NICE평가정보에서 발급한 사이트코드)
        String sSitePw		= ""; 	// 사이트 패스워드 (NICE평가정보에서 발급한 사이트패스워드)
        String sResSeq		= "";			    // 응답 고유번호 (CPMobileStep1 에서 확인된 cpMobile.getResponseSEQ() 데이타)
        String sAuthNo		= "";			    // SMS 인증번호
        String sCPRequest	= "";			    // 요청 고유번호 (CPMobileStep1 에서 정의한 cpMobile.getRequestSEQ() 데이타)

        // 객체 생성
        MCheckPlus cpMobile = new MCheckPlus();

        int iReturn = -1;
        // Method 결과값(iReturn)에 따라, 프로세스 진행여부를 파악합니다.
        iReturn = cpMobile.fnRequestConfirm(sSiteCode, sSitePw, sResSeq, sAuthNo, sCPRequest);

        if (iReturn == 0)
        {
            System.out.println("RETURN_CODE=" + cpMobile.getReturnCode());              // 응답코드
            System.out.println("CONFIRM_DATETIME=" + cpMobile.getConfirmDateTime());    // 인증 완료시간
            System.out.println("REQ_SEQ=" + cpMobile.getRequestSEQ());                  // 요청 고유번호
            System.out.println("RES_SEQ=" + cpMobile.getResponseSEQ());                 // 응답 고유번호
            System.out.println("RES_CI=" + cpMobile.getResponseCI());                   // 아이핀 연결정보(CI)
            System.out.println("RES_DI=" + cpMobile.getResponseDI());                   // 아이핀 중복가입확인정보(DI)
        }
        else if (iReturn == -7 || iReturn == -8)
        {
            System.out.println("AUTH_ERROR=" + iReturn);
            System.out.println("서버 네트웍크 및 방확벽 관련하여 아래 IP와 Port를 오픈해 주셔야 이용 가능합니다.");
            System.out.println("IP : 121.131.196.200 / Port : 3700 ~ 3715");
        }
        else if (iReturn == -9 || iReturn == -10 || iReturn == 12)
        {
            System.out.println("AUTH_ERROR=" + iReturn);
            System.out.println("입력값 오류 : fnRequest 함수 처리시, 필요한 5개의 파라미터값의 정보를 정확하게 입력해 주시기 바랍니다.");
        }
        else
        {
            System.out.println("AUTH_ERROR=" + iReturn);
            System.out.println("iReturn 값 확인 후, NICE평가정보 전산 담당자에게 문의해 주세요.");
        }

        return Flux.just("OK");
    }
}
