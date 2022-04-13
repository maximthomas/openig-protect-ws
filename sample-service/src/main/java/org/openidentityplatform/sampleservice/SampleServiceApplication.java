package org.openidentityplatform.sampleservice;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SpringBootApplication
public class SampleServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SampleServiceApplication.class, args);
    }

    @RestController
    public static class IndexController {
        @RequestMapping("/")
        public Map<String, String> index(@RequestBody String body) {
            return Collections.singletonMap("hello", "world");
        }

        @RequestMapping("/secure")
        public Map<String, String> secure(HttpServletRequest request) {

            return Collections.singletonMap("hello", request.getHeader("X-Auth-Username"));
        }

        @RequestMapping("/secured")
        public Map<String, Object> secured(HttpServletRequest request) throws JsonProcessingException {
            ObjectMapper mapper = new ObjectMapper();
        	Map<String, Object> result = new HashMap<>();
            Enumeration<String> headerNames = request.getHeaderNames();
            Map<String, String> headers = new HashMap<>();
            if (headerNames != null) {
            	while (headerNames.hasMoreElements()) {
            		String headerName = headerNames.nextElement();
            		headers.put(headerName, request.getHeader(headerName));
            	}
            }
            result.put("headers", headers);
            
            Map<String, Object> jwtMap = new HashMap<>();
            
            String authList= request.getHeader("Authorization");
    		if (StringUtils.isNotBlank(authList)){
    			for (String authorization: new HashSet<>(Arrays.asList(authList.split("[,;]")))){
    				authorization=StringUtils.stripToEmpty(authorization);
    				if (StringUtils.startsWithIgnoreCase(authorization, "Bearer")) {
    					String jwt = authorization.replaceFirst("Bearer\\s+", "");
    					
    					System.out.println("------------ Decode JWT ------------");
			            String[] split_string = jwt.split("\\.");
			            String base64EncodedHeader = split_string[0];
			            String base64EncodedBody = split_string[1];
			            String base64EncodedSignature = split_string[2];

			            System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
			            Base64 base64Url = new Base64(true);
			            String header = new String(base64Url.decode(base64EncodedHeader));
			            System.out.println("JWT Header : " + header);


			            System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
			            String body = new String(base64Url.decode(base64EncodedBody));
			            System.out.println("JWT Body : "+body);  
			            
			            jwtMap.put("header", mapper.readValue(header, Map.class));
			            jwtMap.put("body", mapper.readValue(body, Map.class));
    				}
    			}
    		}   
    		result.put("jwt", jwtMap);
            return result;
        }
    }

	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();

		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000);
		filter.setIncludeHeaders(true);

		return filter;
	}
}
