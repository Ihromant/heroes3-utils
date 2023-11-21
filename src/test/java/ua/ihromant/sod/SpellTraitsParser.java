package ua.ihromant.sod;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SpellTraitsParser {
    @Test
    public void parseCastleProbabilities() throws IOException {
        Map<String, Integer> idxes = IntStream.range(0, DESIRED_ORDER.length).boxed().collect(Collectors.toMap(i -> DESIRED_ORDER[i], Function.identity()));
        Map<String, List<Integer>> lines = IOUtils.readLines(Objects.requireNonNull(getClass().getResourceAsStream("/sptraits.txt")), Charset.defaultCharset())
                .stream().filter(l -> l.chars().filter(Character::isDigit).count() > 20).limit(DESIRED_ORDER.length)
                .sorted(Comparator.comparingInt(l -> idxes.get(extractName(l))))
                .map(l -> l.substring(l.lastIndexOf('x') + 1).trim())
                .collect(Collectors.toMap(this::extractName, l -> Arrays.stream(l.split("\\s"), 9, 18)
                        .map(Integer::parseInt).toList()));
        for (int i = 0; i < CASTLES.length; i++) {
            String castleName = CASTLES[i];
            System.out.println("INSERT INTO castle_spells (");
            System.out.println("id, castle_id, spell_id, probability");
            System.out.println(") values");
            for (int j = 0; j < DESIRED_ORDER.length; j++) {
                String spellName = DESIRED_ORDER[j];
                System.out.println((i == 0 ? "(COALESCE((SELECT MAX(id) FROM castle_spells), 0) + " + (j + 1)
                        : "((SELECT MAX(id) + " + (j + 1) + " FROM castle_spells)") +
                        ",(select id from castle where full_name = '" + castleName
                        + "'),(select id from spell where full_name = '" + spellName + "')," + lines.get(spellName).get(i) + ")"
                        + (j == DESIRED_ORDER.length - 1 ? ";" : ","));
            }
            System.out.println();
        }
    }

    private String extractName(String line) {
        String result = line.substring(line.indexOf('{') + 1, line.indexOf('}')).toLowerCase().replace(' ', '_')
                .replace("-", "").replace("'", "");
        if (result.contains("elemental")) {
            result = result.replace("summon_", "");
        }
        if (result.equals("land_mine")) {
            result = "land_mines";
        }
        return result;
    }

    private static String[] CASTLES = Arrays.stream(new String[] {"CASTLE", "RAMPART", "TOWER", "INFERNO", "NECROPOLIS", "DUNGEON", "STRONGHOLD", "FORTRESS", "CONFLUX"})
            .map(String::toLowerCase).toArray(String[]::new);

    private static final String[] DESIRED_ORDER = {"magic_arrow", "haste", "bloodlust", "protection_from_fire", "curse",
            "bless", "protection_from_water", "cure", "dispel", "stone_skin", "slow", "shield", "view_air", "summon_boat",
            "view_earth", "lightning_bolt", "precision", "protection_from_air", "disrupting_ray", "fortune", "fire_wall",
            "blind", "ice_bolt", "remove_obstacle", "weakness", "quicksand", "death_ripple", "visions", "disguise",
            "scuttle_boat", "hypnotize", "destroy_undead", "air_shield", "fireball", "land_mines", "misfortune",
            "forgetfulness", "frost_ring", "mirth", "teleport", "antimagic", "protection_from_earth", "earthquake",
            "animate_dead", "force_field", "counterstrike", "chain_lightning", "inferno", "armageddon", "frenzy",
            "berserk", "slayer", "fire_shield", "clone", "prayer", "resurrection", "meteor_shower", "sorrow", "water_walk",
            "town_portal", "titans_lightning_bolt", "magic_mirror", "air_elemental", "fire_elemental", "sacrifice", "water_elemental",
            "implosion", "earth_elemental", "fly", "dimension_door"};
}
