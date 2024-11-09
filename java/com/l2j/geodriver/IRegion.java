package com.l2j.geodriver;

import com.l2jserver.gameserver.geoengine.*;

public interface IRegion
{
    public static final int REGION_BLOCKS_X = 256;
    public static final int REGION_BLOCKS_Y = 256;
    public static final int REGION_BLOCKS = 65536;
    public static final int REGION_CELLS_X = 2048;
    public static final int REGION_CELLS_Y = 2048;
    public static final int REGION_CELLS = 4194304;
    
    boolean hasGeoPos(final int p0, final int p1);
    
    int getNearestZ(final int p0, final int p1, final int p2);
    
    int getNextLowerZ(final int p0, final int p1, final int p2);
    
    int getNextHigherZ(final int p0, final int p1, final int p2);
    
    boolean canMoveIntoDirections(final int p0, final int p1, final int p2, final Direction p3, final Direction... p4);
    
    boolean canMoveIntoAllDirections(final int p0, final int p1, final int p2);
    
    public enum Type
    {
        NULL, 
        NON_NULL;
    }
}
