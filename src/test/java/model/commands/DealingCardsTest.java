package model.commands;

import model.Card;
import model.CardType;
import model.Response;
import model.json.BaseJsonMappingTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by ysidorov on 25.09.15.
 */
public class DealingCardsTest extends BaseJsonMappingTest{
    @Test
    public void testDealOneCard() throws Exception {
        Card card = new Card("Geralt", "hero", CardType.CLOSE);

        Response response = new Response(Command.DEAL_CARDS, card);
        String asString = toJson(card);
        System.out.println(response);
    }
}
