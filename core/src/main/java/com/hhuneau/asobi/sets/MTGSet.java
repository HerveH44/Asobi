package com.hhuneau.asobi.sets;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class MTGSet {
    @Id
    private String code;

    private String name;
    private Date releaseDate;
    private String border;
    private String type;

    @OneToMany(mappedBy = "set")
    private List<MTGCard> cards;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<MTGCard> getCards() {
        return cards;
    }

    public void setCards(List<MTGCard> cards) {
        this.cards = cards;
    }
}
