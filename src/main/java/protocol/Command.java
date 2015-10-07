package protocol;

/**
 * Created by yuriy on 08.07.2015.
 */
public enum Command {
    OK,
    YOUR_MOVE,
    WAIT,
    EXIT,

    HIGHLIGHT,
    HIGHLIGHT_ROW,
    HIGHLIGHT_CARDS,
    HIGHLIGHT_HORN_TARGETS,

    PLAY_CARD,
    SELECT_CARD,
    SWITCH_CARDS,
    WEAKEN_CARDS,
    DEAL_CARDS,
    DISCARD_CARDS,
    RESTORE_CARD,

    UPDATE_ROW_SCORE,
    UPDATE_SCORE,

    GAME_STARTED,
    GAME_ENDED,
    WON_ROUND,
    LOST_ROUND,
    WON_GAME,
    LOST_GAME
    // , PING, PONG, MSG
}
