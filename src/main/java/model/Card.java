package model;

/**
 * Created by yuriy on 08.07.2015.
 */
public class Card {

    private String face;
    private String description;
    private CardType type;
    private boolean isHero = false;

    public Card() {}

    public Card(String face, String description, CardType type) {
        this.face = face;
        this.description = description;
        this.type = type;
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

    @Override
    public String toString() {
        return "Card{" +
                "face='" + face + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", isHero=" + isHero +
                '}';
    }
}
