package ua.ihromant.sod.h3m;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HeroArtifactSettings {
    private int headwear;
    private int shoulders;
    private int rightHand;
    private int leftHand;
    private int torso;
    private int rightRing;
    private int leftRing;
    private int feet;
    private int misc1;
    private int misc2;
    private int misc3;
    private int misc4;
    private int device1;
    private int device2;
    private int device3;
    private int device4;
    private int unknown;
    private int spellbook;
    private int misc5;
    private int[] backpack;
}
