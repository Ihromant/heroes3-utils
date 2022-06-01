package ua.ihromant.sod;

import org.junit.jupiter.api.Test;
import ua.ihromant.sod.utils.map.SecondarySkill;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HeroStatsParser {
    @Test
    public void parse() {
        HeroType type = HeroType.ELEMENTALIST;
        List<String> lines = new BufferedReader(new InputStreamReader(new BufferedInputStream(getClass().getResourceAsStream("/stats.txt"))))
                .lines().collect(Collectors.toList());
//        String[] attack = lines.get(1).split("\\s+");
//        String[] defense = lines.get(2).split("\\s+");
//        String[] power = lines.get(3).split("\\s+");
//        String[] knowledge = lines.get(4).split("\\s+");
//        System.out.println("(" + type.ordinal() + ",'" + type.name() + "',(SELECT id FROM castle WHERE full_name = '" + type.getCastle().name() + "'),"
//                + type.isBook() + "," + type.isBook() + "," + attack[2] + "," + defense[2] + "," + power[2] + "," + knowledge[2] + ","
//                + dropPercent(attack[3]) + ", " + dropPercent(defense[3]) + ", " + dropPercent(power[3]) + ", " + dropPercent(knowledge[3]) + ", "
//                + dropPercent(attack[4]) + ", " + dropPercent(defense[4]) + ", " + dropPercent(power[4]) + ", " + dropPercent(knowledge[4]) + "),");
        System.out.println();
        lines.subList(7, lines.size()).forEach(line -> {
            String first = line.substring(0, line.indexOf(" small.gif"));
            String name = first.substring(first.indexOf(' ') + 1).replace(' ', '_').toUpperCase();
            if (name.equals("INTERFERENCE")) {
                return;
            }
            SecondarySkill skill = SecondarySkill.valueOf(name);
            String[] second = line.substring(line.indexOf(" small.gif")).split("\\s+");
            String frequence = second[second.length - 1];
            System.out.println("((SELECT id FROM hero_type WHERE full_name = '" + type + "'),"
            + "(SELECT id FROM secondary_skill WHERE full_name = '" + skill + "')," + frequence + "),");
        });
    }

    //private String dropPercent(String from) {
    //    return from.substring(0, from.length() - 1);
    //}

    @Test
    public void printSecondary() {
        Arrays.stream(SecondarySkill.values()).forEach(v -> IntStream.range(1, 4)
                .forEach(i -> System.out.println("((SELECT id FROM secondary_skill WHERE full_name = '" + v.name() + "'), " + i + ", NULL),")));
    }

    @Test
    public void printCastle() {
        Arrays.stream(Castle.values())
                .forEach(v -> System.out.println("(" + v.ordinal() + ", " + v.name() + ", (SELECT id FROM terrain_type WHERE full_name = '" + v.getBackground().name() + "')),"));
    }

    @Test
    public void parseHeroesList() {
        List<String> lines = new BufferedReader(new InputStreamReader(new BufferedInputStream(getClass().getResourceAsStream("/heroes.txt"))))
                .lines().collect(Collectors.toList());
        List<String> skills = new ArrayList<>();
        lines.forEach(line -> {
            String first = line.substring(0, line.indexOf(" small.gif"));
            HeroName hero = HeroName.valueOf(first.substring(0, first.indexOf(' ')).toUpperCase());
            String[] chunks = first.split("\\s+");
            int idx = 0;
            HeroType heroType;
            while ((heroType = tryParseType(chunks[idx])) == null) {
                idx++;
            }
            String specialty = Arrays.stream(chunks, idx + 2, chunks.length).collect(Collectors.joining("_"));
            SpecialityType type = parseType(specialty);
            String parsedSpecialty = parseValue(specialty);
            String speciality;
            switch (type) {
                case SECONDARY_SKILL:
                    speciality = "(SELECT id FROM secondary_skill WHERE full_name = '" + parsedSpecialty + "')";
                    break;
                case SPELL:
                    speciality = "(SELECT id FROM spell WHERE full_name = '" + parsedSpecialty + "')";
                    break;
                case GEMS:
                case SULFUR:
                case CRYSTAL:
                case MERCURY:
                    speciality = "1";
                    break;
                case GOLD:
                    speciality = "350";
                    break;
                case SPEED:
                    speciality = "2";
                    break;
                case BALLISTA:
                    speciality = "5";
                    break;
                default:
                    speciality = "(SELECT id FROM creature WHERE full_name = '" + parsedSpecialty + "')";
            }
            int index = line.indexOf("small.png");
            Spell spell = index == -1 ? null : Spell.valueOf(line.substring(index + 10).replace(' ', '_').toUpperCase());
            System.out.println("(" + hero.ordinal() + ",'" + hero.name() + "',(SELECT id FROM hero_type WHERE full_name ='" + heroType + "'),true,"
                    + type.ordinal() + "," + speciality + ","
                    + (spell == null ? null : "(SELECT id FROM spell WHERE full_name = '" + spell + "')") + "),");
            String[] skillz = line.substring(line.indexOf("small.gif") + 10).split("\\s+");
            SkillLevel skillLevel;
            idx = 0;
            while ((skillLevel = tryParseLevel(skillz[idx])) == null) {
                idx++;
            }
            int idx1 = idx + 1;
            while (!skillLevel.name().equalsIgnoreCase(skillz[idx1])) {
                idx1++;
            }
            SecondarySkill skill = SecondarySkill.valueOf(Arrays.stream(skillz, idx + 1, idx1).collect(Collectors.joining("_")).toUpperCase());
            skills.add("((SELECT id FROM hero WHERE full_name = '" + hero + "')," + skillLevel.ordinal() + "," + "(SELECT id FROM secondary_skill WHERE full_name = '" + skill + "')),");
            idx = idx1 + 1;
            while (idx < skillz.length && (skillLevel = tryParseLevel(skillz[idx])) == null) {
                idx++;
            }
            if (idx < skillz.length) {
                idx1 = idx + 1;
                while (!skillLevel.name().equalsIgnoreCase(skillz[idx1])) {
                    idx1++;
                }
                skill = SecondarySkill.valueOf(Arrays.stream(skillz, idx + 1, idx1).collect(Collectors.joining("_")).toUpperCase());
                skills.add("((SELECT id FROM hero WHERE full_name = '" + hero + "')," + skillLevel.ordinal() + "," + "(SELECT id FROM secondary_skill WHERE full_name = '" + skill + "')),");
            }
        });
        System.out.println();
        skills.forEach(System.out::println);
    }

    private SkillLevel tryParseLevel(String level) {
        try {
            return SkillLevel.valueOf(level.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private SpecialityType parseType(String specialty) {
        try {
            SecondarySkill.valueOf(specialty.toUpperCase());
            return SpecialityType.SECONDARY_SKILL;
        } catch (Exception ignored) {}
        try {
            Spell.valueOf(specialty.toUpperCase());
            return SpecialityType.SPELL;
        } catch (Exception ignored) {}
        try {
            if (specialty.endsWith("s")) {
                Creature.valueOf(specialty.substring(0, specialty.length() - 1).toUpperCase());
                return SpecialityType.DEFAULT_CREATURE;
            }
            Creature.valueOf(specialty.toUpperCase());
            return SpecialityType.DEFAULT_CREATURE;
        } catch (Exception ignored) {}
        try {
            return SpecialityType.valueOf(specialty.toUpperCase());
        } catch (Exception ignored) {}
        return SpecialityType.DEFAULT_CREATURE;
    }

    private String parseValue(String specialty) {
        try {
            return String.valueOf(SecondarySkill.valueOf(specialty.toUpperCase()));
        } catch (Exception ignored) {}
        try {
            return String.valueOf(Spell.valueOf(specialty.toUpperCase()));
        } catch (Exception ignored) {}
        try {
            if (specialty.endsWith("s")) {
                return String.valueOf(Creature.valueOf(specialty.substring(0, specialty.length() - 1).toUpperCase()));
            }
            return String.valueOf(Creature.valueOf(specialty.toUpperCase()));
        } catch (Exception ignored) {}
        try {
            return SpecialityType.valueOf(specialty.toUpperCase()).name();
        } catch (Exception ignored) {}
        return "'" + specialty + "'";
    }

    private HeroType tryParseType(String type) {
        try {
            return HeroType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }
}
