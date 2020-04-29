package me.davidnery.confighelper.models.sections;

import java.util.LinkedHashMap;

/**
 * A class that permit the manipulation of keys and values
 * without an YAMLConfiguration class
 */
public abstract class BaseSection {

    protected final LinkedHashMap<String, Object> SECTION;

    public BaseSection() {
        SECTION = new LinkedHashMap<>();
    }

    /**
     * Check if section contains the {@code key}
     *
     * @param key the key
     * @return {@code true} if yes or {@code false} if no
     */
    public boolean contains(String key) {
        return SECTION.containsKey(key);
    }

    /**
     * Check if section contains the {@code key} with the {@link Class} type
     *
     * @param key     the key
     * @param keyType the type of key
     * @return {@code true} if yes or {@code false} if no
     */
    public boolean contains(String key, Class<?> keyType) {
        Object value = SECTION.get(key);
        return value != null && value.getClass() == keyType;
    }

    /**
     * Get object associated with key
     *
     * @param key the key
     * @return the object if key exists in section or {@code null} if not exists
     */
    public Object get(String key) {
        return get(key, null);
    }

    /**
     * Get object associated with key
     *
     * @param key          the key
     * @param defaultValue default value to return if section not contains the key
     * @return the object associated or the {@code defaultValue}
     */
    public Object get(String key, Object defaultValue) {
        String[] keys = key.split("\\.");
        int length = keys.length;

        Object object = defaultValue;

        if (length > 1) {
            LinkedHashMap<String, Object> lastSection = SECTION;

            for (int i = 0; i < length; i++) {
                Object keyValue = lastSection.get(keys[i]);
                if (keyValue instanceof LinkedHashMap)
                    if (i == length - 1)
                        object = keyValue;
                    else
                        lastSection = (LinkedHashMap<String, Object>) keyValue;
                else
                    object = keyValue;
            }
        } else
            object = SECTION.get(key);

        return object == null ? defaultValue : object;
    }

    /**
     * Get a copy of stored section
     *
     * @return the copy
     */
    public LinkedHashMap<String, Object> get() {
        return (LinkedHashMap<String, Object>) SECTION.clone();
    }

    /**
     * Set a value to a key
     *
     * @param key   the key to save
     * @param value the associated value
     */
    public void set(String key, Object value) {
        String[] keys = key.split("\\.");
        int length = keys.length;

        if (length > 1) {
            LinkedHashMap<String, Object> lastSection = SECTION;

            for (int i = 0; i < length; i++) {
                if (i == length - 1) {
                    lastSection.put(keys[i], value);
                    break;
                }

                LinkedHashMap<String, Object> tempMap;
                Object keyValue = lastSection.get(keys[i]);
                if (keyValue instanceof LinkedHashMap)
                    tempMap = (LinkedHashMap<String, Object>) keyValue;
                else {
                    tempMap = new LinkedHashMap<>();
                }

                lastSection.put(keys[i], tempMap);
                lastSection = tempMap;
            }
        } else {
            SECTION.put(key, value);
        }
    }

    /**
     * Get {@link String} associated with key
     *
     * @param key the key
     * @return the String or null if the key not exists or not store a value with String type
     */
    public String getString(String key) {
        return getString(key, null);
    }

    /**
     * Get {@link String} associated with key
     *
     * @param key          the key
     * @param defaultValue value to return if the key not exists or not store a value with String type
     * @return the String associated or {@code defaultValue}
     */
    public String getString(String key, String defaultValue) {
        Object value = get(key, defaultValue);
        return value instanceof String ? (String) value : defaultValue;
    }

    /**
     * Get {@code int} associated with key
     *
     * @param key the key
     * @return the int or {@code 0} if the key not exists or not store a value with int type
     */
    public int getInt(String key) {
        return getInt(key, 0);
    }

    /**
     * Get {@code int} associated with key
     *
     * @param key          the key
     * @param defaultValue value to return if the key not exists or not store a value with int type
     * @return the int associated or {@code defaultValue}
     */
    public int getInt(String key, int defaultValue) {
        Object value = get(key, defaultValue);
        return value instanceof Number ? ((Number) value).intValue() : defaultValue;
    }

    /**
     * Get {@code double} associated with key
     *
     * @param key the key
     * @return the double or {@code 0.0} if the key not exists or not store a value with double type
     */
    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    /**
     * Get {@code double} associated with key
     *
     * @param key          the key
     * @param defaultValue value to return if the key not exists or not store a value with double type
     * @return the double associated or {@code defaultValue}
     */
    public double getDouble(String key, double defaultValue) {
        Object value = get(key, defaultValue);
        return value instanceof Number ? ((Number) value).doubleValue() : defaultValue;
    }

    /**
     * Get {@code boolean} associated with key
     *
     * @param key the key
     * @return the boolean or {@code false} if the key not exists or not store a value with false type
     */
    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Get {@code boolean} associated with key
     *
     * @param key          the key
     * @param defaultValue value to return if the key not exists or not store a value with boolean type
     * @return the boolean associated or {@code defaultValue}
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = get(key, defaultValue);
        return value instanceof Boolean ? (boolean) value : defaultValue;
    }

    @Override
    public String toString() {
        return SECTION.toString();
    }
}
