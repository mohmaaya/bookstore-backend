package de.anevis.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(cors -> cors.configurationSource(corsConfiguration()))
				.csrf(AbstractHttpConfigurer::disable)
				.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
				.build();
	}

	public static CorsConfigurationSource corsConfiguration() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOriginPattern("*");
		corsConfiguration.applyPermitDefaultValues();
		corsConfiguration.addAllowedMethod(HttpMethod.PUT);
		corsConfiguration.addAllowedMethod(HttpMethod.PATCH);
		corsConfiguration.addAllowedMethod(HttpMethod.DELETE);
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return urlBasedCorsConfigurationSource;
	}

}
