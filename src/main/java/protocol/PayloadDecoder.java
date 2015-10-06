package protocol;

import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by yuriy on 08.07.2015.
 */
public class PayloadDecoder implements Decoder.Text<Payload> {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public Payload decode(String s) throws DecodeException {
        Payload payload = null;
        try {
            payload = mapper.readValue(s, Payload.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payload;
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
