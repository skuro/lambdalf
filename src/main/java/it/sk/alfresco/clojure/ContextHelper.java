package it.sk.alfresco.clojure;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Glue code between Spring beans and Clojure code
 *
 * @author Carlo Sciolla &lt;c.sciolla@sourcesense.com&gt;
 */
public class ContextHelper implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ContextHelper.applicationContext = applicationContext;
	}

	/**
	 * Used in clojure code to fetch Spring defined beans
	 *
	 * @return The {@link ApplicationContext}
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
