package co.kr.sample.nice.controller;

import CheckPlus.nice.MCheckPlus;
import co.kr.sample.nice.constant.NiceMessage;
import co.kr.sample.nice.dto.nicde.NiceReqDTO;
import co.kr.sample.nice.dto.nicde.NiceResDTO;
import co.kr.sample.nice.utils.DateUtils;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static co.kr.sample.nice.constant.NiceMessage.*;


@RestController
@RequestMapping(value = "/nice", produces = MediaType.APPLICATION_JSON_VALUE)
public class NiceController {

    private final Logger log = LoggerFactory.getLogger(NiceController.class);

    @Value("${nice.site-code}")
    private String niceSiteCode;

    @Value("${nice.site-password}")
    private String niceSitePassword;

    Predicate<String> is0000 = s -> s.equals("0000");

    /**
     * <pre>
     * NICE 평가 정보에서 제공하는 비표준(소켓) 방식의 사용자 본인 인증은 2단계에 걸쳐 진행 됩니다.
     * 해당 URI는 1단계 인증 절차로 사용자 정보를 기준으로 본인 인증 절차를 진행합니다.
     * </pre>
     */
    @PostMapping("/authentication")
    public Flux<String> authentication(@RequestBody NiceReqDTO.Authentication request) {
        log.info("request DTO {}", request);
        String jumin            = request.getJumin();
        String name             = request.getName();
        String mobileProvider   = request.getMobileProvider();
        String mobileNo         = request.getMobileNo();
        String returnCode       = "";
        String returnMessage    = "";

        // 위변조 방지를 위한 식별 코드
        String cpRequest = DateUtils.format(new Date(), "yyyyMMddHHmmss") + new Random()
                .ints(100000, 1000000)
                .limit(1)
                .mapToObj(value -> String.valueOf(value))
                .collect(Collectors.joining());
        MCheckPlus cpMobile = new MCheckPlus();

        Integer iReturn = cpMobile.fnRequestSafeAuth(niceSiteCode, niceSitePassword, jumin, name, mobileProvider, mobileNo, cpRequest);

        if (iReturn == 0)
        {
            returnCode      = cpMobile.getReturnCode();
            returnMessage   = NiceMessage.valueOf("N".concat(returnCode)).getReturnMessage();
        }
        else if (iReturn == -7 || iReturn == -8)
        {
            returnCode      = String.valueOf(iReturn);
            returnMessage   = N0090.getReturnMessage();
            log.error("AUTH_ERROR = {}", returnCode);
        }
        else if (iReturn == -9 || iReturn == -10 || iReturn == 12)
        {
            returnCode    = String.valueOf(iReturn);
            returnMessage = N0091.getReturnMessage();
            log.error("AUTH_ERROR = {}", returnCode);
        }
        else
        {
            returnCode    = String.valueOf(iReturn);
            returnMessage = N0099.getReturnMessage();
        }

        NiceResDTO response = NiceResDTO.builder()
                .returnCode(returnCode)
                .returnMessage(returnMessage)
                .requestSEQ(cpMobile.getRequestSEQ())
                .responseSEQ(cpMobile.getResponseSEQ())
                .build();

        return Flux.just(new GsonBuilder().setPrettyPrinting().create().toJson(response));
    }

    /**
     * <pre>
     * NICE 평가 정보에서 제공하는 비표준(소켓) 방식의 사용자 본인 인증은 2단계에 걸쳐 진행 됩니다.
     * 해당 URI는 2단계 인증 절차로 본인 인증 성공 후 인증코드를 확인 하는 절차입니다.
     * </pre>
     */
    @PostMapping("/confirm")
    public Flux<String> confirm(@RequestBody NiceReqDTO.Confirm request) {
        log.info("request DTO {}", request);
        String requestSeq       = request.getRequestSeq();
        String responseSeq      = request.getResponseSeq();
        String authNo           = request.getAuthNo();
        String returnCode       = "";
        String returnMessage    = "";

        MCheckPlus cpMobile = new MCheckPlus();

        Integer iReturn = cpMobile.fnRequestConfirm(niceSiteCode, niceSitePassword, responseSeq, authNo, requestSeq);

        if (iReturn == 0)
        {
            returnCode      = cpMobile.getReturnCode();
            returnMessage   = NiceMessage.valueOf("N".concat(returnCode)).getReturnMessage();
        }
        else if (iReturn == -7 || iReturn == -8)
        {
            returnCode      = String.valueOf(iReturn);
            returnMessage   = N0090.getReturnMessage();
            log.error("AUTH_ERROR = {}", returnCode);
        }
        else if (iReturn == -9 || iReturn == -10 || iReturn == 12)
        {
            returnCode    = String.valueOf(iReturn);
            returnMessage = N0091.getReturnMessage();
            log.error("AUTH_ERROR = {}", returnCode);
        }
        else
        {
            returnCode    = String.valueOf(iReturn);
            returnMessage = N0099.getReturnMessage();
        }

        NiceResDTO response = NiceResDTO.builder()
                .returnCode(returnCode)
                .returnMessage(returnMessage)
                .requestSEQ(cpMobile.getRequestSEQ())
                .responseSEQ(cpMobile.getResponseSEQ())
                .confirmedDate(cpMobile.getConfirmDateTime())
                .responseCI(cpMobile.getResponseCI())
                .responseDI(cpMobile.getResponseDI())
                .build();

        return Flux.just(new GsonBuilder().setPrettyPrinting().create().toJson(response));
    }
}

