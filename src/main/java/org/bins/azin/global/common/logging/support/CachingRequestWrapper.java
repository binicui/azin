package org.bins.azin.global.common.logging.support;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * HTTP 요청 본문을 여러 번 읽을 수 있도록 캐싱 처리를 하기 위해 구현한 래퍼 클래스
 */

public class CachingRequestWrapper extends HttpServletRequestWrapper {

    private final Charset charset;

    private final byte[] cachedBody;

    public CachingRequestWrapper(HttpServletRequest request) {
        super(request);
        this.charset = getRequestEncoding(request);
        this.cachedBody = cacheBodyAsByteArray(request);
    }

    /**
     * 캐싱된 요청 본문을 문자열로 반환한다.
     *
     * @return 캐싱된 요청 본문이 존재할 경우 캐싱된 본문이 변환된 문자열을, 캐싱된 요청 본문이 비어있다면 빈 문자열을 반환
     */
    public String getCachedBodyAsString() {
        return (cachedBody != null && cachedBody.length > 0) ? new String(cachedBody, StandardCharsets.UTF_8) : "";
    }

    /**
     * 요청 본문을 바이트 배열로 캐싱한다.
     *
     * @param request   요청 객체
     * @return          캐싱이 정상적으로 처리될 경우 캐싱된 본문을 담고 있는 바이트 배열, 캐싱 작업 도중 문제가 발생할 경우 빈 바이트 배열을 반환
     */
    private byte[] cacheBodyAsByteArray(HttpServletRequest request) {
        try {
            return StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            return new byte[0];
        }
    }

    private Charset getRequestEncoding(HttpServletRequest request) {
        String encoding = request.getCharacterEncoding();
        return StringUtils.hasText(encoding) ? Charset.forName(encoding) : StandardCharsets.UTF_8;
    }

    /**
     * 캐싱된 요청 본문을 바이트 단위로 읽어내기 위한 {@link ServletInputStream}을 반환한다.
     *
     * @return 요청 본문의 복사본을 바이트 단위로 읽을 수 있는 {@link ServletInputStream}
     * @throws IOException  스트림 생성 중 입출력 오류가 발생했을 경우
     */
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ServletInputStream() {

            private final ByteArrayInputStream cachedInputStream = new ByteArrayInputStream(cachedBody);

            @Override
            public boolean isFinished() {
                return cachedInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener listener) {
                throw new UnsupportedOperationException("Not supported ReadListener.");
            }

            @Override
            public int read() throws IOException {
                return cachedInputStream.read();
            }
        };
    }

    /**
     * 캐싱된 요청 본문을 지정된 문자셋을 사용해 문자 단위로 읽어내기 위한 {@link BufferedReader}를 반환한다.
     *
     * @return 요청 본문의 복사본을 문자 단위로 읽을 수 있는 {@link BufferedReader}
     * @throws IOException  {@link BufferedReader} 생성 중 입출력 오류가 발생한 경우
     */
    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), charset));
    }
}