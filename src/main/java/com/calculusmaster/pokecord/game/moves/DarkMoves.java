package com.calculusmaster.pokecord.game.moves;

import com.calculusmaster.pokecord.game.Duel;
import com.calculusmaster.pokecord.game.Move;
import com.calculusmaster.pokecord.game.Pokemon;
import com.calculusmaster.pokecord.game.enums.elements.StatusCondition;

import java.util.Random;

public class DarkMoves
{
    public String Bite(Pokemon user, Pokemon opponent, Duel duel, Move move)
    {
        return Move.statusDamageMove(user, opponent, duel, move, StatusCondition.FLINCHED, 30);
    }

    public String Pursuit(Pokemon user, Pokemon opponent, Duel duel, Move move)
    {
        return Move.simpleDamageMove(user, opponent, duel, move);
    }

    public String Assurance(Pokemon user, Pokemon opponent, Duel duel, Move move)
    {
        if(duel.lastDamage > 0) move.setPower(move.getPower() * 2);

        return Move.simpleDamageMove(user, opponent, duel, move);
    }

    public String Taunt(Pokemon user, Pokemon opponent, Duel duel, Move move)
    {
        return opponent.getName() + " is now unable to use Status moves for 3 turns!";
    }
}
