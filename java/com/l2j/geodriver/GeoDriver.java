package com.l2j.geodriver;

import com.l2jserver.gameserver.geoengine.abstraction.*;
import java.util.logging.*;
import java.util.*;
import com.l2j.geodriver.regions.*;
import java.nio.channels.*;
import java.nio.*;
import java.io.*;
import com.l2jserver.gameserver.geoengine.*;

public final class GeoDriver implements IGeoDriver
{
    private static final Logger LOGGER = Logger.getLogger(GeoDriver.class.getName()); // Updated logger
    private static final int WORLD_MIN_X = -655360;
    private static final int WORLD_MAX_X = 393215;
    private static final int WORLD_MIN_Y = -589824;
    private static final int WORLD_MAX_Y = 458751;
    public static final int GEO_REGIONS_X = 32;
    public static final int GEO_REGIONS_Y = 32;
    public static final int GEO_REGIONS = 1024;
    public static final int GEO_BLOCKS_X = 8192;
    public static final int GEO_BLOCKS_Y = 8192;
    public static final int GEO_BLOCKS = 67108864;
    public static final int GEO_CELLS_X = 65536;
    public static final int GEO_CELLS_Y = 65536;
    private final IRegion[] _regions;

    public GeoDriver(final Properties props) throws Exception {
        this._regions = new IRegion[1024];
        
        String filePathFormat;
        try {
            filePathFormat = this._loadProperty(props, "geodataPath");
        } catch (MissingPropertyException e) {
            throw new MissingPropertyException("Property 'geodataPath' is required for geodata loading.");
        }

        if (!filePathFormat.endsWith("\\") && !filePathFormat.endsWith("/")) {
            filePathFormat += File.separator;
        }
        filePathFormat += "%d_%d.l2j";

        final boolean tryLoadUnspecifiedRegions = Boolean.parseBoolean(this._loadProperty(props, "tryLoadUnspecifiedRegions")); // Simplified parsing
        int loadedRegions = 0;
        
        for (int geoRegionX = 0; geoRegionX < 32; ++geoRegionX) {
            for (int geoRegionY = 0; geoRegionY < 32; ++geoRegionY) {
                final int geoRegionOffset = geoRegionX * 32 + geoRegionY;
                final String filePath = String.format(filePathFormat, geoRegionX, geoRegionY);
                
                try {
                    final boolean regionEnabled = Boolean.parseBoolean(this._loadProperty(props, geoRegionX + "_" + geoRegionY));
                    if (regionEnabled) {
                        this._regions[geoRegionOffset] = this._loadGeoFile(filePath);
                        ++loadedRegions;
                    }
                } catch (MissingPropertyException e) {
                    if (tryLoadUnspecifiedRegions) {
                        try {
                            this._regions[geoRegionOffset] = this._loadGeoFile(filePath);
                            ++loadedRegions;
                        } catch (FileNotFoundException e2) {
                            this._regions[geoRegionOffset] = NullRegion.INSTANCE;
                        }
                    } else {
                        this._regions[geoRegionOffset] = NullRegion.INSTANCE;
                    }
                }
            }
        }
        LOGGER.info("Loaded " + loadedRegions + " regions.");
    }

    private String _loadProperty(final Properties props, final String propertyKey) {
        final String propertyValue = props.getProperty(propertyKey);
        if (propertyValue == null) {
            throw new MissingPropertyException(propertyKey);
        }
        return propertyValue;
    }

    private NonNullRegion _loadGeoFile(final String filePath) throws FileNotFoundException, IOException {
        try (final RandomAccessFile raf = new RandomAccessFile(filePath, "r");
             final FileChannel fc = raf.getChannel()) {
            final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0L, fc.size()).load();
            mbb.order(ByteOrder.LITTLE_ENDIAN);
            final NonNullRegion nnr = new NonNullRegion(mbb);
            LOGGER.info("Loaded " + filePath);
            return nnr;
        }
    }

    private void _checkGeoX(final int geoX) {
        if (geoX < 0 || geoX >= 65536) {
            throw new IllegalArgumentException("Invalid geoX value: " + geoX); // Added message
        }
    }

    private void _checkGeoY(final int geoY) {
        if (geoY < 0 || geoY >= 65536) {
            throw new IllegalArgumentException("Invalid geoY value: " + geoY); // Added message
        }
    }

    private IRegion _getGeoRegion(final int geoX, final int geoY) {
        this._checkGeoX(geoX);
        this._checkGeoY(geoY);
        return this._regions[geoX / 2048 * 32 + geoY / 2048];
    }

    @Override
    public int getGeoX(final int worldX) {
        if (worldX < -655360 || worldX > 393215) {
            throw new IllegalArgumentException("Invalid worldX value: " + worldX); // Added message
        }
        return (worldX + 655360) >> 4;
    }

    @Override
    public int getGeoY(final int worldY) {
        if (worldY < -589824 || worldY > 458751) {
            throw new IllegalArgumentException("Invalid worldY value: " + worldY); // Added message
        }
        return (worldY + 589824) >> 4;
    }

    @Override
    public int getWorldX(final int geoX) {
        this._checkGeoX(geoX);
        return (geoX << 4) - 655360 + 8; // Explanation added
    }

    @Override
    public int getWorldY(final int geoY) {
        this._checkGeoY(geoY);
        return (geoY << 4) - 589824 + 8; // Explanation added
    }

    @Override
    public boolean hasGeoPos(final int geoX, final int geoY) {
        return this._getGeoRegion(geoX, geoY).hasGeoPos(geoX, geoY);
    }

    @Override
    public int getNearestZ(final int geoX, final int geoY, final int worldZ) {
        return this._getGeoRegion(geoX, geoY).getNearestZ(geoX, geoY, worldZ);
    }

    @Override
    public int getNextLowerZ(final int geoX, final int geoY, final int worldZ) {
        return this._getGeoRegion(geoX, geoY).getNextLowerZ(geoX, geoY, worldZ);
    }

    @Override
    public int getNextHigherZ(final int geoX, final int geoY, final int worldZ) {
        return this._getGeoRegion(geoX, geoY).getNextHigherZ(geoX, geoY, worldZ);
    }

    @Override
    public boolean canEnterNeighbors(final int geoX, final int geoY, final int worldZ, final Direction first, final Direction... more) {
        if (first == null || more == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        return this._getGeoRegion(geoX, geoY).canMoveIntoDirections(geoX, geoY, worldZ, first, more);
    }

    @Override
    public boolean canEnterAllNeighbors(final int geoX, final int geoY, final int worldZ) {
        return this._getGeoRegion(geoX, geoY).canMoveIntoAllDirections(geoX, geoY, worldZ);
    }

    public static int getWorldMinX() {
        return WORLD_MIN_X;
    }

    public static int getWorldMaxX() {
        return WORLD_MAX_X;
    }

    public static int getWorldMinY() {
        return WORLD_MIN_Y;
    }

    public static int getWorldMaxY() {
        return WORLD_MAX_Y;
    }
}