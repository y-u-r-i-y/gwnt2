package protocol;

import model.Card;
import model.CardType;
import model.Dealer;

import javax.websocket.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    private boolean isPlayingHorn = false;
    private void processHornTargetClicked(Session session, String targetId) throws IOException, EncodeException {
        HornTarget hornTarget = null;
        try {
            hornTarget = HornTarget.valueOf(targetId);
        } catch (IllegalArgumentException e) {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "bad horn target"));
        }

        if (isPlayingHorn) {
            sendResponse(session, new Payload(Command.PLAY_HORN, hornTarget));
            sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_HORN_TARGETS, null));
            Dealer.playCard(Dealer.getRememberedCard());
            Dealer.clearRememberedCard();
        } else {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "not playing horn"));
        }
    }

    private boolean isPlayingDecoy = false;
    private List<String> highlightedCards = new ArrayList<>();
    private void processCardClicked(Session session, String cardId) throws IOException, EncodeException {
        Card card = Dealer.getDealtCard(cardId); // ensure there's such a card
        if (card == null) {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "no such card: " + cardId));
            return;
        }
        Row row = null;
        Payload response = null;

        if (isPlayingDecoy) {
            if (Dealer.getPlayedCard(cardId) == null) {
                cancelPlayingDecoy(session); // clicked on another card in hand - stop playing decoy
            } else if (isGoodDecoyTarget(card)) {
                playDecoy(session, card);
                return;
            } else {
                sendResponse(session, new Payload(Command.EVENT_IGNORED, "bad decoy target: " + cardId));
            }
        }
        if (isPlayingHorn) {
            cancelPlayingHorn(session);
        }
        CardType type = card.getType();
        switch (type) {
            case CLOSE:
                row = Row.YOUR_CLOSE_COMBAT_ROW;
                response = new Payload(Command.PLAY_CARD, row); // TODO: check if the card is a SPY
                break;
            case RANGED:
                row = Row.YOUR_RANGED_COMBAT_ROW;
                response = new Payload(Command.PLAY_CARD, row);
                break;
            case SIEGE:
                row = Row.YOUR_SIEGE_COMBAT_ROW;
                response = new Payload(Command.PLAY_CARD, row);
                break;
            case DECOY:
                // here isPlayingDecoy is always false ('true' case is processed earlier)
                // TODO: also can you replace a decoy with another decoy ???
                Dealer.rememberCard(card);
                highlightedCards = Dealer.getDecoyTargetsIds();
                response = new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, highlightedCards);
                isPlayingDecoy = true;
                break;
            case LEADER:
                break;
            case WEATHER:
                response = new Payload(Command.PLAY_WEATHER, null);
                break;
            case HORN:
                Dealer.rememberCard(card);
                response = new Payload(Command.TOGGLE_HIGHLIGHT_HORN_TARGETS, null);
                isPlayingHorn = true;
                break;
            default:
                logger.log(Level.SEVERE, "unknown card type: " + type);
                response = new Payload(Command.EVENT_IGNORED, "type");
        }
        Dealer.playCard(card);
        sendResponse(session, response);
    }

    private void playDecoy(Session session, Card card) throws IOException, EncodeException {
        Card decoy = Dealer.getRememberedCard();
        Dealer.playCard(decoy);
        Dealer.returnCardToHand(card);
        sendResponse(session, new Payload(Command.SWITCH_CARDS, new SwitchPair(decoy.getId(), card.getId())));
        sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, highlightedCards)); // remove highlight
        Dealer.clearRememberedCard();
        highlightedCards.clear();
        isPlayingDecoy = false;
    }
     private void sendResponse(Session session, Payload response) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(response);
    }
     private void cancelPlayingHorn(Session session) throws IOException, EncodeException {
        sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_HORN_TARGETS, null));
        isPlayingHorn = false;
        Dealer.clearRememberedCard();
    }
    private void cancelPlayingDecoy(Session session) throws IOException, EncodeException {
        sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, highlightedCards)); // remove highlight
        highlightedCards.clear();
        isPlayingDecoy = false;
        Dealer.clearRememberedCard();
    }
    private boolean isGoodDecoyTarget(Card card)  {
        return ! (card.isHero() || card.getType().equals(CardType.WEATHER)) && Dealer.getPlayedCard(card.getId()) != null;
    }
}