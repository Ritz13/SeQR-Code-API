package com.vsuc.seqr.utils.exceptions;

public final class ChecksumException extends ReaderException {

    private static final ChecksumException INSTANCE = new ChecksumException();
    static {
        INSTANCE.setStackTrace(NO_TRACE); // since it's meaningless
    }

    private ChecksumException() {
        // do nothing
    }

    private ChecksumException(Throwable cause) {
        super(cause);
    }

    public static ChecksumException getChecksumInstance() {
        return isStackTrace ? new ChecksumException() : INSTANCE;
    }

    public static ChecksumException getChecksumInstance(Throwable cause) {
        return isStackTrace ? new ChecksumException(cause) : INSTANCE;
    }

}
