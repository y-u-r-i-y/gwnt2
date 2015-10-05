package model.commands;

import protocol.Command;
import protocol.HighlightTarget;
import protocol.Response;
import model.json.BaseJsonMappingTest;
import org.junit.Test;

/**
 * Created by ysidorov on 25.09.15.
 */
public class HighlightTest extends BaseJsonMappingTest {

    @Test
    public void testHighlightRow() throws Exception {
        Response response = new Response(Command.HIGHLIGHT, HighlightTarget.HORN_TARGETS);

        String asString = toJson(response);

        System.out.println(
                fromJson(asString, Response.class)
        );
    }
}
