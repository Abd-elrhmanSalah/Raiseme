package com.eprogs.raiseme.service.emailServices;


import com.eprogs.raiseme.constant.EmailTemplateConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TemplateService {

    private static final String TEMPLATE_DIR = "email-templates/";
    private final Map<String, String> templateCache = new ConcurrentHashMap<>();

    public String processTemplate(EmailTemplateConstant template, Map<String, String> variables) throws IOException {
        String content = getTemplateContent(template.getTemplateFileName());
        log.debug("found template: {}", content);
        return replaceVariables(content, variables);
    }

    private String getTemplateContent(String fileName) throws IOException {
        return templateCache.computeIfAbsent(fileName, fn -> {
            try {
                ClassLoader classLoader = TemplateService.class.getClassLoader();
                InputStream resourceStream = classLoader.getResourceAsStream(TEMPLATE_DIR + fn);

                if (resourceStream == null) {
                    throw new RuntimeException("Template not found: " + TEMPLATE_DIR + fn);
                }
                return StreamUtils.copyToString(resourceStream, StandardCharsets.UTF_8);

            } catch (IOException e) {
                throw new RuntimeException("Error loading template: " + fn, e);
            }
        });
    }

    /**
     * the naming convension of variables is: {var:key}
     *
     * @param content
     * @param variables
     * @return
     */
    private String replaceVariables(String content, Map<String, String> variables) {
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            content = content.replaceAll("\\{\\{var:" + entry.getKey() + "\\}\\}", entry.getValue());
        }
        return content;
    }
}