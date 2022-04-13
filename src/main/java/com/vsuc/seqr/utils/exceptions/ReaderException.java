package com.vsuc.seqr.utils.exceptions;

public class ReaderException extends Exception{

    // disable stack traces when not running inside test units
    protected static boolean isStackTrace =
            System.getProperty("surefire.test.class.path") != null;
    protected static final StackTraceElement[] NO_TRACE = new StackTraceElement[0];

    ReaderException() {
        // do nothing
    }

    ReaderException(Throwable cause) {
        super(cause);
    }

    // Prevent stack traces from being taken
    @Override
    public final synchronized Throwable fillInStackTrace() {
        return null;
    }

    /**
     * For testing only. Controls whether library exception classes include stack traces or not.
     * Defaults to false, unless running in the project's unit testing harness.
     *
     * @param enabled if true, enables stack traces in library exception classes
     * @since 3.5.0
     */
    public static void setStackTrace(boolean enabled) {
        isStackTrace = enabled;
    }

}
