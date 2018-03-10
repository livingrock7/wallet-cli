package org.tron.explorer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@EnableScheduling
@SpringBootApplication
public class GrpcClientApplication {

  @Bean
    public ObjectMapper objectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    // disabled features:
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }

  @Autowired
  ObjectMapper objectMapper;

  @Bean
  public WebMvcConfigurer webMvcConfigurer() {
    return new WebMvcConfigurerAdapter() {
          /**
           * Keep "/static/**" prefix.
           */
          @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
            super.addResourceHandlers(registry);
            registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/");
        }

            /**
             * Add Java8 time support for Jackson.
             */
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
                converter.setObjectMapper(objectMapper);
                converters.add(converter);
                super.configureMessageConverters(converters);
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(GrpcClientApplication.class, args);
    }

}


