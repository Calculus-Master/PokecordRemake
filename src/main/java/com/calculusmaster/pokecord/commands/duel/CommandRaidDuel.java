package com.calculusmaster.pokecord.commands.duel;

import com.calculusmaster.pokecord.commands.Command;
import com.calculusmaster.pokecord.game.player.level.PlayerLevel;
import com.calculusmaster.pokecord.util.helpers.event.RaidEventHelper;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandRaidDuel extends Command
{
    public CommandRaidDuel(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public Command runCommand()
    {
        boolean join = this.msg.length == 2 && this.msg[1].equals("join");
        boolean leave = this.msg.length == 2 && this.msg[1].equals("leave");

        if(join || leave)
        {
            if(!RaidEventHelper.hasRaid(this.server.getId())) this.response = "There are no active Raid duels in this server!";
            else if(this.playerData.getLevel() < PlayerLevel.REQUIRED_LEVEL_RAID) this.invalidMasteryLevel(PlayerLevel.REQUIRED_LEVEL_RAID, "to participate in Raids");
            else if(join && RaidEventHelper.isPlayerReady(this.server.getId(), this.player.getId())) this.response = "You are already ready for this server's active Raid duel! To leave, type `p!raidduel leave`.";
            else if(leave && !RaidEventHelper.isPlayerReady(this.server.getId(), this.player.getId())) this.response = "You are not in this server's active Raid duel! To join, type `p!raidduel join`.";
            else
            {
                if(join)
                {
                    RaidEventHelper.getRaid(this.server.getId()).setEvent(this.event);
                    RaidEventHelper.getRaid(this.server.getId()).addPlayer(this.player.getId());

                    this.response = "You have joined the Raid with " + this.playerData.getSelectedPokemon().getName() + "!";
                }
                else if(leave)
                {
                    RaidEventHelper.getRaid(this.server.getId()).removePlayer(this.player.getId());

                    this.response = "You have left the Raid!";
                }
            }
        }
        else this.invalid();

        return this;
    }
}
