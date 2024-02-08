package org.musketeers.utility;

import java.util.Arrays;
import java.util.UUID;

public class CodeGenerator {
    public static String generateCode(){
        String randomCode = UUID.randomUUID().toString();
        System.out.println(randomCode);

        String[] split = randomCode.split("-");
        StringBuilder activationCode = new StringBuilder();

        Arrays.stream(split).forEach(c -> activationCode.append(c.charAt(0)));

        System.out.println(activationCode);

        return activationCode.toString();
    }

}
