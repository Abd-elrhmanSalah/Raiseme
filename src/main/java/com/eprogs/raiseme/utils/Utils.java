package com.eprogs.raiseme.utils;

import com.eprogs.raiseme.dto.CustomPage;
import com.eprogs.raiseme.dto.authDTOS.SystemUserDetails;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordGenerator;
import org.passay.Rule;
import org.passay.WhitespaceRule;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Utils {


    public static Date convertFromStringToDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

    // This method creates a Pageable object with sorting based on the "createdDate" field.
    public static Pageable createOrderedPageable(Map<String, String> params) {
        int pageNumber = 0;
        int pageSize = 1;
        PageRequest pageRequest;
        if (params.containsKey("page")) {
            if (!params.get("page").equals("")) {
                pageNumber = Integer.parseInt(params.get("page"));
            }
        }

        if (params.containsKey("size")) {
            if (!params.get("size").equals("")) {
                pageSize = Integer.parseInt(params.get("size"));
            }
        }

        if (params.containsKey("sortOrder")) {
            String sortOrder = params.get("sortOrder") != null ? params.get("sortOrder") : "desc";
            Sort sort = Sort.by(sortOrder.equalsIgnoreCase("asc") ?
                    Sort.Direction.ASC : Sort.Direction.DESC, "createdDate");
            pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        } else {
            pageRequest = PageRequest.of(pageNumber, pageSize);
        }
        return pageRequest;
    }

    public static Pageable createPageable(Map<String, String> params) {
        int pageNumber = 0;
        int pageSize = 10;

        if (params.containsKey("page")) {
            if (!params.get("page").equals("")) {
                pageNumber = Integer.parseInt(params.get("page"));
            }
        }

        if (params.containsKey("size")) {
            if (!params.get("size").equals("")) {
                pageSize = Integer.parseInt(params.get("size"));
            }
        }

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return pageRequest;
    }

    public static SystemUserDetails getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            throw new UsernameNotFoundException("Not authenticated");
        }
        return (SystemUserDetails) auth.getPrincipal();
    }


    public static String generateRandomPassword() {
        // Initialize password generator
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        // Define rules for password generation
        List<Rule> rules = new ArrayList<>();

        //Rule 1: Password length should be in between
        rules.add(new LengthRule(16, 16));

        //Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());

        // Rule 3.a: At least one alphabetical character
        rules.add(new CharacterRule(EnglishCharacterData.Alphabetical, 1));

        // Rule 3.b: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));

        // Generate and return password
        return passwordGenerator.generatePassword(16, rules);
    }

    public static String generateOTP() {
        // Initialize password generator
        PasswordGenerator passwordGenerator = new PasswordGenerator();

        // Define rules for password generation
        List<Rule> rules = new ArrayList<>();

        //Rule 1: Password length should be in between
        rules.add(new LengthRule(4, 4));

        // Rule 3.b: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 4));

        // Generate and return password
        return passwordGenerator.generatePassword(4, rules);
    }


    public static <T> CustomPage<T> getPaginatedList(List<T> fullList, int page, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        int totalItems = fullList.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);
        long totalRecordsInDb = totalItems;

        if (page < 0 || page >= totalPages) {
            return new CustomPage<>(Collections.emptyList(), page, size, totalItems, totalPages, page == 0, true, totalRecordsInDb);
        }

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, totalItems);

        List<T> content = fullList.subList(fromIndex, toIndex);

        boolean isFirst = page == 0;
        boolean isLast = page == totalPages - 1;

        return new CustomPage<>(content, page, size, totalItems, totalPages, isLast, isFirst, totalRecordsInDb);
    }


    public static Map<String, String> decodeJWTTokenPayload(String token) {
        String[] parts = token.split("\\.");
        String payload = new String(java.util.Base64.getDecoder().decode(parts[1]));

        Map<String, String> payloadMap = new HashMap<>();
        String[] pairs = payload.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":");
            String key = keyValue[0].replace("\"", "").trim();
            String value = keyValue[1].replace("\"", "").trim();
            payloadMap.put(key, value);
        }

        return payloadMap;

    }

    public static boolean isCloseToNumber(double number, double target, double tolerance) {
        return Math.abs(number - target) <= tolerance;
    }

}
