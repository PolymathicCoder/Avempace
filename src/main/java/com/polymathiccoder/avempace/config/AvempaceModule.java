package com.polymathiccoder.avempace.config;

import javax.inject.Singleton;

import org.apache.commons.lang3.StringUtils;

import com.polymathiccoder.avempace.config.AvempaceConfiguration.SchemaGenerationStrategy;
import com.polymathiccoder.avempace.entity.service.RepositoryFactory;
import com.polymathiccoder.avempace.meta.config.MetaModule;
import com.polymathiccoder.avempace.persistence.config.PersistenceModule;

import dagger.Module;
import dagger.Provides;

@Module(
		includes = {
				MetaModule.class,
				PersistenceModule.class
		},
		injects = {
			RepositoryFactory.class
		}
)
public class AvempaceModule {
	@Provides @Singleton
	public AvempaceConfiguration provideAvempaceConfiguration() {
		return new AvempaceConfiguration(
				//"AKIAIQOONXLTVCMQWXZA",
				//"Gi9Ip0hUHiRvfh06rLvS3lsKj28q1PGaqzhJOB2E",
				StringUtils.EMPTY, StringUtils.EMPTY,
				SchemaGenerationStrategy.CLEAN_SLATE);
	}
}
