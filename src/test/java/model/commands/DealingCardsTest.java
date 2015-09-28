package model.commands;

import model.Card;
import model.CardType;
import model.Response;
import model.json.BaseJsonMappingTest;
import org.junit.Test;

/**
 * Created by ysidorov on 25.09.15.
 */
public class DealingCardsTest extends BaseJsonMappingTest {
    @Test
    public void testDealOneCard() throws Exception {
        Card card = new Card("Geralt", CardType.CLOSE, true, false, "the grey one");

        Response response = new Response(Command.DEAL_CARDS, card);

        System.out.println(
            fromJson(toJson(response), Response.class)
        );
    }

    @Test
    public void testDealThreeCards() throws Exception {
        Card card1 = new Card("Geralt", CardType.CLOSE, true, false, "the grey one");
        Card card2 = new Card("Yen", CardType.RANGED, true, false, "hot and annoying");
        Card card3 = new Card("Infantry", CardType.CLOSE, false, false, "poor f_cking infantry");

        Response response = new Response(Command.DEAL_CARDS, new Card[]{card1, card2, card3});

        System.out.println(
                fromJson(toJson(response), Response.class)
        );
    }


}
