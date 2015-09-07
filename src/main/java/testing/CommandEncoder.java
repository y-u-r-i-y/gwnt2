package testing;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by yuriy on 08.07.2015.
 */
public class CommandEncoder implements Encoder.TextStream<Command> {
    @Override
    public void encode(Command object, Writer writer) throws EncodeException, IOException {

    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
