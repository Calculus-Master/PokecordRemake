package com.calculusmaster.pokecord.commandslegacy.pokemon;

import com.calculusmaster.pokecord.commandslegacy.CommandLegacy;
import com.calculusmaster.pokecord.commandslegacy.CommandLegacyInvalid;
import com.calculusmaster.pokecord.game.enums.elements.Feature;
import com.calculusmaster.pokecord.game.pokemon.Pokemon;
import com.calculusmaster.pokecord.game.pokemon.data.PokemonEntity;
import com.calculusmaster.pokecord.util.Global;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class CommandLegacyRedeem extends CommandLegacy
{
    public CommandLegacyRedeem(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public CommandLegacy runCommand()
    {
        if(this.insufficientMasteryLevel(Feature.REDEEM_POKEMON)) return this.invalidMasteryLevel(Feature.REDEEM_POKEMON);

        if(this.playerData.getRedeems() < 1) this.response = "You don't have any redeems!";
        else if(this.msg.length >= 2)
        {
            String pokemon = Global.normalize(this.getMultiWordContent(1));

            if(!this.isPokemon(pokemon)) this.response = "Invalid Pokemon!";
            else
            {
                Pokemon p = Pokemon.create(PokemonEntity.cast(pokemon));
                p.setLevel(new Random().nextInt(100) + 1);

                p.upload();

                this.playerData.changeRedeems(-1);
                this.playerData.addPokemon(p.getUUID());

                this.response = "You redeemed a Level " + p.getLevel() + " " + p.getName() + "!";
            }
        }
        else this.embed.setDescription(CommandLegacyInvalid.getShort());

        return this;
    }
}
