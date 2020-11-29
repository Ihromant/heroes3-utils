CREATE TABLE public.creature_casts (
	id serial primary key,
	specialty_id int4 not null,
	spell_id int4 not null,
	lvl int4 not null,
	probability int4 not null,
	base_effect int4 not null,
	power_coeff int4 not null
);

--drop table public.creature_casts;

INSERT INTO public.creature_casts (
    spell_id, lvl, probability, base_effect, power_coeff, specialty_id
) values
((select id from spell where full_name = 'RESURRECTION'), 2, 1, 0, 2, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ARCHANGEL') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'BLESS'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'BLOODLUST'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'CURE'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'HASTE'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'SHIELD'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'STONE_SKIN'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'FORTUNE'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'PRECISION'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'AIR_SHIELD'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'ANTIMAGIC'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'MIRTH'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'COUNTERSTRIKE'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'FIRE_SHIELD'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'FRENZY'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'PRAYER'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'SLAYER'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'MAGIC_MIRROR'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MASTER_GENIE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'BLOODLUST'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'OGRE_MAGE') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'PROTECTION_FROM_AIR'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'STORM_ELEMENTAL') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'PROTECTION_FROM_WATER'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ICE_ELEMENTAL') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'PROTECTION_FROM_FIRE'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENERGY_ELEMENTAL') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'PROTECTION_FROM_EARTH'), 2, 1, 0, 6, (select id from creature_feature where creature_id = (select id from creature where full_name = 'MAGMA_ELEMENTAL') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'MAGIC_ARROW'), 2, 10, 20, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'ICE_BOLT'), 2, 22, 20, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'LIGHTNING_BOLT'), 2, 22, 20, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'FIREBALL'), 2, 21, 30, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'FROST_RING'), 2, 10, 30, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'INFERNO'), 2, 5, 40, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'METEOR_SHOWER'), 2, 5, 50, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'CHAIN_LIGHTNING'), 2, 5, 50, 5, (select id from creature_feature where creature_id = (select id from creature where full_name = 'FAERIE_DRAGON') and full_name = 'SPELLCASTER')),
((select id from spell where full_name = 'HASTE'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'BLESS'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'STONE_SKIN'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'CURE'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'BLOODLUST'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'SLOW'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'WEAKNESS'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER')),
((select id from spell where full_name = 'AIR_SHIELD'), 3, 1, 0, 3, (select id from creature_feature where creature_id = (select id from creature where full_name = 'ENCHANTER') and full_name = 'ROUND_SPELLCASTER'));