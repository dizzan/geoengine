package com.l2j.geodriver.blocks;

import java.nio.*;
import com.l2jserver.gameserver.geoengine.*;
import com.l2j.geodriver.*;

public final class ComplexBlock implements IBlock
{
    private final int _bbPos;
    private final ByteBuffer _bb;
    
    public ComplexBlock(final ByteBuffer bb) {
        this._bbPos = bb.position();
        if (bb.remaining() < 128) {
            throw new IllegalArgumentException("ByteBuffer does not contain enough data for ComplexBlock.");
        }
        (this._bb = bb).position(this._bbPos + 128);
    }

    
    private short _getCellData(final int geoX, final int geoY) {
        // Calculul indexului pentru accesarea datelor unei celule specifice
        int cellIndex = this._bbPos + (geoX % 8 * 8 + geoY % 8) * 2;
        return this._bb.getShort(cellIndex);
    }

    
    private byte _getCellNSWE(final int geoX, final int geoY) {
        return (byte)(this._getCellData(geoX, geoY) & 0xF);
    }
    
    private static final short HEIGHT_MASK = (short) 0xFFF0;

    private int _getCellHeight(final int geoX, final int geoY) {
        final short height = (short)(this._getCellData(geoX, geoY) & HEIGHT_MASK);
        return height >> 1;
    }

    
    @Override
    public boolean hasGeoPos(final int geoX, final int geoY) {
        return true;
    }
    
    @Override
    public int getNearestZ(final int geoX, final int geoY, final int worldZ) {
        return this._getCellHeight(geoX, geoY);
    }
    
    @Override
    public int getNextLowerZ(final int geoX, final int geoY, final int worldZ) {
        final int cellHeight = this._getCellHeight(geoX, geoY);
        // Returnează `cellHeight` dacă este mai mic sau egal cu `worldZ`, altfel returnează `worldZ`
        return (cellHeight <= worldZ) ? cellHeight : worldZ;
    }

    @Override
    public int getNextHigherZ(final int geoX, final int geoY, final int worldZ) {
        final int cellHeight = this._getCellHeight(geoX, geoY);
        // Returnează `cellHeight` dacă este mai mare sau egal cu `worldZ`, altfel returnează `worldZ`
        return (cellHeight >= worldZ) ? cellHeight : worldZ;
    }

    
    @Override
    public boolean canMoveIntoDirections(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        if (first == null || more == null) {
            throw new IllegalArgumentException("Direction cannot be null.");
        }
        return Utils.canMoveIntoDirections(this._getCellNSWE(geoX, geoY), first, more);
    }

    
    @Override
    public boolean canMoveIntoAllDirections(final int geoX, final int geoY, final int worldZ) {
        return this._getCellNSWE(geoX, geoY) == 15;
    }
}
