package model;

import model.json.BaseJsonMappingTest;
import org.junit.Test;

import java.io.IOException;

public class CardTest extends BaseJsonMappingTest{

    @Test
    public void testSimpleCardSerialize() throws IOException {
        Card c = new Card(IdGenerator.nextId(), "geralt", CardType.CLOSE, Deck.NEUTRAL, 15, "Geralt", true, false, false);
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
        Card c = new Card(IdGenerator.nextId(), "geralt", CardType.CLOSE, Deck.NEUTRAL, 15, "Geralt", true, false, false);

        String asString = toJson(c);

        Card card = fromJson(asString, Card.class);
        System.out.println(card);
    }
}
