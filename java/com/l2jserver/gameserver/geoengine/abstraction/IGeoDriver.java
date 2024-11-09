package com.l2jserver.gameserver.geoengine.abstraction;

import com.l2jserver.gameserver.geoengine.*;

public interface IGeoDriver
{
    int getGeoX(final int p0);
    
    int getGeoY(final int p0);
    
    int getWorldX(final int p0);
    
    int getWorldY(final int p0);
    
    boolean hasGeoPos(final int p0, final int p1);
    
    int getNearestZ(final int p0, final int p1, final int p2);
    
    int getNextLowerZ(final int p0, final int p1, final int p2);
    
    int getNextHigherZ(final int p0, final int p1, final int p2);
    
    boolean canEnterNeighbors(final int p0, final int p1, final int p2, final Direction p3, final Direction... p4);
    
    boolean canEnterAllNeighbors(final int p0, final int p1, final int p2);
}
