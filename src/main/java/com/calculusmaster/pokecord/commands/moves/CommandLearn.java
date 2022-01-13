package com.calculusmaster.pokecord.commands.moves;

import com.calculusmaster.pokecord.commands.Command;
import com.calculusmaster.pokecord.commands.CommandInvalid;
import com.calculusmaster.pokecord.game.enums.elements.Feature;
import com.calculusmaster.pokecord.game.moves.Move;
import com.calculusmaster.pokecord.game.pokemon.Pokemon;
import com.calculusmaster.pokecord.util.Global;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandLearn extends Command
{
    public static Map<String, String> moveLearnRequests = new HashMap<>();

    public CommandLearn(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public Command runCommand()
    {
        if(this.insufficientMasteryLevel(Feature.LEARN_REPLACE_MOVES)) return this.invalidMasteryLevel(Feature.LEARN_REPLACE_MOVES);

        if(this.msg.length < 2)
        {
            this.response = CommandInvalid.getShort();
            return this;
        }

        Pokemon selected = this.playerData.getSelectedPokemon();
        String move = Global.normalize(this.getMultiWordContent(1));

        if(!Move.isMove(move)) this.response = "Invalid move name!";
        else if(!Move.isImplemented(move)) this.response = "`" + move + "` has not been implemented yet!";
        else if(!selected.availableMoves().contains(move)) this.response = selected.getName() + " does not know `" + move + "`";
        else
        {
            StringBuilder movesList = new StringBuilder().append("\n");
            for(int i = 0; i < 4; i++) movesList.append(i + 1).append(": ").append(selected.getMoves().get(i)).append("\n");

            this.embed.setDescription("Which move do you want to replace with " + move + "?" + movesList);
            moveLearnRequests.put(selected.getUUID(), move);
        }

        return this;
    }
}
