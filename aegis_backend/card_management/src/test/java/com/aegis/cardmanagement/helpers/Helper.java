package com.aegis.cardmanagement.helpers;


import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Helper
{
    private static Object getRandVal(Class<?> fieldType) {
        Random random = new Random();
        if (fieldType.equals(String.class)) {
            return UUID.randomUUID().toString();
        }
        else if(Time.class.isAssignableFrom(fieldType)){
            return new Time(random.nextLong());
        }
        else if (Date.class.isAssignableFrom(fieldType)) {
            return Date.from(Instant.now().plus(Duration.ofDays(1 + new Random().nextInt(364))));
        }
        else if (fieldType.equals(Integer.TYPE)) {
            return random.nextInt();
        }
        else if (fieldType.equals(Long.TYPE)) {
            return random.nextLong();
        }
        else if(fieldType.equals(Double.TYPE))
        {
            return random.nextDouble();
        }
        else if (Enum.class.isAssignableFrom(fieldType)) {
            Object[] enumValues = fieldType.getEnumConstants();
            return enumValues[random.nextInt(enumValues.length)];
        }
        else if(Set.class.isAssignableFrom(fieldType))
            return new HashSet<>();
        else
            return null;
    }

    public static Object populate(Object obj, Class cls)
    {
        Field[] objectFields= cls.getDeclaredFields();

        for(Field field:objectFields)
        {
            try {
                field.setAccessible(true);
                field.set(obj, getRandVal(field.getType()));
            }

            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static String generateRandomString() {
        byte[] array = new byte[50]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}
