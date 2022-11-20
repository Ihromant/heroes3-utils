package ua.ihromant.sod.utils.entities;

public enum Control {
    NONE, COMPUTER, HUMAN;

    public static Control of(boolean canBeHuman, boolean canBeComputer) {
        return canBeComputer ? canBeHuman ? HUMAN : COMPUTER : NONE;
    }
}
