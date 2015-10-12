package protocol;

/**
 * Created by yuriy on 08.07.2015.
 */
public enum HornTarget {
    // should be equal to ids of corresponding divs in the mark-up
    // need this in a separate enum to narrow down set of Rows on which a horn can be played
    CLOSE_COMBAT_HORN,
    RANGED_COMBAT_HORN,
    SIEGE_COMBAT_HORN
}
