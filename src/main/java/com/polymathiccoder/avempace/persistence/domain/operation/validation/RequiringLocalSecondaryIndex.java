package com.polymathiccoder.avempace.persistence.domain.operation.validation;

import org.pojomatic.annotations.AutoProperty;

import com.google.common.base.Optional;
import com.polymathiccoder.avempace.persistence.domain.attribute.Attribute;

@AutoProperty
public interface RequiringLocalSecondaryIndex {
	Optional<Attribute> getLocalSecondaryIndexAttribute(final String indexName);
	String getIndexName();
}
