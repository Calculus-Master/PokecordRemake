package com.calculusmaster.pokecord;

import com.calculusmaster.pokecord.commands.Commands;
import com.calculusmaster.pokecord.commands.duel.CommandTarget;
import com.calculusmaster.pokecord.commands.moves.CommandAbilityInfo;
import com.calculusmaster.pokecord.game.PokePass;
import com.calculusmaster.pokecord.game.duel.players.GymLeader;
import com.calculusmaster.pokecord.game.duel.players.Trainer;
import com.calculusmaster.pokecord.game.moves.Move;
import com.calculusmaster.pokecord.game.pokemon.Pokemon;
import com.calculusmaster.pokecord.game.pokemon.PokemonRarity;
import com.calculusmaster.pokecord.game.pokemon.SpecialEvolutionRegistry;
import com.calculusmaster.pokecord.util.PrivateInfo;
import com.calculusmaster.pokecord.util.helpers.*;
import com.calculusmaster.pokecord.util.listener.ButtonListener;
import com.calculusmaster.pokecord.util.listener.Listener;
import com.calculusmaster.pokecord.util.listener.MiscListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Pokecord
{
    public static JDA BOT_JDA;

    public static void main(String[] args) throws InterruptedException, LoginException
    {
        LoggerHelper.disableMongoLoggers();

        //Initializations
        long start = System.currentTimeMillis();

        LoggerHelper.init("Config", ConfigHelper::init);
        LoggerHelper.init("Pokemon Data", DataHelper::createPokemonData, true);
        LoggerHelper.init("Pokemon", DataHelper::createPokemonList);
        LoggerHelper.init("Pokemon", Pokemon::init);
        LoggerHelper.init("Gigantamax", DataHelper::createGigantamaxDataMap);
        LoggerHelper.init("EV Lists", DataHelper::createEVLists);
        LoggerHelper.init("Type Lists", DataHelper::createTypeLists);
        LoggerHelper.init("Species Descriptions", DataHelper::createSpeciesDescLists, true);
        LoggerHelper.init("Evolutions", SpecialEvolutionRegistry::init);
        LoggerHelper.init("Daily Trainer", Trainer::setDailyTrainers);
        LoggerHelper.init("Move", Move::init);
        LoggerHelper.init("Ability Info", CommandAbilityInfo::init);
        LoggerHelper.init("Pokemon Rarity", PokemonRarity::init);
        LoggerHelper.init("Command Handler", Commands::init);
        LoggerHelper.init("Gym Leader", GymLeader::init);
        LoggerHelper.init("PokePass", PokePass::init);
        LoggerHelper.init("Achievement Cache", CacheHelper::initAchievementCache);
        LoggerHelper.init("Market", CacheHelper::initMarketEntries, true);
        LoggerHelper.init("CommandPokemon", CacheHelper::initPokemonLists, true);

        long end = System.currentTimeMillis();

        LoggerHelper.info(Pokecord.class, "Initialization Finished - " + (end - start) + "ms!");

        start = System.currentTimeMillis();

        //Create Bot
        JDABuilder bot = JDABuilder
                .createDefault(PrivateInfo.TOKEN)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(EnumSet.allOf(GatewayIntent.class))
                .setChunkingFilter(ChunkingFilter.ALL)
                .setActivity(Activity.playing("Pokemon"))
                .addEventListeners(
                        new Listener(),
                        new ButtonListener(),
                        new MiscListener()
                );

        BOT_JDA = bot.build().awaitReady();

        LoggerHelper.init("Spawn Event & Location Event Thread Pools", ThreadPoolHandler::createGuildSizeThreadPools);

        end = System.currentTimeMillis();

        LoggerHelper.info(Pokecord.class, "Bot Loading Complete (" + (end - start) + "ms)!");

        for(Guild g : BOT_JDA.getGuilds())
        {
            Thread.sleep(1000);

            SpawnEventHelper.start(g);
            LocationEventHelper.start(g);

            CommandTarget.generateNewServerTarget(g);
        }
    }
}
