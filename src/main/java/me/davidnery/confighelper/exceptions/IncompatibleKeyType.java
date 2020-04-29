package me.davidnery.confighelper.exceptions;

import java.lang.reflect.Field;

public class IncompatibleKeyType extends Exception {

    public IncompatibleKeyType(Object object, Field field, Class<?> configFieldType) {
        super(String.format(
                "Found %s in \"%s\" config key, but \"%s\" variable is %s in %s",
                configFieldType.getName(),
                field.getName(),
                field.getName(),
                field.getType().getName(),
                object.getClass().getName()
        ));
    }

}
