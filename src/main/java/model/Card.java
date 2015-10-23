package model;

/**
 * Created by yuriy on 08.07.2015.
 */
public class Card {

    private String face;
    private String description;
    private CardType type;
    private Deck deck;
    private Bond bond = Bond.NONE;
    private boolean isHero = false;
    private boolean isHealer = false;
    private boolean isSpy = false;
    private String id;
    private int score;
    public Card() {}

    public Card(String id, String face, CardType type, Deck deck, int score,
                String description, boolean isHero, boolean isHealer, boolean isSpy,
                Bond bond) {
        this.face = face;
        this.type = type;
        this.deck = deck;
        this.isHero = isHero;
        this.isHealer = isHealer;
        this.isSpy = isSpy;
        this.description = description;
        this.id = id;
        this.score = score;
        this.bond = bond;
    }
    public Card(String id, String face, CardType type, Deck deck, int score,
                String description, boolean isHero, boolean isHealer, boolean isSpy) {
        this.face = face;
        this.type = type;
        this.deck = deck;
        this.isHero = isHero;
        this.isHealer = isHealer;
        this.isSpy = isSpy;
        this.description = description;
        this.id = id;
        this.score = score;
    }

    public Card(String id, String face, CardType type, Deck deck, int score, String description) {
        this.id = id;
        this.face = face;
        this.type = type;
        this.deck = deck;
        this.score = score;
        this.description = description;
    }
    public Card(String id, String face, CardType type, Deck deck, int score, String description, Bond bond) {
        this.id = id;
        this.face = face;
        this.type = type;
        this.deck = deck;
        this.score = score;
        this.description = description;
        this.bond = bond;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public boolean isHero() {
        return isHero;
    }

    public void setHero(boolean isHero) {
        this.isHero = isHero;
    }

    public boolean isHealer() {
        return isHealer;
    }

    public void setHealer(boolean isHealer) {
        this.isHealer = isHealer;
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSpy() {
        return isSpy;
    }

    public void setSpy(boolean isSpy) {
        this.isSpy = isSpy;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Bond getBond() {
        return bond;
    }

    public void setBond(Bond bond) {
        this.bond = bond;
    }

    @Override
    public String toString() {
        return "Card{" +
                "face='" + face + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", isHero=" + isHero +
                ", id=" + id +
                '}';
    }
}
