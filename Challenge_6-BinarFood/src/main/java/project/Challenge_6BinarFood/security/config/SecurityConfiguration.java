package project.Challenge_6BinarFood.security.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Get;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import project.Challenge_6BinarFood.security.jwt.AuthEntryPointJwt;
import project.Challenge_6BinarFood.security.jwt.JWTAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthenticationProvider authenticationProvider;
    private final ApplicationConfig applicationConfig;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( auth ->
                                auth
                                        .requestMatchers("/api/v1/auth/**").permitAll()
                                        .requestMatchers(DELETE,"/api/v1/users").hasAnyRole("MERCHANT", "CUSTOMER")
                                        .requestMatchers(PATCH,"/api/v1/users").hasAnyRole("MERCHANT", "CUSTOMER")
                                        .requestMatchers("/api/v1/demo-controller/tes").hasRole("MERCHANT")
                                        .requestMatchers(POST,"/api/v1/merchant").hasRole("MERCHANT")
                                        .requestMatchers(PATCH,"/api/v1/merchant/{merchantId}").hasRole("MERCHANT")
                                        .requestMatchers(DELETE,"/api/v1/merchant/{merchantId}").hasRole("MERCHANT")
                                        .requestMatchers(POST,"/api/v1/merchant/getAllMerchantOpenOrClose").permitAll()
                                        .requestMatchers(GET,"/api/v1/merchant/{merchantId}").permitAll()
                                        .requestMatchers(POST,"/api/v1/merchant/{merchantId}/products").hasRole("MERCHANT")
                                        .requestMatchers(PATCH,"/api/v1/merchant/{merchantId}/products/{productId}").hasRole("MERCHANT")
                                        .requestMatchers(DELETE,"/api/v1/merchant/{merchantId}/products/{productId}").hasRole("MERCHANT")
                                        .requestMatchers(GET,"/api/v1/merchant/{merchantId}/products/{productId}").permitAll()
                                        .requestMatchers(GET,"/api/v1/merchant/products").permitAll()
                                        .requestMatchers(GET,"/api/v1/merchant/products/pagination/{page}/{pageSize}/{filter}").permitAll()
                                        .requestMatchers(GET,"/api/v1/merchant/products/pagination/{page}/{pageSize}").permitAll()
                                        .requestMatchers(POST,"/api/v1/order").hasRole("CUSTOMER")
                                        .requestMatchers(GET,"/api/v1/order/getAllUserOrder").hasRole("CUSTOMER")
                                        .requestMatchers(GET,"/api/v1/order/getAllOrder").hasAnyRole("CUSTOMER", "MERCHANT")
                                        .requestMatchers(GET,"/api/v1/order/getAllOrderWithPagination/{page}/{pageSize}").hasAnyRole("CUSTOMER", "MERCHANT")
                                        .requestMatchers(GET,"/api/v1/order/getAllOrderWithPagination/{page}/{pageSize}/{filter}").hasAnyRole("CUSTOMER", "MERCHANT")
                                        .requestMatchers(POST,"/api/v1/invoice/reportMerchantAllPayOrNot/{merchantId}/{format}").hasRole("MERCHANT")
                                        .requestMatchers(POST,"/api/v1/invoice/reportMerchantWeek/{merchantId}/{date}/{format}").hasRole("MERCHANT")
                                        .requestMatchers(POST,"/api/v1/invoice/reportMerchantMonth/{merchantId}/{month}/{format}").hasRole("MERCHANT")
                                        .requestMatchers(POST,"/api/v1/invoice/reportMerchantCustom/{merchantId}/{startDate}/{endDate}/{format}").hasRole("MERCHANT")
                                        .requestMatchers(POST,"/api/v1/invoice/reportUserInvoice/{format}").hasRole("CUSTOMER")
                                        .requestMatchers(GET,"/api/v1/invoice/userOrderByUser").permitAll()
                                        .anyRequest().authenticated()
                );
        http.authenticationProvider(applicationConfig.authenticationProvider());
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }
}
