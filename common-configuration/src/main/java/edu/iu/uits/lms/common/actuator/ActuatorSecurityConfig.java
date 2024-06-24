package edu.iu.uits.lms.common.actuator;

/*-
 * #%L
 * lms-canvas-common-configuration
 * %%
 * Copyright (C) 2015 - 2023 Indiana University
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Indiana University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import edu.iu.uits.lms.common.it12logging.RestSecurityLoggingConfig;
import edu.iu.uits.lms.common.oauth.CustomJwtAuthenticationConverter;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ActuatorSecurityConfig {

//    @Configuration
//    @Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
//    public static class ActuatorSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.requestMatchers().antMatchers("/actuator/**")
//                  .and().authorizeRequests()
//                  .antMatchers("/actuator/health/**").permitAll()
//                  .antMatchers("/**").hasRole("LMS_REST_ADMINS")
//                  .and()
//                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                  .and()
//                  .oauth2ResourceServer()
//                  .jwt().jwtAuthenticationConverter(new CustomJwtAuthenticationConverter());
//            http.apply(new RestSecurityLoggingConfig());
//        }
//
//    }

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER - 10)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/actuator/**")
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/actuator/health/**").permitAll()
                        .requestMatchers("/**").hasRole("LMS_REST_ADMINS")
                )
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(customizer -> customizer.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())))
                .with(new RestSecurityLoggingConfig(), log -> {});
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().authenticated()
//                        .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
//                            public <O extends FilterSecurityInterceptor> O postProcess(
//                                    O fsi) {
//                                fsi.setPublishAuthorizationSuccess(true);
//                                return fsi;
//                            }
//                        })
//                );
//        return http.build();
//    }
}
