package com.calculusmaster.pokecord;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.calculusmaster.pokecord.commands.Command;
import com.calculusmaster.pokecord.commands.economy.CommandMarket;
import com.calculusmaster.pokecord.commands.pokemon.CommandPokemon;
import com.calculusmaster.pokecord.game.Move;
import com.calculusmaster.pokecord.game.Pokemon;
import com.calculusmaster.pokecord.util.Global;
import com.calculusmaster.pokecord.util.Listener;
import com.calculusmaster.pokecord.util.PokemonRarity;
import com.calculusmaster.pokecord.util.PrivateInfo;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.internal.requests.Route;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.stream.Collectors;

public class Pokecord
{
    public static void main(String[] args) throws LoginException
    {
        //Disable MongoDB Logging
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver.connection").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver.management").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver.cluster").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver.protocol.insert").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver.protocol.query").setLevel(Level.OFF);
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver.protocol.update").setLevel(Level.OFF);

        //Initializations
        Pokemon.init();
        Move.init();
        PokemonRarity.init();
        Command.init();
        CommandMarket.init();
        CommandPokemon.init();

        System.out.println(Global.POKEMON.toString());
//      System.out.println(Move.MOVES.stream().map(Move::getName).collect(Collectors.toList()).toString());
        //System.out.println(Move.MOVES.values().stream().map(Move.MoveData::toString).collect(Collectors.toList()).toString());

        //new move system test
        //MoveNew m = new MoveNew("Tackle");
        //System.out.println(m.logic(null, null, null));

        //System.out.println(TypeEffectiveness.getCombinedMap(Type.FLYING, Type.FIRE).toString());

        //Create Bot
        JDABuilder bot = JDABuilder.createDefault(PrivateInfo.TOKEN);
        bot.setActivity(Activity.playing("Pokemon"));
        bot.addEventListeners(new Listener());
        bot.build();
    }
}
