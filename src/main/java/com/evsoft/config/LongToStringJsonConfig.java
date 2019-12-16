package com.evsoft.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


@Configuration
public class LongToStringJsonConfig {

    @Primary
    @Bean("jackson2HttpMessageConverter")
    public MappingJackson2HttpMessageConverter jackson2ObjectMapperBuilderCustomizer() {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);

        return jackson2HttpMessageConverter;
    }
/*    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        jackson2HttpMessageConverter.setObjectMapper(objectMapper);
        converters.add(jackson2HttpMessageConverter);
    }*/

/*    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AbstractInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/static/","/views/","/static/**","/views/**");
        super.addInterceptors(registry);
    }*/

/*    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/swagger-ui.html")
                .addResourceLocations("classpath:/swagger-resources/**")
                .addResourceLocations("classpath:/v2/api-docs")
                .addResourceLocations("classpath:/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/static/**")
                .addResourceLocations("classpath:/views/")
                .addResourceLocations("classpath:/views/**")
                .addResourceLocations("classpath:/META-INF/static/**")
                .addResourceLocations("classpath:/META-INF/views/**");
        super.addResourceHandlers(registry);
    }*/

}