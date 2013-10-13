package com.polymathiccoder.avempace.entity.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.polymathiccoder.avempace.config.Region;

public class RepositoryLogger<T> implements InvocationHandler {
// Static fields
	private static final Logger LOGGER = LoggerFactory.getLogger("com.polymathiccoder.nimble");

// Fields
	private final RegionAwareRepository<T> RegionAwareRepository;

// Life cycle
	@SuppressWarnings("unchecked")
	public static <T> RegionAwareRepository<T> newInstance(final RegionAwareRepository<T> RegionAwareRepository) {
		return (RegionAwareRepository<T>) Proxy.newProxyInstance(
				RegionAwareRepository.getClass().getClassLoader(),
				RegionAwareRepository.getClass().getInterfaces(),
				new RepositoryLogger<T>(RegionAwareRepository));
	}

	private RepositoryLogger(RegionAwareRepository<T> RegionAwareRepository) {
		this.RegionAwareRepository = RegionAwareRepository;
	}

// Behavior
	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		Object result = null;
		try {
			String message = StringUtils.EMPTY;
			for (int i = 0; args != null && i < args.length ; i++) {
				if (args[i] instanceof Region) {
					message += "Region " + ((Region) args[i]).getPrettyName() + ": ";
				}
			}
			message += "Operation: " + method.getName();

			LOGGER.trace(message);

			result = method.invoke(RegionAwareRepository, args);
		} catch (final InvocationTargetException invocationTargetException) {
			invocationTargetException.printStackTrace();
		}
		return result;
	}
}
