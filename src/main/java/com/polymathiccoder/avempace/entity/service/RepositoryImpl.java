package com.polymathiccoder.avempace.entity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.entity.domain.EntityPropertyValueCriteria;
import com.polymathiccoder.avempace.entity.domain.EntityPropertyValueOperations;
import com.polymathiccoder.avempace.persistence.service.ddl.DynamoDBDDLOperationsService;
import com.polymathiccoder.avempace.persistence.service.dml.DynamoDBDMLOperationsService;

public class RepositoryImpl<T> implements Repository<T> {
// Fields
	private final Model<T> model;

	private final RegionAwareRepository<T> regionAwareRepository;

// Life cycle
	public RepositoryImpl(final DynamoDBDDLOperationsService dynamoDBDDLOperationsService, final DynamoDBDMLOperationsService dynamoDBDMLOperationsService, final Model<T> model) {
		this.model = model;
		regionAwareRepository = RepositoryLogger.newInstance(
				new RegionAwareRepositoryImpl<T>(dynamoDBDDLOperationsService, dynamoDBDMLOperationsService, model));
	}

// Behavior
	@Override
	public T find(final EntityPropertyValueCriteria entityPropertyValueCriteria) {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		return regionAwareRepository.find(entityPropertyValueCriteria, distribbutionDefinition.lookupTheClosestRegion());
	}

	@Override
	public List<T> findAllBy(final EntityPropertyValueCriteria entityPropertyValuePropertyValueCriteria) {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		return regionAwareRepository.findAllBy(entityPropertyValuePropertyValueCriteria, distribbutionDefinition.lookupTheClosestRegion());
	}

	@Override
	public List<T> findAll() {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		return regionAwareRepository.findAll(distribbutionDefinition.lookupTheClosestRegion());
	}

	@Override
	public void save(final T pojo) {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		final Region primaryRegion = distribbutionDefinition.getPrimaryRegion();
		regionAwareRepository.save(pojo, primaryRegion);

		if (distribbutionDefinition.isPropagatedAcrossAllRegions() && ! distribbutionDefinition.getSecondaryRegions().isEmpty()) {
			final ExecutorService executor = Executors.newFixedThreadPool(distribbutionDefinition.getSecondaryRegions().size() + 1);

			for (final Region secondaryRegion : distribbutionDefinition.getSecondaryRegions()) {
				executor.execute(
						new Runnable() {
							@Override
							public void run() {
								regionAwareRepository.save(pojo, secondaryRegion);
							}
						});
			}
			executor.shutdown();
		}
	}

	@Override
	public void update(final EntityPropertyValueCriteria entityPropertyValueCriteria, final EntityPropertyValueOperations entityPropertyValueOperations) {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		final Region primaryRegion = distribbutionDefinition.getPrimaryRegion();
		regionAwareRepository.update(entityPropertyValueCriteria, entityPropertyValueOperations, primaryRegion);

		if (distribbutionDefinition.isPropagatedAcrossAllRegions() && ! distribbutionDefinition.getSecondaryRegions().isEmpty()) {
			final ExecutorService executor = Executors.newFixedThreadPool(distribbutionDefinition.getSecondaryRegions().size() + 1);
			for (final Region secondaryRegion : distribbutionDefinition.getSecondaryRegions()) {
				executor.execute(
						new Runnable() {
							@Override
							public void run() {
								regionAwareRepository.update(entityPropertyValueCriteria, entityPropertyValueOperations, secondaryRegion);
							}
						});
			}
			executor.shutdown();
		}
	}

	@Override
	public void remove(final T pojo) {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		final Region primaryRegion = distribbutionDefinition.getPrimaryRegion();
		regionAwareRepository.remove(pojo, primaryRegion);

		if (distribbutionDefinition.isPropagatedAcrossAllRegions() && ! distribbutionDefinition.getSecondaryRegions().isEmpty()) {
			final ExecutorService executor = Executors.newFixedThreadPool(distribbutionDefinition.getSecondaryRegions().size() + 1);
			for (final Region secondaryRegion : distribbutionDefinition.getSecondaryRegions()) {
				executor.execute(
						new Runnable() {
							@Override
							public void run() {
								regionAwareRepository.remove(pojo, secondaryRegion);
							}
						});
			}
			executor.shutdown();
		}
	}

	@Override
	public void drop() {
		final DistribbutionDefinition distribbutionDefinition = model.getDistribbutionDefinition();

		final Set<Region> allRegions = new HashSet<>();
		allRegions.add(distribbutionDefinition.getPrimaryRegion());
		allRegions.addAll(distribbutionDefinition.getSecondaryRegions());

		final ExecutorService executor = Executors.newFixedThreadPool(distribbutionDefinition.getSecondaryRegions().size() + 1);
		for (final Region region : allRegions) {
			executor.execute(
					new Runnable() {
						@Override
						public void run() {
						regionAwareRepository.drop(region);
						}
					});
		}
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException interruptedException) {
			//TODO handle better
		}
	}
}
