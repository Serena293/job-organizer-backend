package com.project.job_organizer.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {


        @Bean
        public Cloudinary cloudinary() {
            return new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", System.getenv("CL_NAME"),
                    "api_key", System.getenv("CL_KEY"),
                    "api_secret", System.getenv("CL_SECRET"),
                    "secure", true
            ));
        }    }

