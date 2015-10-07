package protocol;

/**
 * Created by yuriy on 08.07.2015.
 */
public enum Command {
    // client commands (notifications of relevant client-side events; should be processed on the server side)
    CARD_CLICKED,           // target is one card's id
    HORN_TARGET_CLICKED,    // target is Row
    MESSAGE_SENT,           // target is text message
    PASS_ROUND,             // no target
    GIVE_UP,                // no target

    // server commands
    HIGHLIGHT,
    HIGHLIGHT_ROW,
    TOGGLE_HIGHLIGHT_CARDS,
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

    EVENT_IGNORED, // target is error message
    OK,
    YOUR_MOVE,
    WAIT,
    OPPONENT_GAVE_UP,
    OPPONENT_PASSED_ROUND,
    GAME_STARTED,
    GAME_ENDED,
    WON_ROUND,
    LOST_ROUND,
    WON_GAME,
    LOST_GAME
    // , PING, PONG
}
