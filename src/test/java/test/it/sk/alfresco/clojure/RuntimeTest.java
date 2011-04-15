package test.it.sk.alfresco.clojure;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Carlo Sciolla &lt;c.sciolla@sourcesense.com&gt;
 */
public class RuntimeTest {
	@Before
	public void setup() {
		try {
			clojure.lang.Compiler.load(
					new java.io.StringReader(
							"(ns test.it.sk.alfresco)" +
									"(def *test-var* \"test\")"
					));
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}

	@Test
	public void testRuntimeEnvironment() throws Exception {
		clojure.lang.Compiler.load(
				new java.io.StringReader(
						"(ns test.it.sk.alfresco)" +
								"(if (nil? *test-var*)" +
								"    (. System/out println \"KO\")" +
								"    (. System/out println \"OK\"))"
				));
	}


}
