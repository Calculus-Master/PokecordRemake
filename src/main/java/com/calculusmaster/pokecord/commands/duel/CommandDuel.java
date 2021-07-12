package com.calculusmaster.pokecord.commands.duel;

import com.calculusmaster.pokecord.commands.Command;
import com.calculusmaster.pokecord.commands.CommandInvalid;
import com.calculusmaster.pokecord.commands.pokemon.CommandTeam;
import com.calculusmaster.pokecord.game.duel.Duel;
import com.calculusmaster.pokecord.game.duel.DuelHelper;
import com.calculusmaster.pokecord.mongo.PlayerDataQuery;
import com.calculusmaster.pokecord.util.Mongo;
import com.calculusmaster.pokecord.util.PokemonRarity;
import com.calculusmaster.pokecord.util.helpers.LoggerHelper;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandDuel extends Command
{
    public CommandDuel(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public Command runCommand()
    {
        if(this.msg.length == 1)
        {
            this.embed.setDescription(CommandInvalid.getShort());
            return this;
        }
        else if(!this.serverData.getDuelChannels().isEmpty() && !this.serverData.getDuelChannels().contains(this.event.getChannel().getId()))
        {
            this.sendMsg("Duels are not allowed in this channel!");
            return this;
        }
        else if(this.msg.length >= 3 && (!isNumeric(2) || this.getInt(2) > CommandTeam.MAX_TEAM_SIZE || this.getInt(2) > this.playerData.getTeam().size()))
        {
            this.sendMsg("Error with size. Either it isn't a number, larger than the max of " + CommandTeam.MAX_TEAM_SIZE + ", or larger than your team's size!");
            return this;
        }

        boolean checkTeam = !Arrays.asList(this.msg).contains("--nolimit");

        boolean accept = this.msg[1].equals("accept");
        boolean deny = this.msg[1].equals("deny") || this.msg[1].equals("cancel");

        if(DuelHelper.isInDuel(this.player.getId()) && (accept || deny))
        {
            if(accept)
            {
                Duel d = DuelHelper.instance(this.player.getId());

                if(d.getSize() > this.playerData.getTeam().size())
                {
                    this.sendMsg("Your team needs to contain at least " + d.getSize() + " Pokemon to participate! Deleting duel request!");

                    DuelHelper.delete(this.player.getId());
                }
                else if(checkTeam && this.isInvalidTeam(d.getSize()))
                {
                    this.createInvalidTeamEmbed(d.getSize());
                }
                else
                {
                    d.sendTurnEmbed();
                    this.embed = null;
                }
            }
            else
            {
                DuelHelper.delete(this.player.getId());
                this.embed.setDescription(this.player.getName() + " denied the duel request!");
            }

            return this;
        }

        int size = 3;

        if(this.msg.length >= 3) size = this.getInt(2);

        //Player wants to start a duel with the mention, check all necessary things

        if(DuelHelper.isInDuel(this.player.getId()))
        {
            this.sendMsg("You are already in a duel! You cannot start another one until your current duel is complete!");
            return this;
        }

        if(this.mentions.size() == 0)
        {
            this.embed.setDescription("You need to mention someone to duel them!");
            return this;
        }

        String opponentID = this.mentions.get(0).getId();
        Member opponent = this.getMember(opponentID);

        if(!PlayerDataQuery.isRegistered(opponentID))
        {
            this.sendMsg(opponent.getEffectiveName() + " is not registered! Use p!start <starter> to begin!");
        }
        else if(new PlayerDataQuery(opponentID).getTeam().isEmpty())
        {
            this.sendMsg(opponent.getEffectiveName() + " needs to create a Pokemon team!");
        }
        else if(DuelHelper.isInDuel(opponentID))
        {
            this.sendMsg(opponent.getEffectiveName() + " is already in a Duel!");
        }
        else if(this.player.getId().equals(opponentID))
        {
            this.sendMsg("You cannot duel yourself!");
        }
        else if(checkTeam && this.isInvalidTeam(size))
        {
            this.createInvalidTeamEmbed(size);
        }
        else
        {
            Duel d = Duel.create(this.player.getId(), opponentID, size, this.event);

            LoggerHelper.info(CommandDuel.class, "Duel Created! " + d);

            this.event.getChannel().sendMessage("<@" + opponentID + "> ! " + this.player.getName() + " has challenged you to a duel! Type `p!duel accept` to accept!").queue();
            this.embed = null;
        }

        return this;
    }

    private void createInvalidTeamEmbed(int size)
    {
        int legendaryCap = this.getLegendaryCap(size);
        int mythUBCap = this.getMythicalUBCap(size);

        this.embed = new EmbedBuilder();
        this.embed.setDescription("Your Team is Invalid! Check the limits below.");
        this.embed.setTitle("Invalid Team!");
        this.embed
                .addField("Legendary Limit", "Maximum: " + legendaryCap, false)
                .addField("Mythical and Ultra Beast Limit", "Maximum: " + mythUBCap, false);
        this.embed.setFooter("Check your team with `p!team` to see how many Pokemon of each kind listed above you have!");
    }

    private int getLegendaryCap(int size)
    {
        return 1 + (size - 1) / 4;
    }

    private int getMythicalUBCap(int size)
    {
        return 2 + (size - 1) / 4;
    }

    public boolean isInvalidTeam(int size)
    {
        if(size < 3) return false;

        int legendary = 0;
        int mythical = 0;
        int ub = 0;

        String name;
        List<String> team = this.playerData.getTeam();

        for(int i = 0; i < this.playerData.getTeam().size(); i++)
        {
            name = Mongo.PokemonData.find(Filters.eq("UUID", team.get(i))).first().getString("name");

            if(PokemonRarity.LEGENDARY.contains(name)) legendary++;
            if(PokemonRarity.MYTHICAL.contains(name)) mythical++;
            if(PokemonRarity.ULTRA_BEAST.contains(name)) ub++;
        }

        return legendary > this.getLegendaryCap(size) || (mythical + ub) > this.getMythicalUBCap(size);
    }
}
