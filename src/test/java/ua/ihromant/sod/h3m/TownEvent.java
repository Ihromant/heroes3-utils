package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class TownEvent {
    private String name;
    private String message;
    private int[] resources;
    private int appliesToPlayers;
    private Integer appliesToHuman;
    private int applicesToComputer;
    private int firstOccurence;
    private int subsequentOccurences;
    private int[] unknown1;
    private int[] buildings;
    private int[] creatureQuantities;
    private int[] unknown2;
}
