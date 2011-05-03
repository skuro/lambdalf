package it.sk.alfresco.clojure;

import clojure.lang.IFn;
import clojure.lang.Namespace;
import clojure.lang.Symbol;

/**
 * Used to bootstrap Clojure namespaces as Spring beans
 *
 * @author Carlo Sciolla &lt;c.sciolla@sourcesense.com&gt;
 */
public class NamespaceBootstrap {

	/**
	 * Which namespace to bootstrap
	 */
	private String ns;

	/**
	 * If an IFn with this name is found in {@link this.ns}, it is invoked at init time
	 */
	private String clojureInit;

	/**
	 * Allows for the named namespace to be bootstrapped as a Spring bean
	 *
	 * @return The started up {@link clojure.lang.Namespace}
	 */
	public Object init() {
		Namespace ns = Namespace.findOrCreate(Symbol.create(this.ns));

		if (this.clojureInit != null) {
			Object o = ns.getMapping(Symbol.create(this.ns, this.clojureInit));
			if (o != null && o instanceof IFn) {
				try {
					((IFn) o).invoke();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return ns;
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public void setClojureInit(String clojureInit) {
		this.clojureInit = clojureInit;
	}
}
