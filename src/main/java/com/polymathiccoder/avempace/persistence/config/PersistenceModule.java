package com.polymathiccoder.avempace.persistence.config;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.polymathiccoder.avempace.config.NimbleConfiguration;
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
	@Provides
	public AmazonDynamoDB provideAmazonDynamoDB(final NimbleConfiguration nimbleConfiguration) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(nimbleConfiguration.getAccessKey(), nimbleConfiguration.getSecretKey());
        final AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(credentials);
		return amazonDynamoDB;
    }

	@Provides
	public AmazonDynamoDBAsync provideAmazonDynamoDBClientAsync(final NimbleConfiguration nimbleConfiguration) {
        final BasicAWSCredentials credentials = new BasicAWSCredentials(nimbleConfiguration.getAccessKey(), nimbleConfiguration.getSecretKey());
        final AmazonDynamoDBAsync amazonDynamoDBAsync = new AmazonDynamoDBAsyncClient(credentials);
		return amazonDynamoDBAsync;
    }

	@Provides
	public DynamoDBDMLOperationsService provideAynamoDBDMLOperationsService(final AmazonDynamoDB amazonDynamoDB, final AmazonDynamoDBAsync amazonDynamoDBAsync) {
		return new DynamoDBDMLOperationsServiceImpl(amazonDynamoDB, amazonDynamoDBAsync);
	}

	@Provides
	public DynamoDBDDLOperationsService dynamoDBDDLOperationsService(final AmazonDynamoDB amazonDynamoDB, final AmazonDynamoDBAsync amazonDynamoDBAsync) {
		return new DynamoDBDDLOperationsServiceImpl(amazonDynamoDB, amazonDynamoDBAsync);
	}
}
