package model;

import java.util.*;

/**
 * Created by ysidorov on 06.10.15.
 */
public class Dealer {
    // TODO: move from 'static' to singleton

    private static Set<Card> playersDeck = new HashSet<>();
    private static Map<String, Card> dealtCards = new HashMap<>();
    private static Map<String, Card> playedCards = new HashMap<>();
    private static Map<String, Card> perishedCards = new HashMap<>();
    private static Map<String, Card> cardsOnDeck = new HashMap<>();

    private static Card rememberedCard = null;
    private static boolean isPlayingDecoy = false; // TODO: store it here
    private static boolean isPlayingHorn = false; // TODO: store it here
    private static List<String> highlightedCards = new ArrayList<>();


    public static Card[] dealDefaults() {
        Card[] result = {

                // TODO: add SPECIAL attribute ?


            new Card("geralt", CardType.CLOSE, DeckType.NEUTRAL, true, false, false, "Geralt", IdGenerator.nextId()),
            new Card("roche", CardType.CLOSE, DeckType.NORTHERN, true, false, false, "Roche", IdGenerator.nextId()),
            new Card("dijkstra", CardType.CLOSE, DeckType.NORTHERN, false, false, true, "Spy", IdGenerator.nextId()),
            new Card("keira", CardType.RANGED, DeckType.NORTHERN, false, false, false, "", IdGenerator.nextId()),
            new Card("catapult", CardType.SIEGE, DeckType.NORTHERN, false, false, false, "", IdGenerator.nextId()),
            new Card("horn", CardType.HORN, DeckType.NEUTRAL, false, false, false, "", IdGenerator.nextId()),
            new Card("frost", CardType.WEATHER, DeckType.NEUTRAL, false, false, false, "", IdGenerator.nextId()),
            new Card("rain", CardType.WEATHER, DeckType.NEUTRAL, false, false, false, "", IdGenerator.nextId()),
            new Card("clear", CardType.WEATHER, DeckType.NEUTRAL, false, false, false, "", IdGenerator.nextId()),
            new Card("decoy", CardType.DECOY, DeckType.NEUTRAL, false, false, false, "", IdGenerator.nextId()),
            new Card("medic", CardType.SIEGE, DeckType.NORTHERN, false, true, false, "", IdGenerator.nextId()),
            new Card("infantry", CardType.CLOSE, DeckType.NORTHERN, false, false, false, "poor f. infantry", IdGenerator.nextId()),
        };

        for (Card card : result) {
            dealtCards.put(card.getId(), card);
        }
        return result;
    }
    public static Card[] dealCards(int count) {
        Card[] result = new Card[count];
        for (int i = 0; i < count; i++) {
            Card c = new Card("infantry", CardType.CLOSE, DeckType.NORTHERN, false, false, false, "poor f. infantry", IdGenerator.nextId());
            result[i] = c;
            dealtCards.put(c.getId(), c);
        }
        return result;
    }


    public static Card getDealtCard(String cardId) {
        return dealtCards.get(cardId);
    }
    public static Card getPlayedCard(String cardId) {
        return playedCards.get(cardId);
    }
    public static void playCard(Card card) {
        cardsOnDeck.put(card.getId(), card);
        if (! card.isSpy()) {
            playedCards.put(card.getId(), card);
        }
    }
    public static void returnCardToHand(Card card) {
        playedCards.remove(card.getId());
        cardsOnDeck.remove(card.getId());
    }
    public static List<String> getDecoyTargetsIds() {
        List<String> result = new ArrayList<>();
        // played SPY card will be transferred to Opponent's played deck
        // and won't be in playedCards
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
    public static boolean isCardPlayed(Card card) {
        return playedCards.containsKey(card.getId());
    }
    public static void startPlayingDecoy() {
        isPlayingDecoy = true;
    }
    public static void stopPlayingDecoy() {
        isPlayingDecoy = false;
    }
    public static boolean isPlayingDecoy() {
        return isPlayingDecoy;
    }
    public static void startPlayingHorn() {
        isPlayingHorn = true;
    }
    public static void stopPlayingHorn() {
        isPlayingHorn = false;
    }
    public static boolean isPlayingHorn() {
        return isPlayingHorn;
    }
    public static List<String> getHighlightedCards() {
        return new ArrayList<>(highlightedCards);
    }
    public static void addHighlightedCards(List<String> cards) {
        highlightedCards.addAll(cards);
    }
    public static void clearHighlightedCards() {
        highlightedCards.clear();
    }
    public static boolean isGoodDecoyTarget(Card card)  {
        return isCardPlayed(card) &&
                ! (card.isHero()
                        || CardType.WEATHER.equals(card.getType())
                        || CardType.LEADER.equals(card.getType())
                        || CardType.HORN.equals(card.getType()));
    }

}
