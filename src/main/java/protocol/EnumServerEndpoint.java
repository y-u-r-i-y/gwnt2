package protocol;

import model.Card;
import model.CardType;
import model.Dealer;
import model.DeckType;
import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint(value = "/json",
    encoders = {CommandEncoder.class},
    decoders = {CommandDecoder.class}
)
public class EnumServerEndpoint {
    ObjectMapper mapper = new ObjectMapper();
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }

    @OnMessage
    public void onMessage(Command command, Session session) throws IOException {
        switch (command) {
            case DEAL_CARDS:

                Response response = new Response(Command.DEAL_CARDS,
                        Dealer.dealDefaults());

                session.getBasicRemote().sendText(mapper.writeValueAsString(response));
                break;
            case EXIT:
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game ended"));
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

        }
        logger.info("Received:" + command);
        // session.getBasicRemote().sendText("Echo: " + command);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}