package com.calculusmaster.pokecord.game.bounties.objectives;

import com.calculusmaster.pokecord.game.bounties.enums.ObjectiveType;
import com.calculusmaster.pokecord.game.enums.elements.Type;
import com.calculusmaster.pokecord.util.Global;
import org.bson.Document;

import java.util.Random;

public class UseMaxMoveTypeObjective extends Objective
{
    private Type type;

    public UseMaxMoveTypeObjective()
    {
        super(ObjectiveType.USE_MAX_MOVE_TYPE, Objective.randomTargetAmount(5, 15));
        this.type = Type.values()[new Random().nextInt(Type.values().length)];
    }

    @Override
    public String getDesc()
    {
        return "Use any " + this.target + " Max Moves that are " + Global.normalCase(this.type.toString()) + " Type";
    }

    @Override
    public Document addObjectiveData(Document document)
    {
        return super.addObjectiveData(document)
                .append("type", this.type.toString());
    }

    public Type getType()
    {
        return this.type;
    }

    public UseMaxMoveTypeObjective setType(String type)
    {
        this.type = Type.cast(type);
        return this;
    }
}
