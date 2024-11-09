package com.l2j.geodriver;

import com.l2jserver.gameserver.geoengine.*;

public final class Utils
{
    private static boolean _nsweContains(final byte nswe, final byte nsweFlags) {
        return (nswe & nsweFlags) == nsweFlags;
    }
    
    private static byte _getNsweFlagsFromDirection(final Direction dir) {
        switch (dir) {
            case NORTH_WEST: {
                return 10;
            }
            case NORTH_EAST: {
                return 9;
            }
            case SOUTH_WEST: {
                return 6;
            }
            case SOUTH_EAST: {
                return 5;
            }
            case NORTH: {
                return 8;
            }
            case EAST: {
                return 1;
            }
            case SOUTH: {
                return 4;
            }
            case WEST: {
                return 2;
            }
            default: {
                throw new IllegalStateException("This can't happen we have exacly the number of fields in the enum!");
            }
        }
    }
    
    public static boolean canMoveIntoDirections(final byte nswe, final Direction first, final Direction... more) {
        if (!_nsweContains(nswe, _getNsweFlagsFromDirection(first))) {
            return false;
        }
        if (more != null) {
            for (final Direction dir : more) {
                if (!_nsweContains(nswe, _getNsweFlagsFromDirection(dir))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private Utils() {
    }
}
