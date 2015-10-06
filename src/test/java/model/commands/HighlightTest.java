package model.commands;

import protocol.Command;
import protocol.HighlightTarget;
import protocol.Payload;
import model.json.BaseJsonMappingTest;
import org.junit.Test;

/**
 * Created by ysidorov on 25.09.15.
 */
public class HighlightTest extends BaseJsonMappingTest {

    @Test
    public void testHighlightRow() throws Exception {
        Payload payload = new Payload(Command.HIGHLIGHT, HighlightTarget.HORN_TARGETS);

        String asString = toJson(payload);

        System.out.println(
                fromJson(asString, Payload.class)
        );
    }
}
