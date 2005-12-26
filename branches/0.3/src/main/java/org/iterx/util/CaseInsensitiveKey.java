package org.iterx.util;

public class CaseInsensitiveKey {

    private String value;

    public CaseInsensitiveKey(String key) {

        value = key.toLowerCase();
    }

    public int hashCode() {

        return value.hashCode();
    }

    public boolean equals(Object object) {

        return (this == object ||
                (object instanceof CaseInsensitiveKey &&
                 (((CaseInsensitiveKey) object).value).equals(value)));
    }
}
