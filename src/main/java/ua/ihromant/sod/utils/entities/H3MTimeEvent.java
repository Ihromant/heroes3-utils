package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MTimeEvent {
    private String eventName;
    private String eventMessage;
    private int[] resources;
    private int appliesToPlayers;
    private Integer appliesToHuman;
    private int appliesToComputer;
    private int firstOccurence;
    private int subsequentOccurences;
}
