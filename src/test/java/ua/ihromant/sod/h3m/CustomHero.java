package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CustomHero {
    private int type;
    private int face;
    private String name;
    private int allowedPlayers;
}
