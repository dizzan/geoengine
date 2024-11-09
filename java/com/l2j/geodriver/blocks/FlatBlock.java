package com.l2j.geodriver.blocks;

import com.l2j.geodriver.*;
import java.nio.*;
import com.l2jserver.gameserver.geoengine.*;

public class FlatBlock implements IBlock
{
    private final short _height;
    
    public FlatBlock(final ByteBuffer bb) {
        if (bb.remaining() < Short.BYTES) {
            throw new IllegalArgumentException("ByteBuffer does not contain enough data for FlatBlock height.");
        }
        this._height = bb.getShort();
    }

    
    @Override
    public boolean hasGeoPos(final int geoX, final int geoY) {
        return true;
    }
    
    @Override
    public int getNearestZ(final int geoX, final int geoY, final int worldZ) {
        return this._height;
    }
    @Override
    public int getNextLowerZ(final int geoX, final int geoY, final int worldZ) {
        // Înălțimea blocului este constantă; întoarce `_height` dacă este mai mică sau egală cu `worldZ`
        return (this._height <= worldZ) ? this._height : worldZ;
    }

    
    @Override
    public int getNextHigherZ(final int geoX, final int geoY, final int worldZ) {
        return (this._height >= worldZ) ? this._height : worldZ;
    }
    
    @Override
    public boolean canMoveIntoDirections(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        if (first == null || more == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }
        return true;
    }

    
    @Override
    public boolean canMoveIntoAllDirections(final int geoX, final int geoY, final int worldZ) {
        return true;
    }
}
