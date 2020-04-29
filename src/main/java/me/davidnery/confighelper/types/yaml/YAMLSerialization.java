package me.davidnery.confighelper.types.yaml;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.stream.Collectors;

public abstract class YAMLSerialization {

    @Override
    public String toString() {
        YAMLUtils yamlUtils = new YAMLUtils();

        Collection<Field> fields = yamlUtils.loadKeys(this).values();
        return new StringBuilder("{")
                .append(String.join(", ", fields.stream().map(field -> {
                    try {
                        field.setAccessible(true);
                        return field.getName() + "=" + field.get(this);
                    } catch (IllegalAccessException e) {
                        return "";
                    }
                }).collect(Collectors.toList())))
                .append("}").toString();
    }

}
