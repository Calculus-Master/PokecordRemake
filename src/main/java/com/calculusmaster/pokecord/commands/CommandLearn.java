package com.calculusmaster.pokecord.commands;

import com.calculusmaster.pokecord.game.Pokemon;
import com.calculusmaster.pokecord.util.Global;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandLearn extends Command
{
    public static Map<String, String> moveLearnRequests = new HashMap<>();

    public CommandLearn(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg, "learn <move>");
    }

    @Override
    public Command runCommand()
    {
        if(this.msg.length < 2)
        {
            this.embed.setDescription(CommandInvalid.getShort());
            return this;
        }

        Pokemon selected = this.playerData.getSelectedPokemon();
        String move = Global.normalCase(this.getMove());

        if(!selected.getAvailableMoves().contains(move))
        {
            this.embed.setDescription(selected.getName() + " does not know `" + move + "`");
        }
        else
        {
            StringBuilder movesList = new StringBuilder().append("\n");
            for(int i = 0; i < 4; i++) movesList.append((i + 1) + ": " + selected.getLearnedMoves().get(i) + "\n");

            this.embed.setDescription("Specify a move to replace with " + move + movesList.toString());
            moveLearnRequests.put(selected.getUUID(), move);
        }

        return this;
    }

    private String getMove()
    {
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < this.msg.length; i++) sb.append(this.msg[i] + " ");
        return sb.toString().trim();
    }
}
