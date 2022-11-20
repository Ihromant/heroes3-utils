package ua.ihromant.sod.utils.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ua.ihromant.sod.utils.entities.objects.H3MArtifact;
import ua.ihromant.sod.utils.entities.objects.H3MBaseObject;
import ua.ihromant.sod.utils.entities.objects.H3MGarrison;
import ua.ihromant.sod.utils.entities.objects.H3MGrail;
import ua.ihromant.sod.utils.entities.objects.H3MHero;
import ua.ihromant.sod.utils.entities.objects.H3MMapEvent;
import ua.ihromant.sod.utils.entities.objects.H3MMapMonster;
import ua.ihromant.sod.utils.entities.objects.H3MMapTown;
import ua.ihromant.sod.utils.entities.objects.H3MMessage;
import ua.ihromant.sod.utils.entities.objects.H3MOwnedObject;
import ua.ihromant.sod.utils.entities.objects.H3MPandoraBox;
import ua.ihromant.sod.utils.entities.objects.H3MQuestGuard;
import ua.ihromant.sod.utils.entities.objects.H3MRandomDwelling;
import ua.ihromant.sod.utils.entities.objects.H3MResource;
import ua.ihromant.sod.utils.entities.objects.H3MScholar;
import ua.ihromant.sod.utils.entities.objects.H3MSeerHut;
import ua.ihromant.sod.utils.entities.objects.H3MShrine;
import ua.ihromant.sod.utils.entities.objects.H3MSpellScroll;
import ua.ihromant.sod.utils.entities.objects.H3MWitchHut;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class H3MMap {
    private H3MHeader header;
    private H3MHero[] customHeroes;
    private BitSet availableArtifacts;
    private BitSet availableSpells;
    private BitSet availableSkills;
    private H3MRumor[] rumors;
    private H3MHero[] sodHeroes;
    private MapTile[][][] tiles;
    private H3MObjectAttribute[] objectAttributes;
    private List<H3MHero> placeholders = new ArrayList<>();
    private List<H3MQuestGuard> questGuards = new ArrayList<>();
    private List<H3MPandoraBox> pandoras = new ArrayList<>();
    private List<H3MMessage> messages = new ArrayList<>();
    private List<H3MGarrison> garrisons = new ArrayList<>();
    private List<H3MMapEvent> mapEvents = new ArrayList<>();
    private List<H3MGrail> grails = new ArrayList<>();
    private List<H3MOwnedObject> ownedObjects = new ArrayList<>();
    private List<H3MBaseObject> baseObjects = new ArrayList<>();
    private List<H3MMapTown> towns = new ArrayList<>();
    private List<H3MRandomDwelling> randomDwellings = new ArrayList<>();
    private List<H3MHero> mapHeroes = new ArrayList<>();
    private List<H3MMapMonster> mapMonsters = new ArrayList<>();
    private List<H3MArtifact> artifacts = new ArrayList<>();
    private List<H3MShrine> shrines = new ArrayList<>();
    private List<H3MSpellScroll> scrolls = new ArrayList<>();
    private List<H3MResource> resources = new ArrayList<>();
    private List<H3MWitchHut> witchHuts = new ArrayList<>();
    private List<H3MSeerHut> seerHuts = new ArrayList<>();
    private List<H3MScholar> scholars = new ArrayList<>();
    private H3MTimeEvent[] timeEvents;
}
