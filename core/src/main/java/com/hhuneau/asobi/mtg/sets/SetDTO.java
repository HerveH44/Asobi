package com.hhuneau.asobi.mtg.sets;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SetDTO {
    String code;
    String name;
    String type;
    Date releaseDate;

    public static SetDTO of(MTGSet set) {
        SetDTO setDTO = new SetDTO();
        setDTO.code = set.getCode();
        setDTO.name = set.getName();
        setDTO.type = set.getType();
        setDTO.releaseDate = set.getReleaseDate();
        return setDTO;
    }
}
