package model;

import model.json.BaseJsonMappingTest;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

public class CardTest extends BaseJsonMappingTest{

    @Test
    public void testSimpleCardSerialize() throws IOException {
        Card c = new Card("Geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, "the grey one");
        c.setHero(true);

        String asString = toJson(c);
        System.out.println(asString);

    }
    @Test
    public void testSimpleCardDeSerialize() throws IOException {
        String json = "{\"face\":\"Geralt\",\"description\":\"hero\",\"type\":\"CLOSE\",\"hero\":true}";
        Card card = jsonMapper.readValue(json, Card.class);
        System.out.println(card);
    }
    @Test
    public void testSimpleCardDeSerializeBothWays() throws IOException {
        Card c = new Card("Geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, "the grey one");

        String asString = toJson(c);

        Card card = fromJson(asString, Card.class);
        System.out.println(card);
    }
}
