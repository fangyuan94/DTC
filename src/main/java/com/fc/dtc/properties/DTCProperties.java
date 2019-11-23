package com.fc.dtc.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author fangyuan
 *
 */
@Validated
@ConfigurationProperties(prefix =DTCProperties.PREFIX)
@Getter
@Setter
public class DTCProperties {

    public static final String PREFIX = "dtc";

    private boolean enabled;

}
