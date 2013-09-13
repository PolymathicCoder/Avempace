package com.polymathiccoder.avempace.persistence.domain.attribute;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

import com.polymathiccoder.avempace.persistence.domain.value.PersistentValue;

@AutoProperty
public class AttributeType {
// Fields
	private final Class<? extends PersistentValue> value;

// Life cycle
	public AttributeType(final Class<? extends PersistentValue> value) {
		this.value = value;
	}

// Accessors and mutators
	public Class<?> get() { return value; }

// Common methods
	@Override
	public boolean equals(final Object other) { return Pojomatic.equals(this, other); }
	@Override
	public int hashCode() { return Pojomatic.hashCode(this); }
	@Override
	public String toString() { return Pojomatic.toString(this); }
}
