package it.sk.alfresco.clojure;

/**
 * @author Carlo Sciolla
 */
public class RunSwankServer
{
    public void init() throws Exception
    {
        Runnable cljRunner = new Runnable()
        {
            public void run()
            {
                Thread thisThread = Thread.currentThread();
                ClassLoader savedCL = thisThread.getContextClassLoader();

                ClassLoader newCL = this.getClass().getClassLoader();

                thisThread.setContextClassLoader(newCL);

                try
                {
                    clojure.lang.Compiler.load(
                            new java.io.StringReader(
                                    "(require 'clojure.main)\n" +
                                            "(require 'swank.swank)\n" +
                                            "(clojure.main/with-bindings\n" +
                                            "    (swank.swank/start-server \"nul\" :encoding \"utf-8\" :port 4005))"
                            ));
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                thisThread.setContextClassLoader(savedCL);
            }
        };

        Thread cljThread = new Thread(cljRunner);
        cljThread.start();

    }
}
