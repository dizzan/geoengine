/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <www.l2darkrise.com/>.
 */
package l2e.geoserver.geodata;

/**
 * Created by GenuinelyInsane 10.09.2013 Based on L2J-DarkRise
 */
public class Layer {
	public short height;
	public byte nswe;

	public Layer(short h, byte n) {
		height = h;
		nswe = n;
	}
}