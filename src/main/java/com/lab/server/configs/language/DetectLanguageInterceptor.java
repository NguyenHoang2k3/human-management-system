package com.lab.server.configs.language;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.lab.server.configs.security.JwtProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class DetectLanguageInterceptor implements HandlerInterceptor{

	private final String LANGUAGE_HEADER = "Accept-Language";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod handlerMethod)) {
			return true;
		}

		DetectLanguage detectLanguage = handlerMethod.getMethodAnnotation(DetectLanguage.class);
		Locale defaultLocale = detectLanguage != null ? Locale.forLanguageTag(detectLanguage.value().name()) : Locale.ENGLISH;

		String languageTag = request.getHeader(LANGUAGE_HEADER);
		Locale locale = (languageTag != null && !languageTag.isEmpty()) ? Locale.forLanguageTag(languageTag) : defaultLocale;
		LanguageContext.setLocale(locale);

        return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		LanguageContext.clear();
	}
}
