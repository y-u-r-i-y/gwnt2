package model;

/**
 * Created by ysidorov on 06.10.15.
 */
public class Dealer {
    public static Card[] dealDefaults() {
        Card[] result = {
            new Card("geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, "Geralt"),
            new Card("roche", CardType.CLOSE, DeckType.NORTHERN, true, false, "Roche"),
            new Card("keira", CardType.RANGED, DeckType.NORTHERN, false, false, ""),
            new Card("catapult", CardType.SIEGE, DeckType.NORTHERN, false, false, ""),
            new Card("horn", CardType.HORN, DeckType.NEUTRAL, false, false, ""),
            new Card("frost", CardType.WEATHER, DeckType.NEUTRAL, false, false, ""),
            new Card("rain", CardType.WEATHER, DeckType.NEUTRAL, false, false, ""),
            new Card("clear", CardType.WEATHER, DeckType.NEUTRAL, false, false, ""),
            new Card("decoy", CardType.DECOY, DeckType.NEUTRAL, false, false, ""),
            new Card("medic", CardType.SIEGE, DeckType.NORTHERN, false, true, ""),
            new Card("infantry", CardType.CLOSE, DeckType.NORTHERN, false, false, "poor f. infantry"),
        };

        return result;
    }

}
