package org.bins.azin.global.common.logging.support;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.nio.charset.StandardCharsets;

/**
 * HTTP 응답 본문을 여러 번 읽을 수 있도록 캐싱 처리를 하기 위해 구현한 래퍼 클래스
 */

public class CachingResponseWrapper extends ContentCachingResponseWrapper {

    public CachingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /**
     * 캐싱된 응답 본문을 문자열로 반환한다.
     *
     * @return  캐싱된 응답 본문이 존재할 경우 캐싱된 본문이 변환된 문자열을, 캐싱된 응답 본문이 비어있다면 빈 문자열을 반환
     */
    public String getCachedBodyAsString() {
        byte[] cachedBody = super.getContentAsByteArray();
        return cachedBody.length > 0 ? new String(cachedBody, StandardCharsets.UTF_8) : "";
    }
}