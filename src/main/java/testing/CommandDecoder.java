package testing;

import javax.websocket.*;
import java.io.IOException;
import java.io.Reader;

/**
 * Created by yuriy on 08.07.2015.
 */
public class CommandDecoder implements Decoder.TextStream<Command> {

    @Override
    public void init(EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Command decode(Reader reader) throws DecodeException, IOException {
/*        try (JsonReader jsonReader = Json.createReader(reader)) {
            JsonObject jsonObject = jsonReader.readObject();
            Command command = new Command();
            command.type =  jsonObject.get
            sticker.setX(jsonSticker.getInt("x"));
            sticker.setY(jsonSticker.getInt("y"));
            sticker.setImage(jsonSticker.getString("sticker"));
            return sticker;
        }*/
        return null;
    }
}
