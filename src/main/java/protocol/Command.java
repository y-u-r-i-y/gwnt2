package protocol;

/**
 * Created by yuriy on 08.07.2015.
 */
public enum Command {
    OK,
    YOUR_MOVE,
    WAIT,
    HIGHLIGHT,
    EXIT,
    SELECT_CARD,
    PLAY_CARD,
    GAME_STARTED,
    GAME_ENDED,
    UPDATE_ROW_SCORE,
    UPDATE_SCORE,
    WON_ROUND,
    LOST_ROUND,
    WON_GAME,
    LOST_GAME,
    DEAL_CARDS,
    DISCARD_CARDS,
    RESTORE_CARD
    // PING, PONG
}
