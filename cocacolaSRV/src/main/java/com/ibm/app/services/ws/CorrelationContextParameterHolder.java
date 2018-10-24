package com.ibm.app.services.ws;

public final class CorrelationContextParameterHolder {
	private static ThreadLocal<String> correlationContextParameter = new ThreadLocal<String>();

    private CorrelationContextParameterHolder() {
        // It's a utility class, no instances needed.
    }

    /**
     * Sets the <em>correlation context</em> for this thread.
     */
    public static void set(String value) {
        correlationContextParameter.set(value);
    }

    /**
     * 
     * @return <em>Correlation context</em> assigned to this thread.
     */
    public static String get() {
        return correlationContextParameter.get();
    }

    /**
     * Removes any <em>correlation context</em> from this thread.
     */
    public static void remove() {
        correlationContextParameter.remove();
    }
}
