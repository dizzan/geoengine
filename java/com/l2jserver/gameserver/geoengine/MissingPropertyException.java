package com.l2jserver.gameserver.geoengine;

public final class MissingPropertyException extends RuntimeException
{
    private static final long serialVersionUID = 4917095835827941448L;
    
    public MissingPropertyException(final String key) {
        super(key);
    }
}
