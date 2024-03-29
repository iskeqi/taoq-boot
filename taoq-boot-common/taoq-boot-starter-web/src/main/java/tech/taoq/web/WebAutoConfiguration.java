package tech.taoq.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.taoq.common.util.DateUtil;
import tech.taoq.web.mvc.converter.MyStringToLocalDateConverter;
import tech.taoq.web.mvc.converter.MyStringToLocalDateTimeConverter;
import tech.taoq.web.mvc.converter.MyStringToNumberConverterFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * WebAutoConfiguration
 *
 * @author keqi
 */
@EnableWebMvc
@ServletComponentScan
@ComponentScan("tech.taoq.web")
public class WebAutoConfiguration {

    /**
     * 配置允许跨域的过滤器
     *
     * @return r
     */
    @Bean
    @ConditionalOnProperty(prefix = "taoq.web", value = "cors", matchIfMissing = true)
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    /**
     * 替换掉 SpringBoot 默认配置的 MappingJackson2HttpMessageConverter 对象
     *
     * @return r
     */
    @Bean
    @ConditionalOnProperty(prefix = "taoq.web", value = "my-mapping-jackson2-http-message-converter",
            matchIfMissing = true)
    public WebMvcConfigurer myMappingJackson2HttpMessageConverter() {
        return new WebMvcConfigurer() {
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

                ObjectMapper objectMapper = new ObjectMapper();
                // 反序列化时，忽略掉不认识的属性
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

                // Java8 日期时间处理(此处刻意不对 java.util.Date 做配置，程序中能不用这个类就不用)
                JavaTimeModule javaTimeModule = new JavaTimeModule();
                javaTimeModule.addSerializer(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.LOCAL_DATE_TIME)));
                javaTimeModule.addSerializer(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(DateUtil.LOCAL_DATE)));
                javaTimeModule.addSerializer(LocalTime.class,
                        new LocalTimeSerializer(DateTimeFormatter.ofPattern(DateUtil.LOCAL_TIME)));
                javaTimeModule.addDeserializer(LocalDateTime.class,
                        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.LOCAL_DATE_TIME)));
                javaTimeModule.addDeserializer(LocalDate.class,
                        new LocalDateDeserializer(DateTimeFormatter.ofPattern(DateUtil.LOCAL_DATE)));
                javaTimeModule.addDeserializer(LocalTime.class,
                        new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DateUtil.LOCAL_TIME)));
                objectMapper.registerModule(javaTimeModule);

                converter.setObjectMapper(objectMapper);
                // 加到最前面，才能替换掉 Spring 默认的 MappingJackson2HttpMessageConverter 对象
                converters.add(0, converter);
            }
        };
    }

    /**
     * 配置 MyStringToLocalDateConverter 对象
     * <p>
     * 如果希望对 WebMvcConfigurer 进行扩展，直接继续实现 WebMvcConfigurer 即可
     * Spring 是支持同时存在多个实现类的，实现同一个方法的最终效果是叠加，并不会互相影响
     */
    @Bean
    @ConditionalOnProperty(prefix = "taoq.web", value = "my-string-to-local-date-converter",
            matchIfMissing = true)
    public WebMvcConfigurer myStringToLocalDateConverter() {
        return new WebMvcConfigurer() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverter(new MyStringToLocalDateConverter());
            }
        };
    }

    /**
     * 配置 MyStringToLocalDateTimeConverter 对象
     * <p>
     * 如果希望对 WebMvcConfigurer 进行扩展，直接继续实现 WebMvcConfigurer 即可
     * Spring 是支持同时存在多个实现类的，实现同一个方法的最终效果是叠加，并不会互相影响
     */
    @Bean
    @ConditionalOnProperty(prefix = "taoq.web", value = "my-string-to-local-date-time-converter",
            matchIfMissing = true)
    public WebMvcConfigurer myStringToLocalDateTimeConverter() {
        return new WebMvcConfigurer() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverter(new MyStringToLocalDateTimeConverter());
            }
        };
    }

    /**
     * 配置 MyStringToNumberConverterFactory 对象
     * <p>
     * 如果希望对 WebMvcConfigurer 进行扩展，直接继续实现 WebMvcConfigurer 即可
     * Spring 是支持同时存在多个实现类的，实现同一个方法的最终效果是叠加，并不会互相影响
     */
    @Bean
    @ConditionalOnProperty(prefix = "taoq.web", value = "my-string-to-number-converter-factory",
            matchIfMissing = true)
    public WebMvcConfigurer myStringToNumberConverterFactory() {
        return new WebMvcConfigurer() {
            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverterFactory(new MyStringToNumberConverterFactory());
            }
        };
    }
}
