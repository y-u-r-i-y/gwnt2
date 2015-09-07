package model;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class CardTest {
    private ObjectMapper mapper;
    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testSimpleCardSerialize() throws IOException {
        Card c = new Card("Geralt", "hero", CardType.CLOSE);
        c.setHero(true);

        ObjectMapper mapper = new ObjectMapper();
        String asString = mapper.writeValueAsString(c);
        System.out.println(asString);

    }
    @Test
    public void testSimpleCardDeSerialize() throws IOException {
        String json = "{\"face\":\"Geralt\",\"description\":\"hero\",\"type\":\"CLOSE\",\"hero\":true}";
        Card card = mapper.readValue(json, Card.class);
        System.out.println(card);
    }
}
