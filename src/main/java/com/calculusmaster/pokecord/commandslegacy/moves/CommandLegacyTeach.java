package com.calculusmaster.pokecord.commandslegacy.moves;

import com.calculusmaster.pokecord.commandslegacy.CommandLegacy;
import com.calculusmaster.pokecord.commandslegacy.CommandLegacyInvalid;
import com.calculusmaster.pokecord.game.enums.elements.Feature;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;

public class CommandLegacyTeach extends CommandLegacy
{
    public CommandLegacyTeach(MessageReceivedEvent event, String[] msg)
    {
        super(event, msg);
    }

    @Override
    public CommandLegacy runCommand()
    {
        if(this.insufficientMasteryLevel(Feature.TEACH_TMS)) return this.invalidMasteryLevel(Feature.TEACH_TMS);

        if(this.msg.length < 3 || (!this.msg[1].equals("tr") && !this.msg[1].equals("tm")) || !this.msg[2].chars().allMatch(Character::isDigit))
        {
            this.embed.setDescription(CommandLegacyInvalid.getShort());
            return this;
        }

        boolean tm = this.msg[1].equals("tm");
        int number = Integer.parseInt(this.msg[2]);

        this.response = "Teaching TMs is currently disabled.";

//        if((tm && number > 0 && number <= 100 && this.playerData.getTMList().contains(TM.get(number).toString())) || (!tm && number >= 0 && number < 100 && this.playerData.getTRList().contains(TR.get(number).toString())))
//        {
//            Pokemon selected = this.playerData.getSelectedPokemon();
//
//            if(tm && selected.canLearnTM(TM.get(number)))
//            {
//                TM held = selected.getTM();
//                TM target = TM.get(number);
//
//                selected.setTM(target);
//                selected.updateTMTR();
//
//                this.embed.setDescription("Taught " + target.toString() + " - " + target.getMoveName() + " to " + selected.getRealName());
//
//                this.playerData.removeTM(target.toString());
//                if(selected.hasTM()) this.playerData.addTM(held.toString());
//            }
//            else this.embed.setDescription(selected.getName() + " cannot learn that " + (tm ? "TM" : "TR") + "!");
//        }

        return this;
    }

    private boolean JSONArrayContains(JSONArray j, String val)
    {
        for (int i = 0; i < j.length(); i++) if(j.getString(i).equals(val)) return true;
        return false;
    }
}
