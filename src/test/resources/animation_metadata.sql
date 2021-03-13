create table public.animation_metadata (
id int4 not null primary key,
moving int4,
mouse_over int4,
standing int4,
getting_hit int4,
defend int4,
death int4,
sk int4,
turn_left int4,
turn_right int4,
turn_left1 int4,
turn_right1 int4,
attack_up int4,
attack_straight int4,
attack_down int4,
shoot_up int4,
shoot_straight int4,
shoot_down int4,
sk1 int4,
sk2 int4,
sk3 int4,
start_moving int4,
stop_moving int4
);

-- drop table public.animation_metadata;

insert into public.animation_metadata (
id, moving, mouse_over, standing, getting_hit, defend, death, sk,
turn_left, turn_right, turn_left1, turn_right1, attack_up, attack_straight,
attack_down, shoot_up, shoot_straight, shoot_down, sk1, sk2, sk3, start_moving, stop_moving
) values
((SELECT id FROM creature WHERE full_name = 'CHAMPION'),8,5,4,8,6,8,null,3,3,3,3,10,10,9,null,null,null,null,null,null,3,4),
((SELECT id FROM creature WHERE full_name = 'PIKEMAN'),6,4,7,6,13,5,null,2,2,2,2,10,10,11,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'CRUSADER'),8,8,8,6,11,6,null,2,2,2,2,7,7,7,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'ZEALOT'),6,10,6,8,7,11,null,2,2,2,2,9,8,9,13,14,13,10,10,10,2,null),
((SELECT id FROM creature WHERE full_name = 'ANGEL'),7,10,8,6,10,8,null,2,2,2,2,6,6,6,6,6,6,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'CAVALIER'),8,5,4,8,6,8,null,3,3,3,3,10,10,9,null,null,null,null,null,null,3,4),
((SELECT id FROM creature WHERE full_name = 'MARKSMAN'),8,9,8,6,10,6,null,2,2,2,2,6,6,6,8,8,8,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'ROYAL_GRIFFIN'),4,8,8,7,8,9,null,3,3,3,3,9,9,9,null,null,null,null,null,null,4,4),
((SELECT id FROM creature WHERE full_name = 'ARCHER'),8,9,8,6,9,6,null,2,2,2,2,6,6,6,8,8,8,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'HALBERDIER'),6,4,8,6,12,4,null,2,2,2,2,6,6,7,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'MONK'),6,10,6,8,7,11,null,2,2,2,2,9,8,9,10,10,9,null,null,null,2,null),
((SELECT id FROM creature WHERE full_name = 'ARCHANGEL'),7,7,8,6,10,8,null,2,2,2,2,6,6,6,6,6,6,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'GRIFFIN'),4,8,8,7,8,9,null,3,3,3,3,9,9,9,null,null,null,null,null,null,4,4),
((SELECT id FROM creature WHERE full_name = 'SWORDSMAN'),8,8,8,6,11,6,null,2,2,2,2,7,7,7,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'DWARF'),8,7,8,6,11,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'GRAND_ELF'),8,11,8,6,11,5,null,2,2,2,2,8,8,8,12,12,12,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'BATTLE_DWARF'),8,7,8,6,11,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'CENTAUR_CAPTAIN'),8,9,9,6,11,8,null,3,3,3,3,8,8,8,8,8,8,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'UNICORN'),7,5,8,7,11,8,null,3,3,3,3,9,9,9,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'DENDROID_SOLDIER'),9,9,8,7,10,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,null),
((SELECT id FROM creature WHERE full_name = 'WAR_UNICORN'),7,5,8,7,11,8,null,3,3,3,3,9,9,9,null,null,null,7,7,7,2,2),
((SELECT id FROM creature WHERE full_name = 'GOLD_DRAGON'),4,5,9,6,6,8,null,3,3,3,3,9,9,9,null,null,null,9,9,9,4,4),
((SELECT id FROM creature WHERE full_name = 'WOOD_ELF'),8,11,8,6,11,5,null,2,2,2,2,8,8,8,12,12,12,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'PEGASUS'),6,9,8,6,11,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'CENTAUR'),8,4,8,6,11,8,null,3,3,3,3,8,8,8,8,8,8,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'DENDROID_GUARD'),9,9,8,7,10,8,null,3,3,3,3,7,7,7,null,null,null,8,8,8,1,null),
((SELECT id FROM creature WHERE full_name = 'SILVER_PEGASUS'),6,9,8,6,11,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'GREEN_DRAGON'),4,5,8,6,6,8,null,3,3,3,3,9,9,9,null,null,null,9,9,9,4,4),
((SELECT id FROM creature WHERE full_name = 'STONE_GARGOYLE'),6,9,8,6,11,17,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'NAGA_QUEEN'),8,8,8,6,11,8,null,2,2,2,2,9,9,9,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'GREMLIN'),8,14,8,7,10,9,null,3,3,3,3,8,8,8,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'NAGA'),8,8,8,6,11,8,null,2,2,2,2,9,8,9,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'GENIE'),4,7,7,7,7,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'MASTER_GENIE'),4,6,7,7,7,8,null,3,3,3,2,7,7,7,null,null,null,7,7,7,1,1),
((SELECT id FROM creature WHERE full_name = 'MAGE'),8,11,8,6,11,8,null,2,2,2,2,10,10,10,13,13,13,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'ARCH_MAGE'),8,11,8,6,11,8,null,2,2,2,2,10,10,10,13,13,13,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'IRON_GOLEM'),9,5,8,7,8,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'STONE_GOLEM'),9,5,8,7,8,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'MASTER_GREMLIN'),8,14,8,7,10,9,null,3,3,3,3,8,8,8,17,16,16,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'TITAN'),9,8,8,7,9,9,null,3,3,3,3,7,7,7,8,7,7,null,null,null,1,null),
((SELECT id FROM creature WHERE full_name = 'OBSIDIAN_GARGOYLE'),6,9,8,6,11,17,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'GIANT'),9,8,8,7,8,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,null),
((SELECT id FROM creature WHERE full_name = 'GOG'),8,10,6,7,7,9,null,3,3,3,3,8,8,9,8,8,9,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'EFREET'),7,5,5,5,11,8,null,2,2,2,2,9,9,10,null,null,null,null,11,null,6,6),
((SELECT id FROM creature WHERE full_name = 'FAMILIAR'),6,8,8,7,7,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,2),
((SELECT id FROM creature WHERE full_name = 'MAGOG'),8,10,6,7,7,9,null,3,3,3,3,8,8,9,8,8,9,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'PIT_LORD'),8,11,8,6,11,5,null,2,2,2,2,8,8,8,null,null,null,null,15,null,2,2),
((SELECT id FROM creature WHERE full_name = 'CERBERUS'),6,10,8,7,8,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'EFREET_SULTAN'),7,5,5,5,11,8,null,2,2,2,2,9,9,10,null,null,null,null,11,null,6,6),
((SELECT id FROM creature WHERE full_name = 'HORNED_DEMON'),8,9,8,7,10,9,null,2,2,2,2,7,7,7,null,null,null,null,null,null,2,1),
((SELECT id FROM creature WHERE full_name = 'PIT_FIEND'),8,11,8,6,11,5,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'IMP'),6,8,8,7,7,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,2),
((SELECT id FROM creature WHERE full_name = 'DEVIL'),1,13,8,6,11,8,null,2,2,2,2,8,8,8,null,null,null,null,null,null,14,10),
((SELECT id FROM creature WHERE full_name = 'ARCH_DEVIL'),1,13,8,6,11,8,null,2,2,2,2,8,8,8,null,null,null,null,null,null,14,10),
((SELECT id FROM creature WHERE full_name = 'DEMON'),8,9,8,7,10,9,null,2,2,2,2,7,7,7,null,null,null,null,null,null,2,1),
((SELECT id FROM creature WHERE full_name = 'HELL_HOUND'),6,10,8,7,8,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'ZOMBIE'),10,9,8,7,8,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'WRAITH'),8,10,8,6,10,8,null,2,2,2,2,7,7,7,7,7,7,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'BLACK_KNIGHT'),8,11,8,6,11,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'BONE_DRAGON'),6,5,8,6,11,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,3,4),
((SELECT id FROM creature WHERE full_name = 'VAMPIRE'),6,9,8,7,9,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,5,5),
((SELECT id FROM creature WHERE full_name = 'DREAD_KNIGHT'),8,11,8,6,11,8,null,3,3,3,3,8,8,8,null,null,null,11,11,11,2,2),
((SELECT id FROM creature WHERE full_name = 'SKELETON'),8,11,8,6,11,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'WIGHT'),8,10,8,6,10,8,null,2,2,2,2,7,7,7,7,7,7,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'WALKING_DEAD'),10,9,8,7,8,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'POWER_LICH'),8,11,8,6,11,7,null,2,2,2,2,8,8,8,11,11,11,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'GHOST_DRAGON'),6,9,8,6,11,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,3,4),
((SELECT id FROM creature WHERE full_name = 'VAMPIRE_LORD'),6,9,8,7,9,8,null,3,3,3,3,7,7,7,null,null,null,7,7,7,5,5),
((SELECT id FROM creature WHERE full_name = 'SKELETON_WARRIOR'),8,11,8,6,11,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'LICH'),8,11,8,6,11,7,null,2,2,2,2,8,8,8,11,11,11,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'HARPY'),7,11,7,6,8,7,null,3,3,3,3,12,12,10,null,null,null,null,null,null,4,5),
((SELECT id FROM creature WHERE full_name = 'MEDUSA_QUEEN'),8,10,8,6,10,8,null,2,2,2,2,10,10,10,10,10,10,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'BEHOLDER'),11,3,7,6,11,5,7,2,2,2,2,7,7,7,8,7,7,7,7,7,3,3),
((SELECT id FROM creature WHERE full_name = 'MANTICORE'),6,8,8,6,11,8,null,3,3,3,3,8,8,8,null,null,null,8,8,8,3,3),
((SELECT id FROM creature WHERE full_name = 'SCORPICORE'),6,8,8,6,11,8,null,3,3,3,3,8,8,8,null,null,null,8,8,8,3,3),
((SELECT id FROM creature WHERE full_name = 'MINOTAUR_KING'),7,6,10,8,8,7,null,3,3,3,3,8,8,9,null,null,null,8,8,8,null,null),
((SELECT id FROM creature WHERE full_name = 'MEDUSA'),7,10,8,6,10,8,null,2,2,2,2,11,11,11,10,10,10,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'TROGLODYTE'),7,7,4,6,6,7,null,2,2,2,2,8,10,8,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'BLACK_DRAGON'),4,5,8,7,9,10,null,3,3,3,3,10,8,11,null,null,null,10,8,11,4,6),
((SELECT id FROM creature WHERE full_name = 'RED_DRAGON'),4,5,8,7,9,10,null,3,3,3,3,10,8,11,null,null,null,10,8,11,4,6),
((SELECT id FROM creature WHERE full_name = 'INFERNAL_TROGLODYTE'),7,7,6,6,6,8,null,2,2,2,2,8,10,8,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'HARPY_HAG'),7,11,7,6,8,7,null,3,3,3,3,12,12,10,null,null,null,null,13,null,3,3),
((SELECT id FROM creature WHERE full_name = 'MINOTAUR'),7,6,10,8,8,7,null,3,3,3,3,8,8,9,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'EVIL_EYE'),11,3,7,6,11,5,7,2,2,2,2,7,7,7,8,7,7,7,7,7,3,3),
((SELECT id FROM creature WHERE full_name = 'WOLF_RAIDER'),6,9,8,7,8,7,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'OGRE'),9,9,8,7,9,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'CYCLOPS_KING'),8,7,6,3,4,5,null,3,3,3,3,7,7,7,16,17,17,12,12,12,4,5),
((SELECT id FROM creature WHERE full_name = 'ORC'),8,11,8,6,11,8,null,2,2,2,2,6,6,6,7,7,7,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'OGRE_MAGE'),9,9,8,7,9,8,null,3,3,3,3,7,7,7,null,null,null,8,8,8,1,1),
((SELECT id FROM creature WHERE full_name = 'BEHEMOTH'),9,10,8,7,8,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'GOBLIN'),9,9,8,7,9,9,null,3,3,2,3,8,8,8,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'ROC'),6,7,8,7,9,8,null,3,3,3,3,9,8,9,null,null,null,null,null,null,4,4),
((SELECT id FROM creature WHERE full_name = 'THUNDERBIRD'),6,8,8,7,9,8,null,3,3,3,3,9,8,9,null,null,null,null,null,null,4,4),
((SELECT id FROM creature WHERE full_name = 'ANCIENT_BEHEMOTH'),9,10,8,7,7,8,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'ORC_CHIEFTAIN'),8,11,8,6,11,8,null,2,2,2,2,7,7,7,7,7,7,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'HOBGOBLIN'),9,9,7,7,9,9,null,3,3,3,3,8,8,8,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'WOLF_RIDER'),6,9,8,7,8,7,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'CYCLOPS'),8,7,6,3,4,5,null,3,3,3,3,7,7,7,16,17,17,null,null,null,4,5),
((SELECT id FROM creature WHERE full_name = 'MIGHTY_GORGON'),6,4,8,6,12,5,null,3,3,3,3,8,7,9,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'GNOLL'),8,15,8,6,11,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'LIZARDMAN'),7,10,8,6,10,8,null,2,2,2,2,6,6,6,10,10,10,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'HYDRA'),8,7,8,6,10,8,null,2,2,2,2,17,17,17,17,17,17,17,17,17,2,2),
((SELECT id FROM creature WHERE full_name = 'LIZARD_WARRIOR'),7,10,8,6,10,8,null,2,2,2,2,9,9,9,10,10,10,null,null,null,2,3),
((SELECT id FROM creature WHERE full_name = 'SERPENT_FLY'),6,5,6,6,7,7,null,2,2,2,2,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'GNOLL_MARAUDER'),8,15,8,6,11,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'CHAOS_HYDRA'),8,6,8,6,10,8,null,2,2,2,2,17,17,17,17,17,17,17,17,17,2,2),
((SELECT id FROM creature WHERE full_name = 'GORGON'),6,4,8,6,12,5,null,3,3,3,3,7,6,6,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'WYVERN_MONARCH'),6,8,8,9,8,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,4,6),
((SELECT id FROM creature WHERE full_name = 'WYVERN'),6,8,8,9,8,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,4,6),
((SELECT id FROM creature WHERE full_name = 'BASILISK'),8,4,8,6,10,6,null,2,2,2,2,6,6,6,null,null,null,11,11,11,2,2),
((SELECT id FROM creature WHERE full_name = 'GREATER_BASILISK'),8,4,8,6,11,6,null,2,2,2,2,6,6,6,null,null,null,11,11,11,2,2),
((SELECT id FROM creature WHERE full_name = 'DRAGON_FLY'),6,5,6,6,7,7,null,2,2,2,2,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'MAGIC_ELEMENTAL'),8,7,8,6,13,6,null,2,2,2,2,9,9,9,null,null,null,9,9,9,2,2),
((SELECT id FROM creature WHERE full_name = 'WATER_ELEMENTAL'),8,7,8,6,15,8,null,3,3,3,3,8,8,8,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'ENERGY_ELEMENTAL'),7,6,7,6,11,9,null,2,2,2,2,8,8,8,null,null,null,10,10,10,3,9),
((SELECT id FROM creature WHERE full_name = 'PHOENIX'),6,9,8,6,9,7,null,3,3,3,3,12,12,12,null,null,null,null,null,null,3,3),
((SELECT id FROM creature WHERE full_name = 'EARTH_ELEMENTAL'),8,9,8,6,11,6,null,2,2,2,2,7,7,7,null,null,null,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'MAGMA_ELEMENTAL'),8,9,8,6,11,6,null,2,2,2,2,7,7,7,null,null,null,8,8,8,2,2),
((SELECT id FROM creature WHERE full_name = 'PIXIE'),6,9,8,8,10,6,null,2,2,2,2,10,10,10,null,null,null,null,null,null,3,3),
((SELECT id FROM creature WHERE full_name = 'ICE_ELEMENTAL'),7,7,8,6,11,12,null,3,3,3,3,9,9,9,8,8,8,8,8,8,2,3),
((SELECT id FROM creature WHERE full_name = 'SPRITE'),6,9,8,8,9,6,null,2,2,2,2,10,10,10,null,null,null,null,null,null,3,3),
((SELECT id FROM creature WHERE full_name = 'AIR_ELEMENTAL'),4,6,6,7,11,10,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'FIREBIRD'),6,8,8,6,9,7,null,3,3,3,3,12,12,12,null,null,null,null,null,null,3,3),
((SELECT id FROM creature WHERE full_name = 'FIRE_ELEMENTAL'),6,7,6,6,13,9,null,2,2,2,2,15,15,15,null,null,null,null,null,null,3,7),
((SELECT id FROM creature WHERE full_name = 'PSYCHIC_ELEMENTAL'),8,7,8,6,13,6,null,2,2,2,2,9,9,9,null,null,null,9,9,9,2,2),
((SELECT id FROM creature WHERE full_name = 'STORM_ELEMENTAL'),4,5,10,6,11,9,null,2,2,2,2,6,6,6,6,6,6,6,6,6,1,1),
((SELECT id FROM creature WHERE full_name = 'BOAR'),8,9,12,8,7,8,null,2,2,2,2,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'DIAMOND_GOLEM'),9,5,6,7,8,9,null,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'MUMMY'),8,8,6,8,8,10,null,2,2,2,2,8,8,8,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'TROLL'),8,10,6,6,8,8,null,2,2,2,2,9,9,9,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'ENCHANTER'),8,11,8,6,6,8,null,2,2,2,2,12,12,12,10,10,10,null,10,null,2,2),
((SELECT id FROM creature WHERE full_name = 'SHARPSHOOTER'),8,9,8,6,9,7,null,2,2,2,2,8,8,8,12,12,12,null,null,null,2,2),
((SELECT id FROM creature WHERE full_name = 'ROGUE'),8,12,10,8,9,9,null,2,null,null,null,10,8,9,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'CRYSTAL_DRAGON'),8,6,12,7,7,9,null,3,3,3,3,9,9,9,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'HALFLING'),8,22,20,9,11,8,null,2,2,2,2,13,12,10,19,16,17,null,null,null,null,1),
((SELECT id FROM creature WHERE full_name = 'FAERIE_DRAGON'),8,8,12,7,7,8,null,2,2,2,2,8,8,8,null,null,null,12,12,12,2,2),
((SELECT id FROM creature WHERE full_name = 'RUST_DRAGON'),6,5,12,8,10,10,null,3,3,3,3,11,12,12,null,null,null,11,12,12,4,4),
((SELECT id FROM creature WHERE full_name = 'AZURE_DRAGON'),6,8,12,7,7,9,null,2,2,2,2,11,10,10,null,null,null,11,10,8,4,4),
((SELECT id FROM creature WHERE full_name = 'GOLD_GOLEM'),9,9,8,7,8,8,8,3,3,3,3,7,7,7,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'NOMAD'),7,7,12,9,8,10,null,3,3,3,3,9,9,9,null,null,null,null,null,null,1,1),
((SELECT id FROM creature WHERE full_name = 'PEASANT'),8,16,8,6,9,6,null,2,2,2,2,8,8,8,null,null,null,null,null,null,2,2),
------------------------------------------------------------------------------------------------------------------------------------------
((SELECT id FROM creature WHERE full_name = 'AMMO_CART'),null,null,1,10,10,10,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'BALLISTA'),null,null,1,10,10,9,null,null,null,null,null,null,null,null,9,9,9,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'CATAPULT'),null,null,1,8,8,9,null,null,null,null,null,null,null,null,13,13,13,null,null,null,null,null),
((SELECT id FROM creature WHERE full_name = 'FIRST_AID_TENT'),null,null,6,9,9,6,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null);