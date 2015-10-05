package protocol;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by yuriy on 08.07.2015.
 */
public class CommandEncoder implements Encoder.Text<Command> {

    @Override
    public String encode(Command command) throws EncodeException {
        return command.toString();
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

}
