package me.davidnery.confighelper.types.yaml;

import me.davidnery.confighelper.annotations.yaml.YAMLKey;
import me.davidnery.confighelper.annotations.yaml.YAMLSection;
import me.davidnery.confighelper.exceptions.IncompatibleKeyType;
import me.davidnery.confighelper.exceptions.SectionNotValid;
import me.davidnery.confighelper.models.sections.BaseSection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public class YAMLUtils {

    protected <T> T setup(T object, LinkedHashMap<String, Object> values) throws IncompatibleKeyType {
        LinkedHashMap<String, Field> keys = loadKeys(object);

        try {
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                Field field = keys.get(entry.getKey().toLowerCase());
                if (field != null) {
                    field.setAccessible(true);
                    if (field.getAnnotation(YAMLSection.class) != null
                            && entry.getValue() instanceof LinkedHashMap) {
                        Object objectKey = field.get(object);
                        if (objectKey == null) {
                            Constructor<?> defaultConstructor = field.getType().getDeclaredConstructor();
                            defaultConstructor.setAccessible(true);
                            objectKey = defaultConstructor.newInstance();
                        }

                        if (objectKey instanceof BaseSection)
                            setMapSection(objectKey, (BaseSection) entry.getValue());
                        else
                            field.set(object, setup(
                                    objectKey,
                                    (LinkedHashMap<String, Object>) entry.getValue()
                            ));
                    } else {
                        if (field.getType() != entry.getValue().getClass() && !field.getType().isPrimitive())
                            throw new IncompatibleKeyType(object, field, entry.getValue().getClass());

                        field.set(object, entry.getValue());
                    }
                }
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | SectionNotValid e) {
            e.printStackTrace();
        }

        return object;
    }

    protected LinkedHashMap<String, Object> process(Object object) {
        LinkedHashMap<String, Object> values = new LinkedHashMap<>();

        try {
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.getAnnotation(YAMLKey.class) == null) continue;

                field.setAccessible(true);
                Object value = field.get(object);
                if (value == null) continue;

                if (field.getType() == BaseSection.class)
                    values.put(field.getName(), ((BaseSection) field.get(object)).get());
                else if (field.getAnnotation(YAMLSection.class) != null)
                    values.put(field.getName(), process(field.get(object)));
                else
                    values.put(field.getName(), field.get(object));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return values;
    }

    protected void setMapSection(Object map, BaseSection section) throws SectionNotValid {
        try {
            Field sectionField = getSectionField(section.getClass());
            sectionField.set(section, map);
        } catch (SectionNotValid | NoSuchFieldException e) {
            try {
                Field sectionField = getSectionField(section.getClass().getSuperclass());
                sectionField.set(section, map);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                throw new SectionNotValid();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected LinkedHashMap<String, Field> loadKeys(Object object) {
        LinkedHashMap<String, Field> keys = new LinkedHashMap<>();
        Class<?> configClass = object.getClass();

        for (Field field : configClass.getDeclaredFields())
            if (field.getAnnotation(YAMLKey.class) != null)
                keys.put(field.getName().toLowerCase(), field);

        return keys;
    }

    private Field getSectionField(Class<?> section) throws SectionNotValid, NoSuchFieldException {
        if (section == null) throw new SectionNotValid();

        Field sectionMap = section.getDeclaredField("SECTION");
        sectionMap.setAccessible(true);

        return sectionMap;
    }

}
