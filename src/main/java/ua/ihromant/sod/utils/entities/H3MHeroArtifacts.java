package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MHeroArtifacts {
    private int headwear;
    private int cape;
    private int necklace;
    private int rightHand;
    private int leftHand;
    private int torso;
    private int rightRing;
    private int leftRing;
    private int feet;
    private int misc0;
    private int misc1;
    private int misc2;
    private int misc3;
    private int ballista;
    private int ammoCart;
    private int firstAidTent;
    private int catapult;
    private int spellbook;
    private Integer misc4;
    private int[] backpack;
}
