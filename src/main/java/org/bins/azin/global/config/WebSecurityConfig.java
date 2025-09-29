package org.bins.azin.global.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String[] ALLOWED_URL_MATCHERS = {
            "/",
            "/sign-up",
            "/api/v1/auth/login",
            "/api/v1/auth/logout",
            "/api/v1/user/exists/**",
            "/api/v1/user"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer customizer() {
        return web -> web
                .ignoring()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .requestMatchers(PathRequest.toH2Console());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /* 전역 보안 비활성화 */
        http.httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable);
        /* 요청 인가 설정 */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(ALLOWED_URL_MATCHERS).permitAll()
                .anyRequest().authenticated());
        /* 세션 관리 정책 */
        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionFixation()
                    .changeSessionId()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/"));
        /* H2 콘솔 접근을 위해 'X-FRAME-OPTIONS 헤더 : 동일 도메인 내에선 접근 가능' 설정 */
        http.headers(header -> header
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }
}