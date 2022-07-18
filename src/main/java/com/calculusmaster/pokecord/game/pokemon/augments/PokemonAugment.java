package com.calculusmaster.pokecord.game.pokemon.augments;

import com.calculusmaster.pokecord.util.enums.Prices;

public enum PokemonAugment
{
    //Basic Stat Boost Augments
    HP_BOOST_I      (1, "Health Boost I", "Increases HP by 5%"),
    ATK_BOOST_I     (1, "Attack Boost I", "Increases Attack by 5%"),
    DEF_BOOST_I     (1, "Defense Boost I", "Increases Defense by 5%"),
    SPATK_BOOST_I   (1, "Special Attack Boost I", "Increases Special Attack by 5%"),
    SPDEF_BOOST_I   (1, "Special Defense Boost I", "Increases Special Defense 5%"),
    SPD_BOOST_I     (1, "Speed Boost I", "Increases Speed by 5%"),

    HP_BOOST_II     (1, "Health Boost II", "Increases HP by 10%"),
    ATK_BOOST_II    (1, "Attack Boost II", "Increases Attack by 10%"),
    DEF_BOOST_II    (1, "Defense Boost II", "Increases Defense by 10%"),
    SPATK_BOOST_II  (1, "Special Attack Boost II", "Increases Special Attack by 10%"),
    SPDEF_BOOST_II  (1, "Special Defense Boost II", "Increases Special Defense 10%"),
    SPD_BOOST_II    (1, "Speed Boost II", "Increases Speed by 10%"),

    HP_BOOST_III    (1, "Health Boost III", "Increases HP by 20%"),
    ATK_BOOST_III   (1, "Attack Boost III", "Increases Attack by 20%"),
    DEF_BOOST_III   (1, "Defense Boost III", "Increases Defense by 20%"),
    SPATK_BOOST_III (1, "Special Attack Boost III", "Increases Special Attack by 20%"),
    SPDEF_BOOST_III (1, "Special Defense Boost III", "Increases Special Defense 20%"),
    SPD_BOOST_III   (1, "Speed Boost III", "Increases Speed by 20%"),

    //Unique Augments - Necrozma
    PRISMATIC_CONVERGENCE(2, "Prismatic Convergence", "Supercharges Prismatic Laser, dealing 30% extra damage, but lowering Speed by 2 stages on use."),
    REFRACTED_PRISMATIC_CONVERGENCE(2, "Ascendant Prismatic Convergence", "Supercharges Prismatic Laser, dealing 60% extra damage, but lowering Speed by 4 stages on use."),
    LIGHT_ABSORPTION(1, "Light Absorption", "During Harsh Sunlight, reduces damage taken from Special moves by 15%.")

    ;

    private final int slotCost;
    private final String augmentName;
    private final String description;
    PokemonAugment(int slotCost, String augmentName, String description)
    {
        this.slotCost = slotCost;
        this.augmentName = augmentName;
        this.description = description;
    }

    public int getSlotCost()
    {
        return this.slotCost;
    }

    public String getAugmentName()
    {
        return this.augmentName;
    }

    public String getAugmentID()
    {
        return this.toString();
    }

    public String getAugmentDescription()
    {
        return this.description;
    }

    public int getCreditCost()
    {
        int price = (int)(Prices.UNLOCK_AUGMENT_BASE.get() * Math.pow(1.25, this.getSlotCost() - 1));
        price -= price % 10;
        return price;
    }

    public static PokemonAugment fromID(String augmentID)
    {
        for(PokemonAugment augment : PokemonAugment.values()) if(augment.toString().equalsIgnoreCase(augmentID)) return augment;
        return null;
    }
}
