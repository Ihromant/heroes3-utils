package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class MessageAndTreasure {
    private String message;
    private int[] resources;
    private int artifact;
}
