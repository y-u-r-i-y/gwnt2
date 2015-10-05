package protocol;

import javax.websocket.*;

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
