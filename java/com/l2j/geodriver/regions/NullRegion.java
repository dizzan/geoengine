package com.l2j.geodriver.regions;

import com.l2j.geodriver.*;
import com.l2jserver.gameserver.geoengine.*;

public final class NullRegion implements IRegion
{
    public static final NullRegion INSTANCE;
    
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
    public boolean canMoveIntoDirections(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        return true;
    }
    
    @Override
    public boolean canMoveIntoAllDirections(final int geoX, final int geoY, final int worldZ) {
        return true;
    }
    
    static {
        INSTANCE = new NullRegion();
    }
}
