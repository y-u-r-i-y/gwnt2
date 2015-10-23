package model;

import protocol.HornTarget;
import protocol.Row;

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

    private static Map<Bond, List<Card>> bondMap = new HashMap<>();

    private static Map<Row, RowState> rowStates = new HashMap<>();
    static {
        resetRowStates();
    }

    private static Card rememberedCard = null;
    private static boolean isPlayingDecoy = false; // TODO: store it here
    private static boolean isPlayingHorn = false; // TODO: store it here
    private static List<String> highlightedCards = new ArrayList<>();


    public static Card[] dealDefaults() {
        Card[] result = {

                // TODO: add SPECIAL attribute ?

            new Card(IdGenerator.nextId(), "geralt", CardType.CLOSE, Deck.NEUTRAL, 15, "Geralt", true, false, false),
            new Card(IdGenerator.nextId(), "roche", CardType.CLOSE, Deck.NORTHERN, 10, "Roche", true, false, false),
            new Card(IdGenerator.nextId(), "dijkstra", CardType.CLOSE, Deck.NORTHERN, 4, "Spy", false, false, true),
            new Card(IdGenerator.nextId(), "keira", CardType.RANGED, Deck.NORTHERN, 5, "Keira"),
            new Card(IdGenerator.nextId(), "catapult", CardType.SIEGE, Deck.NORTHERN, 8, "catapult"),
            new Card(IdGenerator.nextId(), "horn", CardType.HORN, Deck.NEUTRAL, 0, "horn"),
            new Card(IdGenerator.nextId(), "frost", CardType.WEATHER, Deck.NEUTRAL, 0, "frost"),
            new Card(IdGenerator.nextId(), "rain", CardType.WEATHER, Deck.NEUTRAL, 0, "frost"),
            new Card(IdGenerator.nextId(), "clear", CardType.WEATHER, Deck.NEUTRAL, 0, "clear"),
            new Card(IdGenerator.nextId(), "decoy", CardType.DECOY, Deck.NEUTRAL, 0, "decoy"),
            new Card(IdGenerator.nextId(), "medic", CardType.SIEGE, Deck.NORTHERN, 5, "medic"),
            new Card(IdGenerator.nextId(), "infantry", CardType.CLOSE, Deck.NORTHERN, 1, "pfi", Bond.NORTHERN_INFANTRY),
        };

        for (Card card : result) {
            dealtCards.put(card.getId(), card);
        }
        return result;
    }
    public static Card[] dealCards(int count) {
        Card[] result = new Card[count];
        for (int i = 0; i < count; i++) {
            Card c = new Card(IdGenerator.nextId(), "infantry", CardType.CLOSE, Deck.NORTHERN, 1, "pfi", Bond.NORTHERN_INFANTRY);
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
        perishedCards.clear();
        cardsOnDeck.clear();
        bondMap.clear();
        resetRowStates();
    }
    public static void resetRowStates() {
        for (Row row : Row.values()) {
            rowStates.put(row, new RowState());
        }
    }
    public static void playDecoyOnCard(Card card) {
        Card decoy = Dealer.getRememberedCard();
        Dealer.playCard(decoy);
        Dealer.returnCardToHand(card);
        Dealer.clearRememberedCard();
        Dealer.clearHighlightedCards();
        Dealer.stopPlayingDecoy();
    }
    public static void playHorn(HornTarget target) {
        Row row;
        switch (target) {
            case CLOSE_COMBAT_HORN:
                row = Row.YOUR_CLOSE_COMBAT_ROW;
                break;
            case RANGED_COMBAT_HORN:
                row = Row.YOUR_RANGED_COMBAT_ROW;
                break;
            case SIEGE_COMBAT_HORN:
                row = Row.YOUR_SIEGE_COMBAT_ROW;
                break;
            default:
                throw new IllegalArgumentException("no such horn target: " + target);
        }
        rowStates.get(row).hornPlayed = true;
        Dealer.playCard(Dealer.getRememberedCard());
        Dealer.clearRememberedCard();
        Dealer.stopPlayingHorn();
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
    public static Map<Row, Integer> getRowScores() {
        Map<Row, Integer> scores = new HashMap<>();
        for (Row row : Row.values()) {
            scores.put(row, 0);
        }
        for (Card card : cardsOnDeck.values()) {
            int score = card.getScore();
            Row row;
            switch (card.getType()) {
                case CLOSE:
                    row = card.isSpy() ? Row.HIS_CLOSE_COMBAT_ROW : Row.YOUR_CLOSE_COMBAT_ROW;
                    break;
                case RANGED:
                    row = card.isSpy() ? Row.HIS_RANGED_COMBAT_ROW : Row.YOUR_RANGED_COMBAT_ROW;
                    break;
                case SIEGE:
                    row = card.isSpy() ? Row.HIS_SIEGE_COMBAT_ROW : Row.YOUR_SIEGE_COMBAT_ROW;
                    break;
                default:
                    continue;
            }
            if (rowStates.get(row).hornPlayed && ! card.isHero()) {
                score *= 2;
            }
            // TODO: take into account weather effects
            scores.put(row, scores.get(row) + score);
        }
        return scores;
    }

    public static boolean hasBondedCardOnDesk(Card card) { // or is isBondInPlay a better name?
        return bondMap.containsKey(card.getBond());
    }

    public static String getFirstBondedCardId(Card card) {
        return bondMap.get(card.getBond()).get(0).getId();
    }

    public static void createBond(Card card) {
        List<Card> newBondedGroup = new ArrayList<>();
        newBondedGroup.add(card);
        bondMap.put(card.getBond(), newBondedGroup);
    }

    public static List<String> getAllBondedCardIds(Card card) {
        // return bondMap.get(card.getBond()).; TODO: implement bond highlighting
        return Collections.emptyList();
    }
}
