package protocol;

import model.Card;
import model.CardType;
import model.Dealer;

import javax.websocket.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@javax.websocket.server.ServerEndpoint(value = "/json",
    encoders = {PayloadEncoder.class},
    decoders = {PayloadDecoder.class}
)
public class ServerEndpoint {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }
    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Dealer.reset();
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
    @OnMessage
    public void onMessage(Payload payload, Session session) throws IOException, EncodeException {
        Payload response;
        switch (payload.command) {
            case CARD_CLICKED:
                processCardClicked(session, (String) payload.target);
                break;
            case HORN_TARGET_CLICKED:
                processHornTargetClicked(session, payload.target.toString());
                break;
            case CANCEL:

                break;
            case MESSAGE_SENT:
                dispatchMessage(session, (String) payload.target);
                break;
            case PASS_ROUND:
                break;
            case GIVE_UP:
                break;
            case DEAL_CARDS: // this is a temporary stub; shouldn't be sent from client
                response = new Payload(Command.DEAL_CARDS, Dealer.dealDefaults());
                sendResponse(session, response);
                break;
        }
    }

    private void dispatchMessage(Session session, String message) {}

    private void processHornTargetClicked(Session session, String targetId) throws IOException, EncodeException {
        HornTarget hornTarget = null;
        try {
            hornTarget = HornTarget.valueOf(targetId);
        } catch (IllegalArgumentException e) {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "bad horn target"));
        }

        if (Dealer.isPlayingHorn()) {
            Dealer.playCard(Dealer.getRememberedCard());
            Dealer.clearRememberedCard();
            Dealer.stopPlayingHorn();
            sendResponse(session, new Payload(Command.PLAY_HORN, hornTarget));
            sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_HORN_TARGETS, null));
        } else {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "not playing horn"));
        }
    }

    private void processCardClicked(Session session, String cardId) throws IOException, EncodeException {
        Card card = Dealer.getDealtCard(cardId); // ensure there's such a card
        if (card == null) {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "no such card: " + cardId));
            return;
        }

        Row row = null;
        Payload response = null;

        if (Dealer.isPlayingDecoy()) {
            if (! Dealer.isCardPlayed(card)) {
                cancelPlayingDecoy(session); // clicked on another card in hand - stop playing decoy
            } else if (Dealer.isGoodDecoyTarget(card)) {
                playDecoy(session, card);
                return;
            } else {
                sendResponse(session, new Payload(Command.EVENT_IGNORED, "bad decoy target: " + cardId));
                return;
            }
        } else {
            if (Dealer.isCardPlayed(card)) {
                sendResponse(session, new Payload(Command.EVENT_IGNORED, "card is already played: " + cardId));
                return;
            }
        }
        if (Dealer.isPlayingHorn()) {
            cancelPlayingHorn(session);// clicked on another card in hand - stop playing horn
        }
        if (card.isHealer()) {
            // TODO: implement selection of one of perished cards
            sendResponse(session, new Payload(Command.DEAL_CARDS_SMOOTHLY, Dealer.dealCards(1)));
        }
        CardType type = card.getType();
        switch (type) {
            case CLOSE:
                response = new Payload(Command.PLAY_CARD, getCardRow(card));
                Dealer.playCard(card);
                break;
            case RANGED:
                response = new Payload(Command.PLAY_CARD, getCardRow(card));
                Dealer.playCard(card);
                break;
            case SIEGE:
                response = new Payload(Command.PLAY_CARD, getCardRow(card));
                Dealer.playCard(card);
                break;
            case DECOY:
                // here isPlayingDecoy is always false ('true' case is processed earlier)
                // TODO: also can you replace a decoy with another decoy ???
                Dealer.rememberCard(card);
                Dealer.addHighlightedCards(Dealer.getDecoyTargetsIds());
                response = new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, Dealer.getHighlightedCards());
                Dealer.startPlayingDecoy();
                break;
            case LEADER:
                break;
            case WEATHER:
                response = new Payload(Command.PLAY_WEATHER, null);
                Dealer.playCard(card);
                break;
            case HORN:
                Dealer.rememberCard(card);
                response = new Payload(Command.TOGGLE_HIGHLIGHT_HORN_TARGETS, null);
                Dealer.startPlayingHorn();
                break;
            default:
                logger.log(Level.SEVERE, "unknown card type: " + type);
                response = new Payload(Command.EVENT_IGNORED, "type");
                Dealer.playCard(card);
        }
        sendResponse(session, response);
        if (card.isSpy()) {
            sendResponse(session, new Payload(Command.DEAL_CARDS_SMOOTHLY, Dealer.dealCards(2)));
        }
    }

    private void playDecoy(Session session, Card card) throws IOException, EncodeException {
        Card decoy = Dealer.getRememberedCard();
        Dealer.playCard(decoy);
        Dealer.returnCardToHand(card);
        sendResponse(session, new Payload(Command.SWITCH_CARDS, new SwitchPair(decoy.getId(), card.getId())));
        sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, Dealer.getHighlightedCards())); // remove highlight
        Dealer.clearRememberedCard();
        Dealer.clearHighlightedCards();
        Dealer.stopPlayingDecoy();
    }
     private void sendResponse(Session session, Payload response) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(response);
    }
     private void cancelPlayingHorn(Session session) throws IOException, EncodeException {
        sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_HORN_TARGETS, null));
        Dealer.stopPlayingHorn();
        Dealer.clearRememberedCard();
    }
    private void cancelPlayingDecoy(Session session) throws IOException, EncodeException {
        sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, Dealer.getHighlightedCards())); // remove highlight
        Dealer.clearRememberedCard();
        Dealer.clearHighlightedCards();
        Dealer.stopPlayingDecoy();
    }
    private Row getCardRow(Card c) {
        CardType type = c.getType();
        switch (type) {
            case CLOSE:
                return c.isSpy() ? Row.HIS_CLOSE_COMBAT_ROW : Row.YOUR_CLOSE_COMBAT_ROW;
            case RANGED:
                return c.isSpy() ? Row.HIS_RANGED_COMBAT_ROW: Row.YOUR_RANGED_COMBAT_ROW;
            case SIEGE:
                return c.isSpy() ? Row.HIS_SIEGE_COMBAT_ROW: Row.YOUR_SIEGE_COMBAT_ROW;
            }
        return null; // TODO: consider adding enum for this
    }
}