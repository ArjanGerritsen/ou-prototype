package nl.ou.se.rest.fuzzer.rdm.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RdmParameterMeta {

    // constants
    public static final String PATTERN = "PATTERN";
    public static final String FORMAT = "FORMAT";

    public static final String MIN_VALUE = "MIN_VALUE";
    public static final String MAX_VALUE = "MAX_VALUE";
    
    public static final String MIN_LENGTH = "MIN_LENGTH";
    public static final String MAX_LENGTH = "MAX_LENGTH";
    
    public static final String MIN_ITEMS = "MIN_ITEMS";
    public static final String MAX_ITEMS = "MAX_ITEMS";

    public static final String ENUM = "ENUM";
    public static final String DEFAULT = "DEFAULT";

    // variables
    private String key;
    private Object value;

    // constructors
    public RdmParameterMeta(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    // getters and setters
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    // toString
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }    
}