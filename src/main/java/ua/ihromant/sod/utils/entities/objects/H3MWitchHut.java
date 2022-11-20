package ua.ihromant.sod.utils.entities.objects;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.BitSet;

@Getter
@Setter
@Accessors(chain = true)
public class H3MWitchHut extends H3MBaseObject {
    private BitSet potentialSkills;
}
