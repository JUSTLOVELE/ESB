package com.esb.env;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.esb.action.interceptors.ProcessorInterceptors;

/**
 * @Description:springmvc配置
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 福建互医科技有限公司
 * @author yangzl 2019-3-28
 * @version 1.00.00
 * @history:
 */
@Configuration
@EnableWebMvc
public class SpringWebMvcConfigurer implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		registry.addInterceptor(new ProcessorInterceptors());
		//registry.addInterceptor(new ProcessorInterceptors()).addPathPatterns("/**");
	}
	
//	@Bean
//	public CommonsMultipartResolver commonsMultipartResolver() {
//		
//		CommonsMultipartResolver c = new CommonsMultipartResolver();
//		c.setMaxUploadSize(10240000);
//		c.setMaxInMemorySize(4096);
//		c.setDefaultEncoding("utf-8");
//		return c;
//	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	     //配置静态资源的处理
		configurer.enable();
	}
	
	@Bean
	public ViewResolver viewResolver() {
		//配置视图解析器
		InternalResourceViewResolver i = new InternalResourceViewResolver();
		i.setSuffix(".jsp");
		i.setPrefix("/jsp/");
		i.setExposeContextBeansAsAttributes(true);
		i.setCache(true);
		return i;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addCorsMappings(CorsRegistry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addFormatters(FormatterRegistry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addViewControllers(ViewControllerRegistry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MessageCodesResolver getMessageCodesResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Validator getValidator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*资源处理器*/
/*    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/img/**").addResourceLocations("/WEB-INF/"+"/img/");
       registry.addResourceHandler("/static/**").addResourceLocations("/WEB-INF/"+"/static/");
    }*/

}
