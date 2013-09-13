package com.polymathiccoder.avempace.entity.service;

import com.polymathiccoder.avempace.config.NimbleModule;
import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.persistence.service.dml.DynamoDBDMLOperationsService;

import dagger.ObjectGraph;

public final class RepositoryFactory {
// Static behavior
	public static <T> Repository<T> create(final Class<T> pojoType) {
		final ObjectGraph objectGraph = ObjectGraph.create(new NimbleModule());
		objectGraph.injectStatics();
		final DynamoDBDMLOperationsService dmlOperations = objectGraph.get(DynamoDBDMLOperationsService.class);

		return new RepositoryImpl<>(dmlOperations, pojoType, Region.SA_EAST_1);
	}
}
