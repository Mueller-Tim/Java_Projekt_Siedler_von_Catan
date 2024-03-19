package ch.zhaw.structures;

import java.awt.Point;

import ch.zhaw.catan.Config;
import ch.zhaw.catan.Player;

/**
 * The Road class is a data class which represents a single road piece
 */
public class Road extends Structure {

	private final Point start;
	private final Point end;

	/**
	 * Constructor which assigns a road to a player and calls the constructor of the
	 * super class
	 *
	 * @param owner
	 */
	public Road(Player owner, Point start, Point end) {
		super(owner, Config.Structure.ROAD, Config.ROAD_COST);
		this.start = start;
		this.end = end;
	}

	/**
	 * Returns the other end of the road as the given position
	 * @param position
	 * @return Point other road point
	 */
	public Point getOtherEnd(Point position) {
		if (position.equals(start)) {
			return end;
		} else if (position.equals(end)) {
			return start;
		}
		return null;
	}

	/**
	 * @return first character of the owner-faction
	 */
	@Override
	public String toString() {
		return String.valueOf(getOwner().getFaction().toString().charAt(0));
	}

	public Point getStart() {
		return start;
	}

	public Point getEnd() {
		return end;
	}

}
