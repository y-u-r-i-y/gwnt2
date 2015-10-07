package protocol;

import model.Card;
import model.CardType;
import model.Dealer;

import javax.websocket.*;
import java.io.IOException;
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

    @OnMessage
    public void onMessage(Payload payload, Session session) throws IOException, EncodeException {
        Payload response;
        switch (payload.command) {
            case CARD_CLICKED:
                processCardClicked(session, (String) payload.target);
                break;
            case HORN_TARGET_CLICKED:
                processHornTargetClicked(session, (Row) payload.target);
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
        //////////////////////////////
    }

    private void dispatchMessage(Session session, String message) {}

    private void processHornTargetClicked(Session session, Row onRow) {

    }

    boolean isPlayingDecoy = false;
    List<String> highlightedCards;
    private void processCardClicked(Session session, String cardId) throws IOException, EncodeException {
        Card card = Dealer.getDealtCard(cardId); // ensure there's such a card
        if (card == null) {
            sendResponse(session, new Payload(Command.EVENT_IGNORED, "no such card: " + cardId));
            return;
        }

        Row row = null;
        Payload response = null;

        if (isPlayingDecoy) {
            Card decoy = Dealer.getRememberedCard();

            if (card.isHero()) {
                // TODO: ensure good decoy target, check other cases
                sendResponse(session, new Payload(Command.EVENT_IGNORED, "bad decoy target: " + cardId));
                return;
            }

            Dealer.playCard(decoy);
            Dealer.returnCardToHand(card);
            sendResponse(session, new Payload(Command.SWITCH_CARDS, new SwitchPair(decoy.getId(), card.getId())));
            sendResponse(session, new Payload(Command.TOGGLE_HIGHLIGHT_CARDS, highlightedCards)); // remove highlight
            Dealer.clearRememberedCard();
            highlightedCards = null;
            isPlayingDecoy = false;
            return;
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
                break;
            case HORN:
                response = new Payload(Command.HIGHLIGHT_HORN_TARGETS, null);
                break;
            default:
                logger.log(Level.SEVERE, "unknown card type: " + type);
                response = new Payload(Command.EVENT_IGNORED, "type");
        }
        Dealer.playCard(card);
        sendResponse(session, response);
    }

    private void sendResponse(Session session, Payload response) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(response);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        Dealer.reset();
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}