package protocol;

import org.codehaus.jackson.map.ObjectMapper;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

/**
 * Created by yuriy on 08.07.2015.
 */
public class PayloadEncoder implements Encoder.Text<Payload> {
    ObjectMapper mapper = new ObjectMapper();
    @Override
    public String encode(Payload payload) throws EncodeException {
        try {
            return mapper.writeValueAsString(payload);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }

}
