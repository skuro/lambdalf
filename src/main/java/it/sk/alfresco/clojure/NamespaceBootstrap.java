package it.sk.alfresco.clojure;

import clojure.lang.IFn;
import clojure.lang.Namespace;
import clojure.lang.RT;
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
	 * If this bean was initialized
	 */
	private boolean inited = false;

	/**
	 * Allows for the named namespace to be bootstrapped as a Spring bean
	 *
	 * @return The started up {@link clojure.lang.Namespace}
	 */
	public void init() {
		Namespace ns = null;
		try {
			RT.loadResourceScript(String.format("%s.clj", this.ns));
			if (this.clojureInit != null) {
				ns = Namespace.findOrCreate(Symbol.create(this.ns));
				Object o = ns.getMapping(Symbol.create(this.ns, this.clojureInit));
				if (o != null && o instanceof IFn) {
					((IFn) o).invoke();
					this.inited = true;

				}
			}
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	public void setNs(String ns) {
		this.ns = ns;
	}

	public void setClojureInit(String clojureInit) {
		this.clojureInit = clojureInit;
	}

	public boolean isInited() {
		return inited;
	}
}
