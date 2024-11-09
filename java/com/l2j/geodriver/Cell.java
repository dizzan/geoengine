package com.l2j.geodriver;

public final class Cell
{
    public static final byte FLAG_NSWE_EAST = 1;
    public static final byte FLAG_NSWE_WEST = 2;
    public static final byte FLAG_NSWE_SOUTH = 4;
    public static final byte FLAG_NSWE_NORTH = 8;
    public static final byte FLAG_NSWE_NORTH_EAST = 9;
    public static final byte FLAG_NSWE_NORTH_WEST = 10;
    public static final byte FLAG_NSWE_SOUTH_EAST = 5;
    public static final byte FLAG_NSWE_SOUTH_WEST = 6;
    public static final byte FLAG_NSWE_ALL = 15;
    
    private Cell() {
    }
}
