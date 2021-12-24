package com.calculusmaster.pokecord.commands.duel;

import com.calculusmaster.pokecord.commands.Command;
import com.calculusmaster.pokecord.game.duel.Duel;
import com.calculusmaster.pokecord.game.duel.core.DuelHelper;
import com.calculusmaster.pokecord.game.duel.extension.TrainerDuel;
import com.calculusmaster.pokecord.game.duel.players.Trainer;
import com.calculusmaster.pokecord.game.player.level.PlayerLevel;
import com.calculusmaster.pokecord.game.pokemon.PokemonRarity;
import com.calculusmaster.pokecord.util.Mongo;
import com.mongodb.client.model.Filters;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class CommandTrainerDuel extends Command
{
    public CommandTrainerDuel(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public Command runCommand()
    {
        boolean regular = this.msg.length == 2 && this.isNumeric(1) && this.getInt(1) >= 1 && this.getInt(1) <= Trainer.DAILY_TRAINERS.size();
        boolean elite = this.msg.length == 2 && this.msg[1].equals("elite");

        if(this.msg.length == 2 && (elite || regular))
        {
            if(DuelHelper.isInDuel(this.player.getId()))
            {
                this.response = "You are already in a duel!";
                return this;
            }

            Trainer.TrainerInfo trainer = elite ? Trainer.createElite() : Trainer.DAILY_TRAINERS.get(this.getInt(1) - 1);

            if(elite && this.playerData.getLevel() < PlayerLevel.REQUIRED_LEVEL_ELITE) this.invalidMasteryLevel(PlayerLevel.REQUIRED_LEVEL_ELITE, "to duel an Elite Trainer");

            if(this.playerData.getTeam().size() < trainer.pokemon.size())
            {
                this.response = "Your team is too small! It needs to be of size " + trainer.pokemon.size() + "!";
                return this;
            }

            if(!trainer.elite && this.isInvalidTeam(trainer.pokemon.size()))
            {
                this.response = "Your team is invalid! You can have a maximum of " + this.getLegendaryCap(trainer.pokemon.size()) + " legendaries and " + this.getMythicalUBCap(trainer.pokemon.size()) + " mythicals/ultra beasts!";
                return this;
            }

            Duel d = TrainerDuel.create(this.player.getId(), this.event, trainer);

            this.response = "You challenged " + trainer.name + " !";

            d.sendTurnEmbed();
        }
        else
        {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < Trainer.DAILY_TRAINERS.size(); i++) sb.append(i + 1).append(": ").append(Trainer.DAILY_TRAINERS.get(i).name).append(Trainer.hasPlayerDefeated(Trainer.DAILY_TRAINERS.get(i).trainerID, this.player.getId()) ? " (Defeated)" : "").append("\n");

            sb.append("\n\nElite Trainer: Use `p!trainerduel elite` to duel an Elite Trainer!");
            this.embed.setDescription(sb.toString());
            this.embed.setTitle("Daily Trainers");
        }
        return this;
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
