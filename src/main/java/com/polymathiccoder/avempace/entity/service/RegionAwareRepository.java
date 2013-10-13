package com.polymathiccoder.avempace.entity.service;

import java.util.List;

import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.entity.domain.EntityPropertyValueCriteria;
import com.polymathiccoder.avempace.entity.domain.EntityPropertyValueOperations;

public interface RegionAwareRepository<T> {
	T find(final EntityPropertyValueCriteria entityPropertyValueCriteria, final Region region);

	List<T> findAllBy(final EntityPropertyValueCriteria entityPropertyValuePropertyValueCriteria, final Region region);
	List<T> findAll(final Region region);

	void save(final T pojo, final Region region);

	void update(final EntityPropertyValueCriteria entityPropertyValueCriteria, final EntityPropertyValueOperations entityPropertyValueOperations, final Region region);

	void remove(final T pojo, final Region region);
	void drop(final Region region);
}
