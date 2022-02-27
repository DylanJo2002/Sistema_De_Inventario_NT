package com.nt.Backend_NT.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Configuration
public class SwaggerSpecConfig {

    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        return () -> {
        	
            SwaggerResource wsResource = new SwaggerResource();
            wsResource.setName("new spec");
            wsResource.setSwaggerVersion("2.0");
            Logger log  = Logger.getLogger(this.getClass().toString());
            wsResource.setLocation("/swagger.json");

            List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
            resources.clear();
            resources.add(wsResource);
            return resources;
        };
    }
}
