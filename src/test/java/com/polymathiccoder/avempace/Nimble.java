package com.polymathiccoder.avempace;

import static com.polymathiccoder.avempace.criteria.domain.Conditions.beginsWith;
import static com.polymathiccoder.avempace.criteria.domain.Conditions.contains;
import static com.polymathiccoder.avempace.criteria.domain.Conditions.equalTo;
import static com.polymathiccoder.avempace.entity.domain.EntityPropertyValueCriteria.matching;
import static com.polymathiccoder.avempace.entity.domain.EntityPropertyValueCriterion.$;
import static com.polymathiccoder.avempace.entity.domain.EntityPropertyValueOperation.*;
import static com.polymathiccoder.avempace.entity.domain.EntityPropertyValueOperations.*;

import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBAsyncClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.polymathiccoder.avempace.config.Region;
import com.polymathiccoder.avempace.entity.service.Repository;
import com.polymathiccoder.avempace.entity.service.RepositoryFactory;
import com.polymathiccoder.avempace.mapping.Mapping;
import com.polymathiccoder.avempace.meta.model.MetaModel;
import com.polymathiccoder.avempace.persistence.domain.Table;
import com.polymathiccoder.avempace.persistence.domain.TableDefinition;
import com.polymathiccoder.avempace.persistence.domain.operation.ddl.CreateTable;
import com.polymathiccoder.avempace.persistence.domain.operation.ddl.DeleteTable;
import com.polymathiccoder.avempace.persistence.service.ddl.DynamoDBDDLOperationsServiceImpl;
import com.polymathiccoder.avempace.persistence.service.dml.DynamoDBDMLOperationsServiceImpl;

public class Nimble {
	public static void main(final String[] args) {
		final BasicAWSCredentials credentials = new BasicAWSCredentials("AKIAIQOONXLTVCMQWXZA", "Gi9Ip0hUHiRvfh06rLvS3lsKj28q1PGaqzhJOB2E");
        final AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(credentials);
        final AmazonDynamoDBAsync amazonDynamoDBAsync = new AmazonDynamoDBAsyncClient(credentials);

		final DynamoDBDDLOperationsServiceImpl ddlOperations =  new DynamoDBDDLOperationsServiceImpl(amazonDynamoDB, amazonDynamoDBAsync);
		ddlOperations.useRegion(Region.SA_EAST_1);

        final DynamoDBDMLOperationsServiceImpl dmlOperations =  new DynamoDBDMLOperationsServiceImpl(amazonDynamoDB, amazonDynamoDBAsync);
		dmlOperations.useRegion(Region.SA_EAST_1);

// DDL
		final TableDefinition tableDefinition = Mapping.create(MetaModel.create(Employee.class)).getTableDefinition();
		final Table table = Table.Builder.create(tableDefinition, Region.SA_EAST_1).build();

		final DeleteTable deleteTable = DeleteTable.Builder.create(table).build();
		//ddlOperations.deleteTable(deleteTable);

		final CreateTable createTable = CreateTable.Builder.create(table).build();
		//ddlOperations.createTable(createTable);

// DML
		final Employee employee0 = new Employee(0l, "Looser", 35, "PolymathicCoder Inc.", "USA");
		final Employee employee1 = new Employee(1l, "Abdelmonaim", 28, "PolymathicCoder Inc.", "USA");
		final Employee employee2 = new Employee(2l, "Maximino", 32, "PolymathicCoder Inc.", "USA");
		final Employee employee3 = new Employee(3l, "Tomasz", 22, "PolymathicCoder Inc.", "Poland");
		final Employee employee4 = new Employee(4l, "Fouad", 18, "PolymathicCoder Inc.", "Morocco");
		final Employee employee5 = new Employee(5l, "Abdelbadia", 23, "PolymathicCoder Inc.", "Canada");
		final Employee employee6 = new Employee(6l, "Yasser", 24, "PolymathicCoder Inc.", "Germany");

		final Repository<Employee> repository = RepositoryFactory.create(Employee.class);

	// Put
	System.out.println("+++ PUT");
		repository.save(employee0);
		repository.save(employee1);
		repository.save(employee2);
		repository.save(employee3);
		repository.save(employee4);
		repository.save(employee5);
		repository.save(employee6);

	// Delete
	System.out.println("+++ DELETE");
		repository.remove(employee0);

	// Get
	System.out.println("+++ GET");
		System.out.println(repository.find(
				matching(
						$("company").is(equalTo("PolymathicCoder Inc.")))
					.and(
						$("id").is(equalTo(3l)))
		));

	// Update
	System.out.println("+++ UPDATE");
		repository.update(
				matching(
						$("company").is(equalTo("PolymathicCoder Inc.")))
					.and(
						$("id").is(equalTo(3l))),
				apply(
						change("location").withValue("USA"))
					//.and(
						//add("tx").withValue("xx"))
					//.and(
						//remove("tx")
						//)
		);

	// Query
	System.out.println("+++ QUERY");

		List<Employee> employees;

		employees = repository.findAllBy(
				matching(
						$("location").is(equalTo("PolymathicCoder Inc.")))
					.and(
						$("id").is(equalTo(3)))
		);

		for (final Employee employee : employees) {
			System.out.println(employee);
		}

	// Index Query
	System.out.println("+++ QUERY INDEX");
		employees = repository.findAllBy(
				matching(
						$("company").is(equalTo("PolymathicCoder Inc.")))
					.and(
						$("location").is(equalTo("USA")))
		);

		for (final Employee employee : employees) {
			System.out.println(employee);
		}

	// Scan
	System.out.println("+++ SCAN");

		employees = repository.findAllBy(
				matching(
						$("firstName")
								.is(beginsWith("Max"))
								.is(contains("mino")))
		);

		for (final Employee employee : employees) {
			System.out.println(employee);
		}

		System.out.println("+++ SCAN ALL");
		employees = repository.findAll();

		for (final Employee employee : employees) {
			System.out.println(employee);
		}
	}
}
