package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class H3MHeroArtifacts {
    private Integer headwear;
    private Integer cape;
    private Integer necklace;
    private Integer rightHand;
    private Integer leftHand;
    private Integer torso;
    private Integer rightRing;
    private Integer leftRing;
    private Integer feet;
    private Integer misc0;
    private Integer misc1;
    private Integer misc2;
    private Integer misc3;
    private Integer ballista;
    private Integer ammoCart;
    private Integer firstAidTent;
    private Integer catapult;
    private Integer spellbook;
    private Integer misc4;
    private int[] backpack;
}
