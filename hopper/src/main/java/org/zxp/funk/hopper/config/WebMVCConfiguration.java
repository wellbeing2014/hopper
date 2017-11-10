package org.zxp.funk.hopper.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


	@Configuration
	//<mvc:annotation-driven/>
	@EnableWebMvc
	@ComponentScan(basePackages={"org.zxp.funk.hopper.controller","org.zxp.funk.hopper.service","org.zxp.funk.hopper.utils","org.zxp.funk.hopper.jpa.dao"})
	public class WebMVCConfiguration extends WebMvcConfigurerAdapter {
	
	// equals: <mvc:default-servlet-handler/>
	@Override
	public void configureDefaultServletHandling(
	        DefaultServletHandlerConfigurer configurer) {
	    configurer.enable();
	    
	}
	
	// add the resolver
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/views/");
	    resolver.setSuffix(".jsp");
	    resolver.setRedirectHttp10Compatible(false);
	    return resolver;
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(converter());
    }

    @Bean
    MappingJackson2HttpMessageConverter converter() {
    	MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    	converter.setPrefixJson(false);
    	//MediaType mt =new MediaType(type, subtype, charset)
    	
    	List<MediaType> myMediaTypes = new ArrayList<MediaType> ();
    	myMediaTypes.add(MediaType.parseMediaType("application/json;charset=utf-8"));
    	myMediaTypes.add(MediaType.parseMediaType("text/json;charset=utf-8"));
    	myMediaTypes.add(MediaType.parseMediaType("text/html;charset=utf-8"));
    	converter.setSupportedMediaTypes(myMediaTypes);
        return converter;
    }
	
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }    
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("/static/");
    }
        
        
    @Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*"); 
	}
    
    
    @Bean
    public static PropertyPlaceholderConfigurer propertyConfigurer() throws IOException {
        PropertyPlaceholderConfigurer props = new PropertyPlaceholderConfigurer();
        props.setLocations(
                new Resource[] {
                        new ClassPathResource("conf.properties")
                        }
                );
        props.setIgnoreResourceNotFound(true);
        return props;
    }
    
    /*@Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new CommonInterceptor());
    }*/

}
