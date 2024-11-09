package com.l2j.geodriver.regions;

import com.l2j.geodriver.*;
import java.nio.*;
import com.l2j.geodriver.blocks.*;
import com.l2jserver.gameserver.geoengine.*;

public final class NonNullRegion implements IRegion
{
    private final IBlock[] _blocks;
    
    public NonNullRegion(final ByteBuffer bb) {
        this._blocks = new IBlock[65536];
        for (int regionBlockOffset = 0; regionBlockOffset < 65536; ++regionBlockOffset) {
            final int blockType = bb.get();
            if (blockType == IBlock.Type.FLAT.ordinal()) {
                this._blocks[regionBlockOffset] = new FlatBlock(bb);
            }
            else if (blockType == IBlock.Type.COMPLEX.ordinal()) {
                this._blocks[regionBlockOffset] = new ComplexBlock(bb);
            }
            else {
            	if (blockType != IBlock.Type.MULTILAYER.ordinal()) {
            	    throw new RuntimeException("L2JGeoDriver: Invalid block type encountered! Block type: " + blockType);
            	}

                this._blocks[regionBlockOffset] = new MultilayerBlock(bb);
            }
        }
    }
    
    private IBlock _getBlock(final int geoX, final int geoY) {
        // Calculate the block index based on subdivided regions
        int index = (geoX / 8 % 256) * 256 + (geoY / 8 % 256);
        return this._blocks[index];
    }
    
    @Override
    public boolean hasGeoPos(final int geoX, final int geoY) {
        return this._getBlock(geoX, geoY).hasGeoPos(geoX, geoY);
    }
    
    @Override
    public int getNearestZ(final int geoX, final int geoY, final int worldZ) {
        return this._getBlock(geoX, geoY).getNearestZ(geoX, geoY, worldZ);
    }
    
    @Override
    public int getNextLowerZ(final int geoX, final int geoY, final int worldZ) {
        return this._getBlock(geoX, geoY).getNextLowerZ(geoX, geoY, worldZ);
    }
    
    @Override
    public int getNextHigherZ(final int geoX, final int geoY, final int worldZ) {
        return this._getBlock(geoX, geoY).getNextHigherZ(geoX, geoY, worldZ);
    }
    
    @Override
    public boolean canMoveIntoDirections(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        if (first == null || more == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }
        return this._getBlock(geoX, geoY).canMoveIntoDirections(geoX, geoY, worldZ, first, more);
    }

    
    @Override
    public boolean canMoveIntoAllDirections(final int geoX, final int geoY, final int worldZ) {
        return this._getBlock(geoX, geoY).canMoveIntoAllDirections(geoX, geoY, worldZ);
    }
}
