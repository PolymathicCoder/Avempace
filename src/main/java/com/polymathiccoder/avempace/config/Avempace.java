package com.polymathiccoder.avempace.config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

import com.polymathiccoder.avempace.RepositoryImplTest;
import com.polymathiccoder.avempace.entity.service.RepositoryFactory;

import dagger.ObjectGraph;

public class Avempace {
	private static final File LOCAL_DYNAMODB_BINARY = new File(RepositoryImplTest.class.getClassLoader().getResource("dynamodb_local_2013-09-12/DynamoDBLocal.jar").getFile());

	static {
		final ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.submit(
				new Runnable() {
					@Override
					public void run() {
						final CommandLine cmdLine = CommandLine.parse("java -Djava.library.path="+ LOCAL_DYNAMODB_BINARY.getParentFile().getAbsolutePath() + " -jar " + LOCAL_DYNAMODB_BINARY.getAbsolutePath());
						final DefaultExecutor executor = new DefaultExecutor();
						executor.setExitValue(1);
						executor.setProcessDestroyer(new ShutdownHookProcessDestroyer());
						try {
							executor.execute(cmdLine);
						} catch (final IOException ioException) {
							//TODO handle better
						}
					}
				}
		);
	}

	public static RepositoryFactory getRepositoryFactory() {
		final ObjectGraph objectGraph = ObjectGraph.create(new AvempaceModule());
		objectGraph.injectStatics();

		return objectGraph.get(RepositoryFactory.class);
	}

}
