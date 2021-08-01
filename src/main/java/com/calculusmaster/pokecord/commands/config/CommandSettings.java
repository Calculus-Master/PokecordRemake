package com.calculusmaster.pokecord.commands.config;

import com.calculusmaster.pokecord.commands.Command;
import com.calculusmaster.pokecord.commands.CommandInvalid;
import com.calculusmaster.pokecord.commands.pokemon.CommandPokemon;
import com.calculusmaster.pokecord.util.Global;
import com.calculusmaster.pokecord.util.helpers.SettingsHelper;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static com.calculusmaster.pokecord.util.helpers.SettingsHelper.Setting;
import static com.calculusmaster.pokecord.util.helpers.SettingsHelper.Setting.*;

public class CommandSettings extends Command
{
    public CommandSettings(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public Command runCommand()
    {
        boolean isUserAdmin = Global.userHasAdmin(this.server, this.player);

        boolean client = this.msg.length >= 2 && (this.msg[1].equals("client") || this.msg[1].equals("c"));
        boolean server = this.msg.length >= 2 && (this.msg[1].equals("server") || this.msg[1].equals("s"));

        if(client)
        {
            if(this.msg.length == 2 || !Setting.isValid(this.msg[2]))
            {
                this.embed.setTitle("Client Settings");

                StringBuilder clientSettings = new StringBuilder();
                for(Setting s : Setting.values()) if(s.isClient()) clientSettings.append("`p!settings client ").append(s.getCommand()).append("` - ").append(s.getDesc()).append("\n");

                this.embed.setDescription(clientSettings.toString());
            }
            else
            {
                SettingsHelper settings = this.playerData.getSettings();

                if(CLIENT_DETAILED.matches(this.msg[2]))
                {
                    //Toggle if no input given
                    if(this.msg.length == 3)
                    {
                        boolean currentValue = settings.getSettingBoolean(CLIENT_DETAILED);

                        settings.updateSettingBoolean(CLIENT_DETAILED, !currentValue);

                        if(currentValue) this.sendMsg("Disabled viewing of detailed information!");
                        else this.sendMsg("Enabled viewing of detailed information!");
                    }
                    //Set value to specific input
                    else if(this.msg.length == 4)
                    {
                        if("true".contains(this.msg[3]) || "false".contains(this.msg[3]))
                        {
                            boolean newValue = "true".contains(this.msg[3]);

                            settings.updateSettingBoolean(CLIENT_DETAILED, newValue);

                            if(newValue) this.sendMsg("Enabled viewing of detailed information!");
                            else this.sendMsg("Disabled viewing of detailed information!");
                        }
                        else this.sendMsg("Valid arguments: `true` or `false`!");
                    }
                }
                else if(CLIENT_CATCH_AUTO_INFO.matches(this.msg[2]))
                {
                    //Toggle if no input given
                    if(this.msg.length == 3)
                    {
                        boolean currentValue = settings.getSettingBoolean(CLIENT_CATCH_AUTO_INFO);

                        settings.updateSettingBoolean(CLIENT_CATCH_AUTO_INFO, !currentValue);

                        if(currentValue) this.sendMsg("Disabled automatic info viewing after catch!");
                        else this.sendMsg("Enabled automatic info viewing after catch!");
                    }
                    //Set value to specific input
                    else if(this.msg.length == 4)
                    {
                        if("true".contains(this.msg[3]) || "false".contains(this.msg[3]))
                        {
                            boolean newValue = "true".contains(this.msg[3]);

                            settings.updateSettingBoolean(CLIENT_CATCH_AUTO_INFO, newValue);

                            if(newValue) this.sendMsg("Enabled automatic info viewing after catch!");
                            else this.sendMsg("Disabled automatic info viewing after catch!");
                        }
                        else this.sendMsg("Valid arguments: `true` or `false`!");
                    }
                }
                else if(CLIENT_DEFAULT_ORDER.matches(this.msg[2]))
                {
                    if(this.msg.length == 4)
                    {
                        CommandPokemon.OrderSort order = CommandPokemon.OrderSort.cast(this.msg[3]);

                        if(order == null) this.sendMsg("Invalid Order!");
                        else
                        {
                            settings.updateSettingString(CLIENT_DEFAULT_ORDER, order.toString());

                            this.sendMsg("Your Pokemon List will now be ordered by `" + order.toString().toLowerCase() + "`!");
                        }
                    }
                }
                else if(CLIENT_POKEMON_LIST_FIELDS.matches(this.msg[2]))
                {
                    //Toggle if no input given
                    if (this.msg.length == 3)
                    {
                        boolean currentValue = settings.getSettingBoolean(CLIENT_POKEMON_LIST_FIELDS);

                        settings.updateSettingBoolean(CLIENT_POKEMON_LIST_FIELDS, !currentValue);

                        if (currentValue) this.sendMsg("Pokemon List view is now Text-based!");
                        else this.sendMsg("Pokemon List view is now Field-based!");
                    }
                    //Set value to specific input
                    else if (this.msg.length == 4)
                    {
                        if ("true".contains(this.msg[3]) || "false".contains(this.msg[3]))
                        {
                            boolean newValue = "true".contains(this.msg[3]);

                            settings.updateSettingBoolean(CLIENT_POKEMON_LIST_FIELDS, newValue);

                            if (newValue) this.sendMsg("Pokemon List view is now Field-based!");
                            else this.sendMsg("Pokemon List view is now Text-based!");
                        }
                        else this.sendMsg("Valid arguments: `true` or `false`!");
                    }
                }
                else this.embed.setDescription(CommandInvalid.getShort());
            }
        }
        else if(server)
        {
            if(this.msg.length == 2 || (isUserAdmin && !Setting.isValid(this.msg[2])))
            {
                this.embed.setTitle("Server Settings");

                StringBuilder serverSettings = new StringBuilder();
                for(Setting s : Setting.values()) if(s.isServer()) serverSettings.append("`p!settings server ").append(s.getCommand()).append("` - ").append(s.getDesc()).append("\n");

                this.embed.setDescription(serverSettings.toString());
                this.embed.setFooter("Only Administrators can edit these settings!");
            }
            else if(!isUserAdmin)
            {
                this.sendMsg("You must be an Administrator to change server settings!");
            }
            else
            {
                if(SERVER_PREFIX.matches(this.msg[2]))
                {
                    //Reset prefix
                    if(this.msg.length == 3)
                    {
                        this.serverData.setPrefix("p!");

                        this.sendMsg("Server prefix has been reset to `p!`");
                    }
                    //Change prefix
                    else if(this.msg.length == 4)
                    {
                        String oldPrefix = this.serverData.getPrefix();

                        this.serverData.setPrefix(this.msg[3]);

                        this.sendMsg("Server prefix has been changed from `" + oldPrefix + "` to `" + this.msg[3] + "`");
                    }
                }
                else if(SERVER_SPAWNCHANNEL.matches(this.msg[2]))
                {
                    String channel = !this.event.getMessage().getMentionedChannels().isEmpty() ? this.event.getMessage().getMentionedChannels().get(0).getId() : this.event.getMessage().getChannel().getId();
                    TextChannel channelName = this.event.getGuild().getTextChannelById(channel);

                    if(channelName != null && this.serverData.getSpawnChannels().contains(channel))
                    {
                        this.serverData.removeSpawnChannel(channel);

                        this.sendMsg(channelName.getAsMention() + " will no longer have Pokemon spawns!");
                    }
                    else if(channelName != null)
                    {
                        this.serverData.addSpawnChannel(channel);

                        this.sendMsg(channelName.getAsMention() + " will now have Pokemon spawns!");
                    }
                }
                else if(SERVER_ZCRYSTAL_DUEL_EQUIP.matches(this.msg[2]))
                {
                    if(this.msg.length == 3 || (this.msg.length == 4 && !this.msg[3].equals("true") && !this.msg[3].equals("false")))
                    {
                        boolean currentValue = this.serverData.canEquipZCrystalDuel();

                        this.serverData.setEquipZCrystalDuel(!currentValue);

                        this.sendMsg("Players can " + (this.serverData.canEquipZCrystalDuel() ? "now" : "no longer") + " equip Z Crystals while in a Duel!");
                    }
                    else if(this.msg.length == 4)
                    {
                        boolean newValue = this.msg[3].equals("true");

                        this.serverData.setEquipZCrystalDuel(newValue);

                        this.sendMsg("Players can " + (newValue ? "now" : "no longer") + " equip Z Crystals while in a Duel!");
                    }
                }
                else if(SERVER_DYNAMAX.matches(this.msg[2]))
                {
                    if(this.msg.length == 3 || (this.msg.length == 4 && !this.msg[3].equals("true") && !this.msg[3].equals("false")))
                    {
                        boolean currentValue = this.serverData.isDynamaxEnabled();

                        this.serverData.setDynamaxEnabled(!currentValue);

                        this.sendMsg("Players can " + (this.serverData.isDynamaxEnabled() ? "now" : "no longer") + " Dynamax their Pokemon in a Duel!");
                    }
                    else if(this.msg.length == 4)
                    {
                        boolean newValue = this.msg[3].equals("true");

                        this.serverData.setDynamaxEnabled(newValue);

                        this.sendMsg("Players can " + (newValue ? "now" : "no longer") + " Dynamax their Pokemon in a Duel!");
                    }
                }
                else if(SERVER_ZMOVE.matches(this.msg[2]))
                {
                    if(this.msg.length == 3 || (this.msg.length == 4 && !this.msg[3].equals("true") && !this.msg[3].equals("false")))
                    {
                        boolean currentValue = this.serverData.areZMovesEnabled();

                        this.serverData.setZMovesEnabled(!currentValue);

                        this.sendMsg("Players can " + (this.serverData.areZMovesEnabled() ? "now" : "no longer") + " use Z-Moves in a Duel!");
                    }
                    else if(this.msg.length == 4)
                    {
                        boolean newValue = this.msg[3].equals("true");

                        this.serverData.setZMovesEnabled(newValue);

                        this.sendMsg("Players can " + (newValue ? "now" : "no longer") + " use Z-Moves in a Duel!");
                    }
                }
                else if(SERVER_DUELCHANNEL.matches(this.msg[2]))
                {
                    String channel = !this.event.getMessage().getMentionedChannels().isEmpty() ? this.event.getMessage().getMentionedChannels().get(0).getId() : this.event.getMessage().getChannel().getId();
                    TextChannel channelName = this.event.getGuild().getTextChannelById(channel);

                    if(channelName != null && this.serverData.getDuelChannels().contains(channel))
                    {
                        this.serverData.removeDuelChannel(channel);

                        this.sendMsg(this.serverData.getDuelChannels().isEmpty() ? "Duels are now allowed anywhere!" : "Duels are no longer allowed in " + channelName.getAsMention() + "!");
                    }
                    else if(channelName != null)
                    {
                        this.serverData.addDuelChannel(channel);

                        this.sendMsg("Duels are now allowed in " + channelName.getAsMention() + "!");
                    }
                    else if(this.msg.length == 4 && this.msg[3].equals("reset"))
                    {
                        this.serverData.clearDuelChannels();

                        this.sendMsg("Duels are now allowed anywhere!");
                    }
                }
                else if(SERVER_BOTCHANNEL.matches(this.msg[2]))
                {
                    String channel = !this.event.getMessage().getMentionedChannels().isEmpty() ? this.event.getMessage().getMentionedChannels().get(0).getId() : this.event.getMessage().getChannel().getId();
                    TextChannel channelName = this.event.getGuild().getTextChannelById(channel);

                    if(channelName != null && this.serverData.getBotChannels().contains(channel))
                    {
                        this.serverData.removeBotChannel(channel);

                        this.sendMsg(this.serverData.getDuelChannels().isEmpty() ? "Bot commands are now allowed anywhere!" : "Bot commands are no longer allowed in " + channelName.getAsMention() + "!");
                    }
                    else if(channelName != null)
                    {
                        this.serverData.addBotChannel(channel);

                        this.sendMsg("Bot commands are now allowed in " + channelName.getAsMention() + "!");
                    }
                    else if(this.msg.length == 4 && this.msg[3].equals("reset"))
                    {
                        this.serverData.clearBotChannels();

                        this.sendMsg("Bot commands are now allowed anywhere!");
                    }
                }
                else this.embed.setDescription(CommandInvalid.getShort());
            }
        }
        else
        {
            this.sendMsg("Type `p!settings client` or `p!settings server` to view all available settings!");
        }

        return this;
    }
}
