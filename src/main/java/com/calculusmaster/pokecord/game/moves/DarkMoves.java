package com.calculusmaster.pokecord.game.moves;

import com.calculusmaster.pokecord.game.Duel;
import com.calculusmaster.pokecord.game.Move;
import com.calculusmaster.pokecord.game.Pokemon;

import java.util.Random;

public class DarkMoves
{
    public String Bite(Pokemon user, Pokemon opponent, Duel duel, Move move)
    {
        int damage = move.getDamage(user, opponent);
        opponent.damage(damage);

        if(new Random().nextInt(100) < 30)
        {
            opponent.setFlinched(true);
            return move.getDamageResult(opponent, damage) + " " + opponent.getName() + " flinched!";
        }

        return move.getDamageResult(opponent, damage);
    }
}
