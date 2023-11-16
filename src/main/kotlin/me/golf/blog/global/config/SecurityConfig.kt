package me.golf.blog.global.config

import jakarta.servlet.DispatcherType
import me.golf.blog.global.jwt.JwtSecurityConfig
import me.golf.blog.global.jwt.TokenProvider
import me.golf.blog.global.jwt.error.JwtAccessDeniedHandler
import me.golf.blog.global.jwt.error.JwtAuthenticationEntryPoint
import me.golf.blog.global.security.CustomAuthenticationProvider
import me.golf.blog.global.security.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val corsFilter: CorsFilter,
    private val tokenProvider: TokenProvider,
    private val authenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val accessDeniedHandler: JwtAccessDeniedHandler
) {

    @Bean
    fun customAuthenticationProvider(): CustomAuthenticationProvider? {
        return CustomAuthenticationProvider()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        val excludePaths = arrayOf(
            "/docs/**",
            "/images/**",
            "/favicon.ico",
            "/",
            "/h2-console"
        )

        http.csrf { it.disable() }
            .headers { header -> header.frameOptions { it.disable() } }
            .formLogin { it.disable() }

            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter::class.java)

            .exceptionHandling {
                it.authenticationEntryPoint(authenticationEntryPoint)
                it.accessDeniedHandler(accessDeniedHandler)
            }

            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }

            .authorizeHttpRequests {
                it.dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                it.requestMatchers(*excludePaths)
                it.requestMatchers(HttpMethod.POST, "/members").permitAll()
                it.requestMatchers(HttpMethod.POST, "/auths/refresh").permitAll()
                it.requestMatchers(HttpMethod.POST, "/auths").permitAll()
                it.anyRequest().authenticated()
            }

            .userDetailsService(customUserDetailsService)
            .apply(JwtSecurityConfig(tokenProvider))

        return http.build()
    }
}