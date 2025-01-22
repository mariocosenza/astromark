package it.astromark.authentication.utils;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;


public class PasswordUtils {

    public static String hashPassword(String password) {
        return Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
