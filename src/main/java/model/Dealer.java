package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ysidorov on 06.10.15.
 */
public class Dealer {
    private static Map<String, Card> dealtCards = new HashMap<>();
    private static Map<String, Card> playedCards = new HashMap<>();
    private static Card rememberedCard = null;
    public static Card[] dealDefaults() {
        Card[] result = {
            new Card("geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, "Geralt", IdGenerator.nextId()),
            new Card("roche", CardType.CLOSE, DeckType.NORTHERN, true, false, "Roche", IdGenerator.nextId()),
            new Card("keira", CardType.RANGED, DeckType.NORTHERN, false, false, "", IdGenerator.nextId()),
            new Card("catapult", CardType.SIEGE, DeckType.NORTHERN, false, false, "", IdGenerator.nextId()),
            new Card("horn", CardType.HORN, DeckType.NEUTRAL, false, false, "", IdGenerator.nextId()),
            new Card("frost", CardType.WEATHER, DeckType.NEUTRAL, false, false, "", IdGenerator.nextId()),
            new Card("rain", CardType.WEATHER, DeckType.NEUTRAL, false, false, "", IdGenerator.nextId()),
            new Card("clear", CardType.WEATHER, DeckType.NEUTRAL, false, false, "", IdGenerator.nextId()),
            new Card("decoy", CardType.DECOY, DeckType.NEUTRAL, false, false, "", IdGenerator.nextId()),
            new Card("medic", CardType.SIEGE, DeckType.NORTHERN, false, true, "", IdGenerator.nextId()),
            new Card("infantry", CardType.CLOSE, DeckType.NORTHERN, false, false, "poor f. infantry", IdGenerator.nextId()),
        };

        for (Card card : result) {
            dealtCards.put(card.getId(), card);
        }
        return result;
    }

    public static Card getDealtCard(String cardId) {
        return dealtCards.get(cardId);
    }
    public static Card getPlayedCard(String cardId) {
        return playedCards.get(cardId);
    }
    public static void addPlayedCard(Card card) {
        playedCards.put(card.getId(), card);
    }
    public static List<String> getDecoyTargetsIds() {
        List<String> result = new ArrayList<>();
        for (Card c : playedCards.values()) {
            if (! c.isHero()) {
                switch (c.getType()) {
                    case CLOSE:
                    case RANGED:
                    case SIEGE:
                        result.add(c.getId());
                }
            }
        }
        return result;
    }

    public static void reset() {
        dealtCards.clear();
        playedCards.clear();
    }

    public static void rememberCard(Card card) {
        rememberedCard = card;
    }
    public static void clearRememberedCard() {
        rememberedCard = null;
    }
    public static Card getRememberedCard() {
        return rememberedCard;
    }
}
