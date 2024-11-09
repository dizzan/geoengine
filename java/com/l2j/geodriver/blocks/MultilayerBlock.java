package com.l2j.geodriver.blocks;

import java.nio.*;
import com.l2jserver.gameserver.geoengine.*;
import com.l2j.geodriver.*;

public class MultilayerBlock implements IBlock
{
    private final int _bbPos;
    private final ByteBuffer _bb;
    
    public MultilayerBlock(final ByteBuffer bb) {
        this._bbPos = bb.position();
        this._bb = bb;
        for (int blockCellOffset = 0; blockCellOffset < 64; ++blockCellOffset) {
            final byte numLayers = this._bb.get();
            if (numLayers <= 0 || numLayers > 125) {
                throw new RuntimeException("L2JGeoDriver: Geo file corrupted! Invalid layers count at block cell offset: " + blockCellOffset);
            }

            this._bb.position(this._bb.position() + numLayers * 2);
        }
    }
    
    private short _getNearestLayer(final int geoX, final int geoY, final int worldZ) {
    	int index = this._getCellIndex(geoX, geoY); // Calculul indexului pentru accesarea stratului cel mai apropiat
        final byte numLayers = this._bb.get(index);
        ++index;
        int nearestDZ = 0;
        short nearestData = 0;
        for (int i = 0; i < numLayers; ++i) {
            final short layerData = this._bb.getShort(index);
            index += 2;
            final int layerZ = this._extractLayerHeight(layerData);
            if (layerZ == worldZ) {
                return layerData;
            }
            final int layerDZ = Math.abs(layerZ - worldZ);
            if (i == 0 || layerDZ < nearestDZ) {
                nearestDZ = layerDZ;
                nearestData = layerData;
            }
        }
        return nearestData;
    }
    
    private int _getCellIndex(final int geoX, final int geoY) {
        final int cellOffset = geoX % 8 * 8 + geoY % 8;
        int index = this._bbPos;
        for (int i = 0; i < cellOffset; ++i) {
            // Salt peste `numLayers` în `ByteBuffer` pentru fiecare celulă, fiecare strat ocupă 2 octeți
            index += 1 + this._bb.get(index) * 2;
        }
        return index;
    }
    
    private byte _getNearestNSWE(final int geoX, final int geoY, final int worldZ) {
        return (byte)(this._getNearestLayer(geoX, geoY, worldZ) & 0xF);
    }
    
    private int _extractLayerHeight(short layer) {
        layer &= (short)65520;
        return layer >> 1;
    }
    
    @Override
    public boolean hasGeoPos(final int geoX, final int geoY) {
        return true;
    }
    
    @Override
    public int getNearestZ(final int geoX, final int geoY, final int worldZ) {
        return this._extractLayerHeight(this._getNearestLayer(geoX, geoY, worldZ));
    }
    
    @Override
    public int getNextLowerZ(final int geoX, final int geoY, final int worldZ) {
        int index = this._getCellIndex(geoX, geoY);
        final byte numLayers = this._bb.get(index);
        ++index;
        int lowerZ = Integer.MIN_VALUE; // Folosit pentru a marca că nicio valoare inferioară nu a fost găsită
        for (int i = 0; i < numLayers; ++i) {
            final short layerData = this._bb.getShort(index);
            index += 2;
            final int layerZ = this._extractLayerHeight(layerData);
            if (layerZ == worldZ) {
                return layerZ;
            }
            if (layerZ < worldZ && layerZ > lowerZ) {
                lowerZ = layerZ;
            }
        }
        return (lowerZ == Integer.MIN_VALUE) ? worldZ : lowerZ;
    }
    
    @Override
    public int getNextHigherZ(final int geoX, final int geoY, final int worldZ) {
        int index = this._getCellIndex(geoX, geoY);
        final byte numLayers = this._bb.get(index);
        ++index;
        int higherZ = Integer.MAX_VALUE;
        for (int i = 0; i < numLayers; ++i) {
            final short layerData = this._bb.getShort(index);
            index += 2;
            final int layerZ = this._extractLayerHeight(layerData);
            if (layerZ == worldZ) {
                return layerZ;
            }
            if (layerZ > worldZ && layerZ < higherZ) {
                higherZ = layerZ;
            }
        }
        return (higherZ == Integer.MAX_VALUE) ? worldZ : higherZ;
    }
    
    @Override
    public boolean canMoveIntoDirections(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        if (first == null || more == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }
        return Utils.canMoveIntoDirections(this._getNearestNSWE(geoX, geoY, worldZ), first, more);
    }

    
    @Override
    public boolean canMoveIntoAllDirections(final int geoX, final int geoY, final int worldZ) {
        return this._getNearestNSWE(geoX, geoY, worldZ) == 15;
    }
}
