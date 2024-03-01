//package org.musketeers.config;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CloudinaryConfig {
//
//    @Value("${spending-service-config.cloudinary.cloud-name}")
//    private String cloudinaryCloudName;
//
//    @Value("${spending-service-config.cloudinary.api-key}")
//    private String cloudinaryApiKey;
//
//    @Value("${spending-service-config.cloudinary.api-secret}")
//    private String cloudinaryApiSecret;
//
//    @Bean
//    public Cloudinary cloudinary() {
//        return new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", cloudinaryCloudName,
//                "api_key", cloudinaryApiKey,
//                "api_secret", cloudinaryApiSecret));
//    }
//
//}
