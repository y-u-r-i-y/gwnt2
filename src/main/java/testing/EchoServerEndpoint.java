package testing;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

@ServerEndpoint(value = "/echo")
public class EchoServerEndpoint {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.info("Connected ... " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        switch (message) {
            case "quit":
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Game ended"));
                    return;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // break;
        }
        logger.info("Received:" + message);
        session.getBasicRemote().sendText("Echo: " + message);
    }

    @OnError
    public void onError(Throwable t) {
        logger.info("error");
        t.printStackTrace();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));
    }
}