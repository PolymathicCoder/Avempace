package com.polymathiccoder.avempace.persistence.domain.operation.validation;

import org.pojomatic.annotations.AutoProperty;

import com.polymathiccoder.avempace.persistence.domain.attribute.Attribute;

@AutoProperty
public interface RequiringPrimaryKeyHash {
	Attribute getPrimaryKeyHashAttribute();
}
