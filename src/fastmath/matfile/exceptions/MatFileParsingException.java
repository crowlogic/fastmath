/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile.exceptions;

import fastmath.matfile.exceptions.MatFileException;

public class MatFileParsingException
extends MatFileException {
    private static final long serialVersionUID = 1L;

    public MatFileParsingException(Exception e, String msg) {
        super(e, msg);
    }

    public MatFileParsingException(Exception e) {
        super(e);
    }

    public MatFileParsingException(String string) {
        super(string);
    }
}

