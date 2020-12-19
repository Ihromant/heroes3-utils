CREATE TABLE public.creature (
	id int4 NOT null primary key,
	full_name varchar not NULL,
	castle_id int4,
	lvl int2 not null,
	upgrade boolean not null,
	attack int2 not null,
	defense int2 not null,
	damage_low int4 not null,
	damage_high int4 not null,
	hit_points int4 not null,
	speed int2 not null,
	growth int4 not null,
	ai_value int4 not null,
	gold_cost int4 not null,
	mercury_cost int2,
	crystal_cost int2,
	sulfur_cost int2,
	gem_cost int2
);

-- drop table public.creature;

INSERT INTO public.creature (
    id, full_name, castle_id, lvl, upgrade, attack, defense, damage_low, damage_high, hit_points,
	speed, growth, ai_value, gold_cost
) values
(0,'PIKEMAN',0,1,false,4,5,1,3,10,4,14,80,60),
(1,'HALBERDIER',0,1,true,6,5,2,3,10,5,14,115,75),
(2,'ARCHER',0,2,false,6,3,2,3,10,4,9,126,100),
(3,'MARKSMAN',0,2,true,6,3,2,3,10,6,9,184,150),
(4,'GRIFFIN',0,3,false,8,8,3,6,25,6,7,351,200),
(5,'ROYAL_GRIFFIN',0,3,true,9,9,3,6,25,9,7,448,240),
(6,'SWORDSMAN',0,4,false,10,12,6,9,35,5,4,445,300),
(7,'CRUSADER',0,4,true,12,12,7,10,35,6,4,588,400),
(8,'MONK',0,5,false,12,7,10,12,30,5,3,485,400),
(9,'ZEALOT',0,5,true,12,10,10,12,30,7,3,750,450),
(10,'CAVALIER',0,6,false,15,15,15,25,100,7,2,1946,1000),
(11,'CHAMPION',0,6,true,16,16,20,25,100,9,2,2100,1200),
(12,'ANGEL',0,7,false,20,20,50,50,200,12,1,5019,3000),
(13,'ARCHANGEL',0,7,true,30,30,50,50,250,18,1,8776,5000),
(14,'CENTAUR',1,1,false,5,3,2,3,8,6,14,100,70),
(15,'CENTAUR_CAPTAIN',1,1,true,6,3,2,3,10,8,14,138,90),
(16,'DWARF',1,2,false,6,7,2,4,20,3,8,138,120),
(17,'BATTLE_DWARF',1,2,true,7,7,2,4,20,5,8,209,150),
(18,'WOOD_ELF',1,3,false,9,5,3,5,15,6,7,234,200),
(19,'GRAND_ELF',1,3,true,9,5,3,5,15,7,7,331,225),
(20,'PEGASUS',1,4,false,9,8,5,9,30,8,5,518,250),
(21,'SILVER_PEGASUS',1,4,true,9,10,5,9,30,12,5,532,275),
(22,'DENDROID_GUARD',1,5,false,9,12,10,14,55,3,3,517,350),
(23,'DENDROID_SOLDIER',1,5,true,9,12,10,14,65,4,3,803,425),
(24,'UNICORN',1,6,false,15,14,18,22,90,7,2,1806,850),
(25,'WAR_UNICORN',1,6,true,15,14,18,22,110,9,2,2030,950),
(26,'GREEN_DRAGON',1,7,false,18,18,40,50,180,10,1,4872,2400),
(27,'GOLD_DRAGON',1,7,true,27,27,40,50,250,16,1,8613,4000),
(28,'GREMLIN',2,1,false,3,3,1,2,4,4,16,44,30),
(29,'MASTER_GREMLIN',2,1,true,4,4,1,2,4,5,16,66,40),
(30,'STONE_GARGOYLE',2,2,false,6,6,2,3,16,6,9,165,130),
(31,'OBSIDIAN_GARGOYLE',2,2,true,7,7,2,3,16,9,9,201,160),
(32,'STONE_GOLEM',2,3,false,7,10,4,5,30,3,6,250,150),
(33,'IRON_GOLEM',2,3,true,9,10,4,5,35,5,6,412,200),
(34,'MAGE',2,4,false,11,8,7,9,25,5,4,570,350),
(35,'ARCH_MAGE',2,4,true,12,9,7,9,30,7,4,680,450),
(36,'GENIE',2,5,false,12,12,13,16,40,7,3,884,550),
(37,'MASTER_GENIE',2,5,true,12,12,13,16,40,11,3,942,600),
(38,'NAGA',2,6,false,16,13,20,20,110,5,2,2016,1100),
(39,'NAGA_QUEEN',2,6,true,16,13,30,30,110,7,2,2840,1600),
(40,'GIANT',2,7,false,19,16,40,60,150,7,1,3718,2000),
(41,'TITAN',2,7,true,24,24,40,60,300,11,1,7500,5000),
(42,'IMP',3,1,false,2,3,1,2,4,5,15,50,50),
(43,'FAMILIAR',3,1,true,4,4,1,2,4,7,15,60,60),
(44,'GOG',3,2,false,6,4,2,4,13,4,8,159,125),
(45,'MAGOG',3,2,true,7,4,2,4,13,6,8,240,175),
(46,'HELL_HOUND',3,3,false,10,6,2,7,25,7,5,357,200),
(47,'CERBERUS',3,3,true,10,8,2,7,25,8,5,392,250),
(48,'DEMON',3,4,false,10,10,7,9,35,5,4,445,250),
(49,'HORNED_DEMON',3,4,true,10,10,7,9,40,6,4,480,270),
(50,'PIT_FIEND',3,5,false,13,13,13,17,45,6,3,765,500),
(51,'PIT_LORD',3,5,true,13,13,13,17,45,7,3,1224,700),
(52,'EFREET',3,6,false,16,12,16,24,90,9,2,1670,900),
(53,'EFREET_SULTAN',3,6,true,16,14,16,24,90,13,2,2343,1100),
(54,'DEVIL',3,7,false,19,21,30,40,160,11,1,5101,2700),
(55,'ARCH_DEVIL',3,7,true,26,28,30,40,200,17,1,7115,4500),
(56,'SKELETON',4,1,false,5,4,1,3,6,4,12,60,60),
(57,'SKELETON_WARRIOR',4,1,true,6,6,1,3,6,5,12,85,70),
(58,'WALKING_DEAD',4,2,false,5,5,2,3,15,3,8,98,100),
(59,'ZOMBIE',4,2,true,5,5,2,3,20,4,8,128,125),
(60,'WIGHT',4,3,false,7,7,3,5,18,5,7,252,200),
(61,'WRAITH',4,3,true,7,7,3,5,18,7,7,315,230),
(62,'VAMPIRE',4,4,false,10,9,5,8,30,6,4,555,360),
(63,'VAMPIRE_LORD',4,4,true,10,10,5,8,40,9,4,783,500),
(64,'LICH',4,5,false,13,10,11,13,30,6,3,848,550),
(65,'POWER_LICH',4,5,true,13,10,11,15,40,7,3,1079,600),
(66,'BLACK_KNIGHT',4,6,false,16,16,15,30,120,7,2,2087,1200),
(67,'DREAD_KNIGHT',4,6,true,18,18,15,30,120,9,2,2382,1500),
(68,'BONE_DRAGON',4,7,false,17,15,25,50,150,9,1,3388,1800),
(69,'GHOST_DRAGON',4,7,true,19,17,25,50,200,14,1,4696,3000),
(70,'TROGLODYTE',5,1,false,4,3,1,3,5,4,14,59,50),
(71,'INFERNAL_TROGLODYTE',5,1,true,5,4,1,3,6,5,14,84,65),
(72,'HARPY',5,2,false,6,5,1,4,14,6,8,154,130),
(73,'HARPY_HAG',5,2,true,6,6,1,4,14,9,8,238,170),
(74,'BEHOLDER',5,3,false,9,7,3,5,22,5,7,336,250),
(75,'EVIL_EYE',5,3,true,10,8,3,5,22,7,7,367,280),
(76,'MEDUSA',5,4,false,9,9,6,8,25,5,4,517,300),
(77,'MEDUSA_QUEEN',5,4,true,10,10,6,8,30,6,4,577,330),
(78,'MINOTAUR',5,5,false,14,12,12,20,50,6,3,835,500),
(79,'MINOTAUR_KING',5,5,true,15,15,12,20,50,8,3,1068,575),
(80,'MANTICORE',5,6,false,15,13,14,20,80,7,2,1547,850),
(81,'SCORPICORE',5,6,true,16,14,14,20,80,11,2,1589,1050),
(82,'RED_DRAGON',5,7,false,19,19,40,50,180,11,1,4702,2500),
(83,'BLACK_DRAGON',5,7,true,25,25,40,50,300,15,1,8721,4000),
(84,'GOBLIN',6,1,false,4,2,1,2,5,5,15,60,40),
(85,'HOBGOBLIN',6,1,true,5,3,1,2,5,7,15,78,50),
(86,'WOLF_RIDER',6,2,false,7,5,2,4,10,6,9,130,100),
(87,'WOLF_RAIDER',6,2,true,8,5,3,4,10,8,9,203,140),
(88,'ORC',6,3,false,8,4,2,5,15,4,7,192,150),
(89,'ORC_CHIEFTAIN',6,3,true,8,4,2,5,20,5,7,240,165),
(90,'OGRE',6,4,false,13,7,6,12,40,4,4,416,300),
(91,'OGRE_MAGE',6,4,true,13,7,6,12,60,5,4,672,400),
(92,'ROC',6,5,false,13,11,11,15,60,7,3,1027,600),
(93,'THUNDERBIRD',6,5,true,13,11,11,15,60,11,3,1106,700),
(94,'CYCLOPS',6,6,false,15,12,16,20,70,6,2,1266,750),
(95,'CYCLOPS_KING',6,6,true,17,13,16,20,70,8,2,1443,1100),
(96,'BEHEMOTH',6,7,false,17,17,30,50,160,6,1,3162,1500),
(97,'ANCIENT_BEHEMOTH',6,7,true,19,19,30,50,300,9,1,6168,3000),
(98,'GNOLL',7,1,false,3,5,2,3,6,4,12,56,50),
(99,'GNOLL_MARAUDER',7,1,true,4,6,2,3,6,5,12,90,70),
(100,'LIZARDMAN',7,2,false,5,6,2,3,14,4,9,126,110),
(101,'LIZARD_WARRIOR',7,2,true,6,8,2,5,15,5,9,156,140),
(102,'SERPENT_FLY',7,3,false,7,9,2,5,20,9,8,268,220),
(103,'DRAGON_FLY',7,3,true,8,10,2,5,20,13,8,312,240),
(104,'BASILISK',7,4,false,11,11,6,10,35,5,4,552,325),
(105,'GREATER_BASILISK',7,4,true,12,12,6,10,40,7,4,714,400),
(106,'GORGON',7,5,false,10,14,12,16,70,5,3,890,525),
(107,'MIGHTY_GORGON',7,5,true,11,16,12,16,70,6,3,1028,600),
(108,'WYVERN',7,6,false,14,14,14,18,70,7,2,1350,800),
(109,'WYVERN_MONARCH',7,6,true,14,14,18,22,70,11,2,1518,1100),
(110,'HYDRA',7,7,false,16,18,25,45,175,5,1,4120,2200),
(111,'CHAOS_HYDRA',7,7,true,18,20,25,45,250,7,1,5931,3500),
(112,'PIXIE',8,1,false,2,2,1,2,3,7,20,55,25),
(113,'SPRITE',8,1,true,2,2,1,3,3,9,20,95,30),
(114,'AIR_ELEMENTAL',8,2,false,9,9,2,8,25,7,6,356,250),
(115,'STORM_ELEMENTAL',8,2,true,9,9,2,8,25,8,6,486,275),
(116,'WATER_ELEMENTAL',8,3,false,8,10,3,7,30,5,6,315,300),
(117,'ICE_ELEMENTAL',8,3,true,8,10,3,7,30,6,6,380,375),
(118,'FIRE_ELEMENTAL',8,4,false,10,8,4,6,35,6,5,345,350),
(119,'ENERGY_ELEMENTAL',8,4,true,12,8,4,6,35,8,5,470,400),
(120,'EARTH_ELEMENTAL',8,5,false,10,10,4,8,40,4,4,330,400),
(121,'MAGMA_ELEMENTAL',8,5,true,11,11,6,10,40,6,4,490,500),
(122,'PSYCHIC_ELEMENTAL',8,6,false,15,13,10,20,75,7,2,1669,950),
(123,'MAGIC_ELEMENTAL',8,6,true,15,13,15,25,80,9,2,2012,1200),
(124,'FIREBIRD',8,7,false,18,18,30,40,150,15,2,4336,2000),
(125,'PHOENIX',8,7,true,21,18,30,40,200,21,2,6721,3000),
--------------------------------------------------------------------------
(126,'PEASANT',NULL,1,false,1,1,1,1,1,3,25,15,10),
(127,'HALFLING',NULL,1,false,4,2,1,3,4,5,15,75,40),
(128,'ROGUE',NULL,2,false,8,3,2,4,10,6,8,135,100),
(129,'BOAR',NULL,2,false,6,5,2,3,15,6,8,145,150),
(130,'NOMAD',NULL,3,false,9,8,2,6,30,7,7,345,200),
(131,'MUMMY',NULL,3,false,7,7,3,5,30,5,7,270,300),
(132,'SHARPSHOOTER',NULL,4,false,12,10,8,10,15,9,4,585,400),
(133,'TROLL',NULL,5,false,14,7,10,15,40,7,3,1024,500),
(134,'GOLD_GOLEM',NULL,5,false,11,12,8,10,50,5,3,600,500),
(135,'DIAMOND_GOLEM',NULL,6,false,13,12,10,14,60,5,2,775,750),
(136,'ENCHANTER',NULL,6,false,17,12,14,14,30,9,2,1210,750),
(137,'FAERIE_DRAGON',NULL,7,false,20,20,20,30,500,15,1,19580,10000),
(138,'RUST_DRAGON',NULL,7,false,30,30,50,50,750,17,1,26433,15000),
(139,'CRYSTAL_DRAGON',NULL,7,false,40,40,60,75,800,16,1,39338,20000),
(140,'AZURE_DRAGON',NULL,7,false,50,50,70,80,1000,19,1,78845,30000);

alter table creature
add column upgraded int4;

update creature
set upgraded = (id + 1)
where (id % 2 = 0) and id < 126;