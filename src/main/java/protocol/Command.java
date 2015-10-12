package protocol;

/**
 * Created by yuriy on 08.07.2015.
 */
public enum Command {
    // client commands (notifications of relevant client-side events; should be processed on the server side)
    CARD_CLICKED,           // target is card's id
    HORN_TARGET_CLICKED,    // target is Row
    MESSAGE_SENT,           // target is text message
    PASS_ROUND,             // no target
    GIVE_UP,                // no target
    CANCEL,                 // no target

    // server commands
    HIGHLIGHT,
    HIGHLIGHT_ROW,
    TOGGLE_HIGHLIGHT_CARDS,
    TOGGLE_HIGHLIGHT_BOND,
    TOGGLE_HIGHLIGHT_HORN_TARGETS,

    PLAY_CARD,
    PLAY_WEATHER,
    PLAY_HORN,
    SWITCH_CARDS,
    WEAKEN_CARDS,
    DEAL_CARDS,
    DEAL_CARDS_SMOOTHLY, // used to deal new card(s) in case if a spy or healer is played

    DISCARD_CARDS,
    RESTORE_CARD,

    UPDATE_ROW_SCORE,
    UPDATE_GAME_STATE,

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
