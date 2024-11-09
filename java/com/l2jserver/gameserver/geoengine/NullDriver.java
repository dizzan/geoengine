package com.l2jserver.gameserver.geoengine;

import com.l2jserver.gameserver.geoengine.abstraction.*;
import java.util.logging.*;
import java.util.*;

public final class NullDriver implements IGeoDriver
{
    private static final Logger _LOGGER;
    
    public NullDriver(final Properties props) {
        NullDriver._LOGGER.info("Using Null GeoDriver.");
    }
    
    @Override
    public int getGeoX(final int worldX) {
        return worldX;
    }
    
    @Override
    public int getGeoY(final int worldY) {
        return worldY;
    }
    
    @Override
    public int getWorldX(final int geoX) {
        return geoX;
    }
    
    @Override
    public int getWorldY(final int geoY) {
        return geoY;
    }
    
    @Override
    public boolean hasGeoPos(final int geoX, final int geoY) {
        return false;
    }
    
    @Override
    public int getNearestZ(final int geoX, final int geoY, final int worldZ) {
        return worldZ;
    }
    
    @Override
    public int getNextLowerZ(final int geoX, final int geoY, final int worldZ) {
        return worldZ;
    }
    
    @Override
    public int getNextHigherZ(final int geoX, final int geoY, final int worldZ) {
        return worldZ;
    }
    
    @Override
    public boolean canEnterNeighbors(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        return true;
    }
    
    @Override
    public boolean canEnterAllNeighbors(final int geoX, final int geoY, final int worldZ) {
        return true;
    }
    
    static {
        _LOGGER = Logger.getLogger(NullDriver.class.getName());
    }
}
