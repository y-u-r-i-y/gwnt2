package model.commands;

import model.*;
import protocol.Command;
import protocol.Payload;
import model.json.BaseJsonMappingTest;
import org.junit.Test;

/**
 * Created by ysidorov on 25.09.15.
 */
public class DealingCardsTest extends BaseJsonMappingTest {
    @Test
    public void testDealOneCard() throws Exception {
        Card card = new Card(IdGenerator.nextId(), "geralt", CardType.CLOSE, Deck.NEUTRAL, 15, "Geralt", true, false, false);

        Payload payload = new Payload(Command.DEAL_CARDS, card);

        System.out.println(
            fromJson(toJson(payload), Payload.class)
        );
    }

    @Test
    public void testDealThreeCards() throws Exception {
        Card card1 = new Card(IdGenerator.nextId(), "geralt", CardType.CLOSE, Deck.NEUTRAL, 15, "Geralt", true, false, false);
        Card card2 = new Card(IdGenerator.nextId(), "yen", CardType.CLOSE, Deck.NEUTRAL, 15, "yen", true, false, false);
        Card card3 = new Card(IdGenerator.nextId(), "infantry", CardType.CLOSE, Deck.NORTHERN, 1, "pfi", Bond.NORTHERN_INFANTRY);

        Payload payload = new Payload(Command.DEAL_CARDS, new Card[]{card1, card2, card3});

        System.out.println(
                fromJson(toJson(payload), Payload.class)
        );
    }


}
