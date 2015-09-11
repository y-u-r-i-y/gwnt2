package testing;

import javax.websocket.*;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by yuriy on 08.07.2015.
 */
public class CommandDecoder implements Decoder.Text<Command> {

    @Override
    public Command decode(String s) throws DecodeException {
        return Command.valueOf(s);
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

}
