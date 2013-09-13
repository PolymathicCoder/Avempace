package com.polymathiccoder.avempace.meta.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.polymathiccoder.avempace.config.Region;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE })
@Documented
public @interface Entity {
	Region primaryRegion() default Region.US_EAST_1;
	Region[] secondaryRegions() default {};
	boolean propagatedAcrossAllRegions() default true;
}
