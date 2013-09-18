package com.polymathiccoder.avempace.persistence.config;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.polymathiccoder.avempace.config.AvempaceConfiguration;
import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.persistence.service.ddl.DynamoDBDDLOperationsService;
import com.polymathiccoder.avempace.persistence.service.ddl.DynamoDBDDLOperationsServiceImpl;
import com.polymathiccoder.avempace.persistence.service.dml.DynamoDBDMLOperationsService;
import com.polymathiccoder.avempace.persistence.service.dml.DynamoDBDMLOperationsServiceImpl;

import dagger.Module;
import dagger.Provides;

@Module(
		complete = false,
		injects = {
				DynamoDBDDLOperationsService.class,
				DynamoDBDMLOperationsService.class
		}
)
public class PersistenceModule {
	@Provides @Singleton
	public Map<Region, AmazonDynamoDB> provideAmazonDynamoDBsIndexedByRegion(final AvempaceConfiguration avempaceConfiguration) {
		final Map<Region, AmazonDynamoDB> amazonDynamoDBsIndexedByRegion = new HashMap<>();
		for (final Region region : Region.values()) {
			String accessKey = avempaceConfiguration.getAccessKey();
			String secretKey = avempaceConfiguration.getSecretKey();
			String endpoint = region.getEndpoint();

			AmazonDynamoDB amazonDynamoDB;
			if (accessKey.isEmpty() || secretKey.isEmpty()) {
				accessKey = "NONE";
				secretKey = "NONE";
				endpoint = "http://localhost:8000";
			}

			final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
	        amazonDynamoDB = new AmazonDynamoDBClient(credentials);
	        amazonDynamoDB.setEndpoint(endpoint);

	        amazonDynamoDBsIndexedByRegion.put(region, amazonDynamoDB);
		}
		return amazonDynamoDBsIndexedByRegion;
    }

	@Provides @Singleton
	public Map<Region, AmazonDynamoDBAsync> provideAmazonDynamoDBAsyncsIndexedByRegion(final AvempaceConfiguration avempaceConfiguration) {
		final Map<Region, AmazonDynamoDBAsync> amazonDynamoDBAsyncsIndexedByRegion = new HashMap<>();
		for (final Region region : Region.values()) {
			String accessKey = avempaceConfiguration.getAccessKey();
			String secretKey = avempaceConfiguration.getSecretKey();
			String endpoint = region.getEndpoint();

			AmazonDynamoDBAsync amazonDynamoDBAsync;
			if (accessKey.isEmpty() || secretKey.isEmpty()) {
				accessKey = "NONE";
				secretKey = "NONE";
				endpoint = "http://localhost:8000";
			}

			final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
			amazonDynamoDBAsync = new AmazonDynamoDBAsyncClient(credentials);
	        amazonDynamoDBAsync.setEndpoint(endpoint);

	        amazonDynamoDBAsyncsIndexedByRegion.put(region, amazonDynamoDBAsync);
		}
		return amazonDynamoDBAsyncsIndexedByRegion;
    }

	@Provides @Singleton
	public DynamoDBDMLOperationsService provideAynamoDBDMLOperationsService(final Map<Region, AmazonDynamoDB> amazonDynamoDBsIndexedByRegion, final Map<Region, AmazonDynamoDBAsync> amazonDynamoDBAsyncsIndexedByRegion) {
		return new DynamoDBDMLOperationsServiceImpl(amazonDynamoDBsIndexedByRegion, amazonDynamoDBAsyncsIndexedByRegion);
	}

	@Provides @Singleton
	public DynamoDBDDLOperationsService dynamoDBDDLOperationsService(final Map<Region, AmazonDynamoDB> amazonDynamoDBsIndexedByRegion, final Map<Region, AmazonDynamoDBAsync> amazonDynamoDBAsyncsIndexedByRegion) {
		return new DynamoDBDDLOperationsServiceImpl(amazonDynamoDBsIndexedByRegion, amazonDynamoDBAsyncsIndexedByRegion);
	}
}
