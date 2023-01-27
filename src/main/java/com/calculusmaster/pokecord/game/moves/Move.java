package com.calculusmaster.pokecord.game.moves;

import com.calculusmaster.pokecord.game.duel.Duel;
import com.calculusmaster.pokecord.game.enums.elements.*;
import com.calculusmaster.pokecord.game.moves.types.*;
import com.calculusmaster.pokecord.game.pokemon.Pokemon;
import com.calculusmaster.pokecord.game.pokemon.augments.PokemonAugment;
import com.calculusmaster.pokecord.util.helpers.LoggerHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Move
{
    //TODO: Keep checking the custom moves and see if they can function as close to the original as possible
    public static final List<String> WIP_MOVES = List.of("Roar", "Whirlwind", "Rage Powder", "Frustration", "Return", "Magnetic Flux", "After You", "Disable", "Miracle Eye", "Me First", "Gravity", "Spite", "Mean Look", "Foresight", "Wide Guard", "Teleport", "Odor Sleuth", "Helping Hand", "Stuff Cheeks", "Copycat", "Follow Me", "Sky Drop", "Simple Beam", "Fling", "Telekinesis", "Quash", "No Retreat", "Encore", "Substitute", "Magic Coat", "Embargo", "Ally Switch", "Sleep Talk", "Gear Up", "Struggle");
    public static final List<String> CUSTOM_MOVES = List.of("Leech Seed", "Rapid Spin", "Mirror Shot", "Worry Seed", "Aromatic Mist", "Pay Day", "Pollen Puff");
    public static List<String> INCOMPLETE_MOVES = new ArrayList<>();

    //Other Data Storage
    public static final List<String> SOUND_BASED_MOVES = List.of("Boomburst", "Bug Buzz", "Chatter", "Clanging Scales", "Clangorous Soul", "Clangorous Soulblaze", "Confide", "Disarming Voice", "Echoed Voice", "Eerie Spell", "Grass Whistle", "Growl", "Heal Bell", "Howl", "Hyper Voice", "Metal Sound", "Noble Roar", "Overdrive", "Parting Shot", "Perish Song", "Relic Song", "Roar", "Round", "Screech", "Shadow Panic", "Sing", "Snarl", "Snore", "Sparkling Aria", "Supersonic", "Uproar");
    public static final List<String> BITING_MOVES = List.of("Bite", "Crunch", "Fire Fang", "Fishious Rend", "Hyper Fang", "Ice Fang", "Jaw Lock", "Poison Fang", "Psychic Fangs", "Thunder Fang");
    public static final List<String> PULSE_MOVES = List.of("Aura Sphere", "Dark Pulse", "Dragon Pulse", "Heal Pulse", "Origin Pulse", "Terrain Pulse", "Water Pulse");
    public static final List<String> BALL_AND_BOMB_MOVES = List.of("Acid Spray", "Aura Sphere", "Barrage", "Beak Blast", "Bullet Seed", "Egg Bomb", "Electro Ball", "Energy Ball", "Focus Blast", "Gyro Ball", "Ice Ball", "Magnet Bomb", "Mist Ball", "Mud Bomb", "Octazooka", "Pollen Puff", "Pyro Ball", "Rock Blast", "Rock Wrecker", "Searing Shot", "Seed Bomb", "Shadow Ball", "Sludge Bomb", "Weather Ball", "Zap Cannon");
    public static final List<String> OHKO_MOVES = List.of("Fissure", "Guillotine", "Horn Drill", "Sheer Cold");
    public static final List<String> DIRECT_DAMAGE_MOVES = List.of("Bide", "Comeuppance", "Counter", "Dragon Rage", "Endeavor", "Final Gambit", "Guardian of Alola", "Metal Burst", "Mirror Coat", "Nature's Madness", "Night Shade", "Psywave", "Ruination", "Seismic Toss", "Sonic Boom", "Super Fang");

    private String name;
    private MoveData data;
    private Type type;
    private Category category;
    private int power;
    private int accuracy;
    private int priority;

    public boolean isZMove;
    public boolean isMaxMove;

    private double damageMultiplier;
    private double accuracyMultiplier;

    public int critChance;
    public boolean hitCrit;

    public static void init()
    {
        //Incomplete Moves
        INCOMPLETE_MOVES.clear();

        for(String m : MoveData.MOVES) if(!WIP_MOVES.contains(m) && !Move.isImplemented(m) && !INCOMPLETE_MOVES.contains(m)) INCOMPLETE_MOVES.add(m);

        INCOMPLETE_MOVES = INCOMPLETE_MOVES.stream().distinct().collect(Collectors.toList());
    }

    //From Data
    public Move(String name)
    {
        if(!Move.isMove(name)) name = "Tackle";

        this.data = MoveData.get(name);
        this.name = this.data.name;

        this.setDefaultValues();
    }

    private Move() {}

    //For temporary one-time moves (mainly Raid Pokemon)
    public static Move create(String name, Type type, Category category, int power, int accuracy)
    {
        Move m = new Move();

        m.data = new MoveData(name, type, category, power, accuracy, new ArrayList<>(), false, false);
        m.name = name;

        m.setDefaultValues();

        return m;
    }

    public String logic(Pokemon user, Pokemon opponent, Duel duel)
    {
        //Call specific move method
        Class<?> typeClass = this.getHostClass();

        String results = this.getMoveUsedResult(user);
        String moveName = this.getMethodName();

        try
        {
            results += (String)(typeClass.getMethod(moveName, Pokemon.class, Pokemon.class, Duel.class, Move.class).invoke(typeClass.getDeclaredConstructor().newInstance(), user, opponent, duel, this));
        }
        catch (Exception e)
        {
            LoggerHelper.reportError(Move.class, "Could not find Move Method (" + this.getName() + ")", e);

            results = "An error occurred while using this move (" + this.getName() + "). Defaulting to Tackle..." + new Move("Tackle").logic(user, opponent, duel);
        }

        return results;
    }

    public static boolean isImplemented(String name)
    {
        boolean implemented;

        if(WIP_MOVES.contains(name)) implemented = false;
        else
        {
            Move m = new Move(name);

            try { m.getHostClass().getMethod(m.getMethodName(), Pokemon.class, Pokemon.class, Duel.class, Move.class); implemented = true; }
            catch (NoSuchMethodException e) { implemented = false; }
        }

        //TODO: Temporarily Disabled: if(!implemented) LoggerHelper.warn(Move.class, "Unimplemented Move `" + name + "`!");

        return implemented;
    }

    public String getMethodName()
    {
        String out = this.name;

        if(this.name.equals("10,000,000 Volt Thunderbolt")) out = "TenMillionVoltThunderbolt";

        out = out.replaceAll("\\s", "").replaceAll("'", "");

        return out;
    }

    public Class<?> getHostClass()
    {
        if(this.isZMove) return ZMoves.class;
        else if(this.isMaxMove) return MaxMoves.class;
        else return switch(this.data.type) {
            case BUG -> BugMoves.class;
            case DARK -> DarkMoves.class;
            case DRAGON -> DragonMoves.class;
            case ELECTRIC -> ElectricMoves.class;
            case FAIRY -> FairyMoves.class;
            case FIGHTING -> FightingMoves.class;
            case FIRE -> FireMoves.class;
            case FLYING -> FlyingMoves.class;
            case GHOST -> GhostMoves.class;
            case GRASS -> GrassMoves.class;
            case GROUND -> GroundMoves.class;
            case ICE -> IceMoves.class;
            case NORMAL -> NormalMoves.class;
            case POISON -> PoisonMoves.class;
            case PSYCHIC -> PsychicMoves.class;
            case ROCK -> RockMoves.class;
            case STEEL -> SteelMoves.class;
            case WATER -> WaterMoves.class;
        };
    }

    public String getMoveUsedResult(Pokemon user)
    {
        return user.getName() + " used **" + this.name + "**! ";
    }

    public String getDamageResult(Pokemon opponent, int dmg)
    {
        return "It dealt **" + dmg + "** damage to " + opponent.getName() + "! " + (dmg > 0 ? this.getTypeEffectivenessText(opponent, false) + (this.hitCrit ? " It was a critical hit!" : "") : "");
    }

    public String getEffectivenessOverview(Pokemon opponent)
    {
        return this.getTypeEffectivenessText(opponent, true);
    }

    private String getTypeEffectivenessText(Pokemon other, boolean forBattle)
    {
        double e = TypeEffectiveness.getEffectiveness(other.getType()).get(this.type);

        //Freeze Dry
        if(this.name.equals("Freeze Dry")) e = this.getEffectivenessOverride(other);

        if(e == 4.0) return forBattle ? "**Extremely Effective**" : "It's **extremely** effective (4x)!";
        else if(e == 2.0) return forBattle ? "**Super Effective**" : "It's **super** effective (2x)!";
        else if(e == 1.0) return forBattle ? "**Effective**" : "";
        else if(e == 0.5) return forBattle ? "**Not Very Effective**" : "It's **not very** effective (0.5x)!";
        else if(e == 0.25) return forBattle ? "**Extremely Ineffective**" : "It's **extremely** ineffective (0.25x)!";
        else if(e == 0.0) return forBattle ? "**No Effect**" : this.getNoEffectResult(other);
        else throw new IllegalStateException("Effectiveness multiplier is a strange value: " + e);
    }

    public String getNoEffectResult(Pokemon opponent)
    {
        return "It **doesn't affect** " + opponent.getName() + "...";
    }

    public String getMissedResult(Pokemon user)
    {
        return user.getName() + " **missed** " + this.getName() + "!";
    }

    public String getNothingResult()
    {
        return "**Nothing** happened!";
    }

    public String getNotImplementedResult()
    {
        return "It did **nothing**! (Move has not been implemented yet)";
    }

    //Other Methods

    public boolean is(String... names)
    {
        return Stream.of(names).anyMatch(s -> s.equalsIgnoreCase(this.getName()));
    }

    public boolean is(Category... categories)
    {
        return List.of(categories).contains(this.getCategory());
    }

    public boolean is(Type... types)
    {
        return List.of(types).contains(this.getType());
    }

    public boolean is(List<String> names)
    {
        return names.contains(this.getName());
    }

    public boolean isContact()
    {
        return this.is(Category.PHYSICAL) ||this.is("Petal Dance", "Trump Card", "Wring Out", "Grass Knot", "Draining Kiss", "Infestation");
    }

    public static boolean isMove(String move)
    {
        return MoveData.MOVES.stream().anyMatch(move::equalsIgnoreCase);
    }

    public boolean isAccurate(Pokemon user, Pokemon opponent)
    {
        int combined = user.changes().getAccuracy() - opponent.changes().getEvasion();

        double numerator = 3.0 + combined > 0 ? combined : 0;
        double denominator = 3.0 + combined < 0 ? Math.abs(combined) : 0;

        double stageMultiplier = combined == 0 ? 1.0 : numerator / denominator;

        int threshold = (int)(this.getAccuracy() * stageMultiplier * this.accuracyMultiplier);

        return (new Random().nextInt(100) + 1) <= threshold;
    }

    public int getDamage(Pokemon user, Pokemon opponent)
    {
        Random r = new Random();

        //Augment: Precision Strikes
        if(user.hasAugment(PokemonAugment.PRECISION_STRIKES)) this.critChance += 2;

        //Damage = [((2 * Level / 5 + 2) * Power * A / D) / 50 + 2] * Modifier
        int level = user.getLevel();
        int power = this.power;
        int atkStat = user.getStat(this.category.equals(Category.PHYSICAL) ? Stat.ATK : Stat.SPATK);
        int defStat = opponent.getStat(this.category.equals(Category.PHYSICAL) ? Stat.DEF : Stat.SPDEF);

        //Modifier = Targets * Weather * Badge * Critical * Random * STAB * Type * Burn * Other
        //Ignored Components: Targets, Badge, Other
        //Weather component is done in the Harsh Sunlight section in Duel
        double critical = (r.nextInt(24) < this.critChance) ? 1.5 : 1.0;
        double random = (r.nextInt(16) + 85.0) / 100.0;
        double stab = user.isType(this.type) ? 1.5 : 1.0;
        double type = TypeEffectiveness.getEffectiveness(opponent.getType()).get(this.type);
        double burned = this.category.equals(Category.PHYSICAL) && user.hasStatusCondition(StatusCondition.BURNED) ? 0.5 : 1.0;

        if(critical > 1.0) hitCrit = true;

        //Any nuances go here

        //Psyshock, Psystrike, Secret Sword
        if(this.name.equals("Psyshock") || this.name.equals("Psystrike") || this.name.equals("Secret Sword")) defStat = opponent.getStat(Stat.DEF);

        //Photon Geyser, Light That Burns The Sky
        if(this.name.equals("Photon Geyser") || this.name.equals("Light That Burns The Sky")) atkStat = Math.max(user.getStat(Stat.ATK), user.getStat(Stat.SPATK));

        //Freeze Dry
        if(this.name.equals("Freeze Dry")) type = this.getEffectivenessOverride(opponent);

        //Gensect Z-Move: Elemental Techno Overdrive
        if(this.name.equals("Elemental Techno Overdrive")) stab = Math.random() + 1;

        //Body Press
        if(this.name.equals("Body Press")) atkStat = user.getStat(Stat.DEF);

        //Foul Play
        if(this.name.equals("Foul Play")) atkStat = opponent.getStat(Stat.ATK);

        //Ability: Adaptability
        if(stab > 1.0 && user.hasAbility(Ability.ADAPTABILITY)) stab *= 2.0;

        //Ability: Technician
        if(user.hasAbility(Ability.TECHNICIAN) && power <= 60) power = (int)(power * 1.5);

        //Ability: Wonder Guard
        if(user.hasAbility(Ability.WONDER_GUARD) && type < 2) type = 0.0;

        //Ability: Battle Armor
        if(opponent.hasAbility(Ability.BATTLE_ARMOR)) critical = 1.0;

        //Ability: Prism Armor
        if(opponent.hasAbility(Ability.PRISM_ARMOR) && type > 1) power = (int)(power * 0.75);

        //Ability: Neuroforce
        if(user.hasAbility(Ability.NEUROFORCE) && type > 1) power = (int)(power * 1.25);

        //Ability: Hustle
        if(user.hasAbility(Ability.HUSTLE)) atkStat *= 1.5;

        //Ability: Defeatist
        if(user.hasAbility(Ability.DEFEATIST) && user.getHealth() <= user.getMaxHealth(0.5)) atkStat *= 0.5;

        //Augment: Phantom Targeting
        if(user.hasAugment(PokemonAugment.PHANTOM_TARGETING) && type > 1.0) type *= 1.5;

        //Augment: Harmony
        if(user.hasAugment(PokemonAugment.HARMONY) && stab > 1.0) stab *= 1.75;

        //Augment: Precision Burst
        if(user.hasAugment(PokemonAugment.PRECISION_BURST) && critical > 1.0) critical = 2.0;

        //Augment: Raw Force
        if(user.hasAugment(PokemonAugment.RAW_FORCE))
        {
            critical = 1.0;
            stab = 1.0;
            type = 1.0;

            power *= 1.5;
        }

        //Augment: Modifying Force
        if(user.hasAugment(PokemonAugment.MODIFYING_FORCE))
        {
            if(critical > 1.0) critical *= 1.15;
            if(stab > 1.0) stab *= 1.2;
            type *= 1.25;

            power *= 0.6;
        }

        //Augment: Meteor Shower
        if(user.hasAugment(PokemonAugment.METEOR_SHOWER) && this.is("Meteor Mash"))
        {
            power -= 20;
        }

        //Augment: Magical Sure Shot
        if(user.hasAugment(PokemonAugment.SURE_SHOT) && this.is(Type.PSYCHIC))
        {
            if(type < 1.0) type = (type + 1.0) / 2;
            else if(type > 1.0) type *= 1.5;
        }

        double modifier = critical * random * stab * type * burned;
        double damage = (((2 * level / 5.0 + 2) * power * (double)atkStat / (double)defStat) / 50) + 2;
        double finalDMG = damage * modifier;

        finalDMG *= this.damageMultiplier;
        return (int)(finalDMG + 0.5);
    }

    private double getEffectivenessOverride(Pokemon other)
    {
        return switch(this.getName()) {
            case "Freeze Dry" -> Stream.of(Type.WATER, Type.GRASS, Type.GROUND, Type.FLYING, Type.DRAGON).anyMatch(other::isType) ? 2.0 : (Stream.of(Type.FIRE, Type.ICE, Type.STEEL).anyMatch(other::isType) ? 0.5 : 1.0);
            case "Flying Press" -> Stream.of(Type.NORMAL, Type.GRASS, Type.ICE, Type.FIGHTING, Type.DARK).anyMatch(other::isType) ? 2.0 : (Stream.of(Type.ELECTRIC, Type.POISON, Type.FLYING, Type.PSYCHIC, Type.FAIRY).anyMatch(other::isType) ? 0.5 : (other.isType(Type.GHOST) ? 0.0 : 1.0));
            default -> 1.0;
        };
    }

    public void setDefaultValues()
    {
        this.type = this.data.type;
        this.category = this.data.category;
        this.power = this.data.basePower;
        this.accuracy = this.data.baseAccuracy;

        this.isZMove = this.data.isZMove;
        this.isMaxMove = this.data.isMaxMove;

        this.hitCrit = false;
        this.critChance = 1;

        this.damageMultiplier = 1.0;
        this.accuracyMultiplier = 1.0;
    }

    public static String getRandomMove()
    {
        return MoveData.MOVES.get(new Random().nextInt(MoveData.MOVES.size()));
    }

    //Other
    public void setDamageMultiplier(double damageMultiplier)
    {
        this.damageMultiplier *= damageMultiplier;
    }

    public void setAccuracyMultiplier(double accuracyMultiplier)
    {
        this.accuracyMultiplier = accuracyMultiplier;
    }

    //Core
    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public void setPriority()
    {
        this.setPriority(switch(this.getName()) {
            case "Helping Hand" -> 5;
            case "Baneful Bunker", "Detect", "Endure", "Kings Shield", "Magic Coat", "Protect", "Spiky Shield", "Snatch" -> 4;
            case "Crafty Shield", "Fake Out", "Quick Guard", "Wide Guard", "Spotlight" -> 3;
            case "Ally Switch", "Extreme Speed", "Feint", "First Impression", "Follow Me", "Rage Powder" -> 2;
            case "Accelerock", "Aqua Jet", "Baby Doll Eyes", "Bide", "Bullet Punch", "Ice Shard", "Iron Deluge", "Mach Punch", "Powder", "Quick Attack", "Shadow Sneak", "Sucker Punch", "Vacuum Wave", "Water Shuriken" -> 1;
            case "Vital Throw" -> -1;
            case "Beak Blast", "Focus Punch", "Shell Trap" -> -3;
            case "Avalanche", "Revenge" -> -4;
            case "Counter", "Mirror Coat" -> -5;
            case "Circle Throw", "Dragon Tail", "Roar", "Whirlwind" -> -6;
            case "Trick Room" -> -7;
            default -> 0;
        });
    }

    public String getName()
    {
        return this.name;
    }

    public Type getType()
    {
        return this.type;
    }

    public void setType(Type t)
    {
        this.type = t;
    }

    public Category getCategory()
    {
        return this.category;
    }

    public void setCategory(Category c)
    {
        this.category = c;
    }

    public int getPower()
    {
        return this.power;
    }

    public void setPower(int p)
    {
        this.power = p;
    }

    public void setPower(double p)
    {
        this.power = (int)(p * this.power);
    }

    public int getAccuracy()
    {
        return this.accuracy;
    }

    public void setAccuracy(int a)
    {
        this.accuracy = a;
    }
}
