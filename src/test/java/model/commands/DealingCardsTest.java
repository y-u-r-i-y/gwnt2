package model.commands;

import model.Card;
import model.CardType;
import model.DeckType;
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
        Card card = new Card("Geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, "the grey one", "1");

        Payload payload = new Payload(Command.DEAL_CARDS, card);

        System.out.println(
            fromJson(toJson(payload), Payload.class)
        );
    }

    @Test
    public void testDealThreeCards() throws Exception {
        Card card1 = new Card("Geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, "the grey one", "1");
        Card card2 = new Card("Yen", CardType.RANGED, DeckType.NEUTRAL, true, false, "hot and annoying", "2");
        Card card3 = new Card("Infantry", CardType.CLOSE, DeckType.NORTHERN, false, false, "poor f_cking infantry", "3");

        Payload payload = new Payload(Command.DEAL_CARDS, new Card[]{card1, card2, card3});

        System.out.println(
                fromJson(toJson(payload), Payload.class)
        );
    }


}
