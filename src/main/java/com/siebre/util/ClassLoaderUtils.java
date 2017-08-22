/*
 * Created on 2004-2-19
 *
 */
package com.siebre.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class ClassLoaderUtils {
    protected static final Logger log = LogManager.getLogger(ClassLoaderUtils.class);

	public static ClassLoader getThreadContextLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	public static ClassLoader getCurrentLoader() {
		return ClassLoaderUtils.class.getClassLoader();
	}

	public static URL getResource(String resource) {
		ClassLoader classLoader = null;
		URL url = null;

		try {
			classLoader = getThreadContextLoader();
			if (classLoader != null) {
				url = classLoader.getResource(resource);
			}

			if (url == null) {
				classLoader = getCurrentLoader();
				if (classLoader != null) {
					url = classLoader.getResource(resource);
				}
			}
		} catch (Throwable t) {
		}
		
		if (url == null)
			url = ClassLoader.getSystemResource(resource);

		log.debug("Resource " + resource + " was found as " + url.getPath());

		return url;
	}

	public static InputStream getResourceAsStream(String resource) {
		URL url = getResource(resource);
		try {
			return url != null ? url.openStream() : null;
		} catch (IOException e) {
			return null;
		}
	}
}
