package com.l2j.geodriver;

import com.l2jserver.gameserver.geoengine.*;

public interface IBlock
{
    public static final int BLOCK_CELLS_X = 8;
    public static final int BLOCK_CELLS_Y = 8;
    public static final int BLOCK_CELLS = 64;
    
    boolean hasGeoPos(final int p0, final int p1);
    
    int getNearestZ(final int p0, final int p1, final int p2);
    
    int getNextLowerZ(final int p0, final int p1, final int p2);
    
    int getNextHigherZ(final int p0, final int p1, final int p2);
    
    boolean canMoveIntoDirections(final int p0, final int p1, final int p2, final Direction p3, final Direction... p4);
    
    boolean canMoveIntoAllDirections(final int p0, final int p1, final int p2);
    
    public enum Type
    {
        FLAT, 
        COMPLEX, 
        MULTILAYER;
    }
}
