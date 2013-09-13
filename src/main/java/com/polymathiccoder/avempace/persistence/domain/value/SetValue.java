package com.polymathiccoder.avempace.persistence.domain.value;

import static org.fest.reflect.core.Reflection.constructor;

import java.util.HashSet;
import java.util.Set;

import org.pojomatic.Pojomatic;
import org.pojomatic.annotations.AutoProperty;

@AutoProperty
public abstract class SetValue<SCALAR extends ScalarValue<INTRINSIC, PERSISTENT>, INTRINSIC, PERSISTENT> extends PersistentValue {
// Fields
	protected final Set<SCALAR> scalarValues;

// Life cycle
	protected SetValue(final Set<INTRINSIC> intrinsicDataRepresentations, final Class<INTRINSIC> intrinsicDataType, Class<SCALAR> scalarType) {
		scalarValues = new HashSet<>();
		for (final INTRINSIC value : intrinsicDataRepresentations) {
			scalarValues.add(constructor()
					.withParameterTypes(intrinsicDataType)
					.in(scalarType)
					.newInstance(value));
		}
	}

// Behavior
	public Set<PERSISTENT> toPhysicalDataRepresentations() {
		final Set<PERSISTENT> physicalDataRepresentations = new HashSet<>();

		for (final SCALAR scalarValue : scalarValues) {
			physicalDataRepresentations.add(scalarValue.toPhysicalDataRepresentation());
		}

		return physicalDataRepresentations;
	}

	public <F> Set<F> toPojoValues(final Class<F> entityPropertyValueType) {
		final Set<F> entityPropertyValues = new HashSet<>();

		for (final SCALAR scalarValue : scalarValues) {
			entityPropertyValues.add(scalarValue.toEntityPropertyValue(entityPropertyValueType));
		}

		return entityPropertyValues;
	}

	@Override
	public Object toPojo(final Class<?> clazz) {
		return toPojoValues(clazz);
	}

// Common methods
	@Override
	public boolean equals(final Object other) { return Pojomatic.equals(this, other); }
	@Override
	public int hashCode() { return Pojomatic.hashCode(this); }
	@Override
	public String toString() { return Pojomatic.toString(this); }
}
