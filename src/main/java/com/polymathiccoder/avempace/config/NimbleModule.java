package com.polymathiccoder.avempace.config;

import javax.inject.Singleton;

import com.polymathiccoder.avempace.config.NimbleConfiguration.SchemaGenerationStrategy;
import com.polymathiccoder.avempace.meta.config.MetaModule;
import com.polymathiccoder.avempace.persistence.config.PersistenceModule;

import dagger.Module;
import dagger.Provides;

@Module(
		includes = {
				MetaModule.class,
				PersistenceModule.class
		}
)
public class NimbleModule {
	@Provides @Singleton
	public NimbleConfiguration provideNimbleConfiguration() {
		return new NimbleConfiguration(
				"AKIAIQOONXLTVCMQWXZA",
				"Gi9Ip0hUHiRvfh06rLvS3lsKj28q1PGaqzhJOB2E",
				SchemaGenerationStrategy.DROP_CREATE);
	}
}
