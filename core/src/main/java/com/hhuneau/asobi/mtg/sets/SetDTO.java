package com.hhuneau.asobi.mtg.sets;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetDTO {
    String code;
    String name;
    String type;

    public static SetDTO of(MTGSet set) {
        SetDTO setDTO = new SetDTO();
        setDTO.code = set.getCode();
        setDTO.name = set.getName();
        setDTO.type = set.getType();
        return setDTO;
    }
}
