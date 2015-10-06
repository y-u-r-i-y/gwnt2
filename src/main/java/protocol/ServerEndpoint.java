package protocol;

import model.Card;
import model.CardType;
import model.Dealer;
import model.DeckType;

import javax.websocket.*;
import java.io.IOException;
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
            case DEAL_CARDS:
                response = new Payload(Command.DEAL_CARDS, Dealer.dealDefaults());

                sendResponse(session, response);
                break;
            case HIGHLIGHT:
                response = new Payload(Command.HIGHLIGHT, payload.target);

                sendResponse(session, response);
                break;
            case PLAY_CARD:
                Card dealtCard = Dealer.getDealtCard(payload.target.toString());
                String cardsId = dealtCard.getId();
                CardType type = dealtCard.getType();
                Row row = Row.CLOSE_COMBAT_ROW;
                switch (type) {
                    case CLOSE:
                        row = Row.CLOSE_COMBAT_ROW;
                        break;
                    case RANGED:
                        row = Row.RANGED_ROW;
                        break;
                    case SIEGE:
                        row = Row.SIEGE_ROW;
                        break;
                    case DECOY:
                        break;
                    case LEADER:
                        break;
                    case WEATHER:
                        break;
                    case HORN:
                        break;
                }

                // TODO: do PLAY and if OK, get card's strength and row
                response = new Payload(Command.SELECT_CARD, payload.target); //card id
                sendResponse(session, response);

                response = new Payload(Command.PLAY_CARD, row);// send card's row
                sendResponse(session, response);

                response = new Payload(Command.UPDATE_ROW_SCORE, 42); // update card's row
                sendResponse(session, response);
                break;



            case EXIT:
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game ended"));
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        }
        // logger.info("Received:" + payload.command);
    }

    private void sendResponse(Session session, Payload response) throws IOException, EncodeException {
        session.getBasicRemote().sendObject(response);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}