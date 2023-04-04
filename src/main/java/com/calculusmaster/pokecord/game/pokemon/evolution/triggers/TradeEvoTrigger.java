package com.calculusmaster.pokecord.game.pokemon.evolution.triggers;

import com.calculusmaster.pokecord.game.pokemon.Pokemon;

public class TradeEvoTrigger implements EvolutionTrigger
{
    public TradeEvoTrigger() {}

    @Override
    public boolean canEvolve(Pokemon p, String serverID)
    {
        return false;
    }

    @Override
    public String getDescription()
    {
        return "Traded";
    }
}
