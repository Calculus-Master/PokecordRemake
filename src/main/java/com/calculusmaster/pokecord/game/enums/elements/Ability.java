package com.calculusmaster.pokecord.game.enums.elements;

import com.calculusmaster.pokecord.util.Global;

import java.util.Arrays;
import java.util.List;

public enum Ability
{
    STENCH,
    DRIZZLE,
    SPEED_BOOST,
    BATTLE_ARMOR,
    STURDY,
    DAMP,
    LIMBER,
    SAND_VEIL,
    STATIC,
    VOLT_ABSORB,
    WATER_ABSORB,
    OBLIVIOUS,
    CLOUD_NINE,
    COMPOUND_EYES,
    INSOMNIA,
    COLOR_CHANGE,
    IMMUNITY,
    FLASH_FIRE,
    SHIELD_DUST,
    OWN_TEMPO,
    SUCTION_CUPS,
    INTIMIDATE,
    SHADOW_TAG,
    ROUGH_SKIN,
    WONDER_GUARD,
    LEVITATE,
    EFFECT_SPORE,
    SYNCHRONIZE,
    CLEAR_BODY,
    NATURAL_CURE,
    LIGHTNING_ROD,
    SERENE_GRACE,
    SWIFT_SWIM,
    CHLOROPHYLL,
    ILLUMINATE,
    TRACE,
    HUGE_POWER,
    POISON_POINT,
    INNER_FOCUS,
    MAGMA_ARMOR,
    WATER_VEIL,
    MAGNET_PULL,
    SOUNDPROOF,
    RAIN_DISH,
    SAND_STREAM,
    PRESSURE,
    THICK_FAT,
    EARLY_BIRD,
    FLAME_BODY,
    RUN_AWAY,
    KEEN_EYE,
    HYPER_CUTTER,
    PICKUP,
    TRUANT,
    HUSTLE,
    CUTE_CHARM,
    PLUS,
    MINUS,
    FORECAST,
    STICKY_HOLD,
    SHED_SKIN,
    GUTS,
    MARVEL_SCALE,
    LIQUID_OOZE,
    OVERGROW,
    BLAZE,
    TORRENT,
    SWARM,
    ROCK_HEAD,
    DROUGHT,
    ARENA_TRAP,
    VITAL_SPIRIT,
    WHITE_SMOKE,
    PURE_POWER,
    SHELL_ARMOR,
    AIR_LOCK,
    TANGLED_FEET,
    MOTOR_DRIVE,
    RIVALRY,
    STEADFAST,
    SNOW_CLOAK,
    GLUTTONY,
    ANGER_POINT,
    UNBURDEN,
    HEATPROOF,
    SIMPLE,
    DRY_SKIN,
    DOWNLOAD,
    IRON_FIST,
    POISON_HEAL,
    ADAPTABILITY,
    SKILL_LINK,
    HYDRATION,
    SOLAR_POWER,
    QUICK_FEET,
    NORMALIZE,
    SNIPER,
    MAGIC_GUARD,
    NO_GUARD,
    STALL,
    TECHNICIAN,
    LEAF_GUARD,
    KLUTZ,
    MOLD_BREAKER,
    SUPER_LUCK,
    AFTERMATH,
    ANTICIPATION,
    FOREWARN,
    UNAWARE,
    TINTED_LENS,
    FILTER,
    SLOW_START,
    SCRAPPY,
    STORM_DRAIN,
    ICE_BODY,
    SOLID_ROCK,
    SNOW_WARNING,
    HONEY_GATHER,
    FRISK,
    RECKLESS,
    MULTITYPE,
    FLOWER_GIFT,
    BAD_DREAMS,
    PICKPOCKET,
    SHEER_FORCE,
    CONTRARY,
    UNNERVE,
    DEFIANT,
    DEFEATIST,
    CURSED_BODY,
    HEALER,
    FRIEND_GUARD,
    WEAK_ARMOR,
    HEAVY_METAL,
    LIGHT_METAL,
    MULTISCALE,
    TOXIC_BOOST,
    FLARE_BOOST,
    HARVEST,
    TELEPATHY,
    MOODY,
    OVERCOAT,
    POISON_TOUCH,
    REGENERATOR,
    BIG_PECKS,
    SAND_RUSH,
    WONDER_SKIN,
    ANALYTIC,
    ILLUSION,
    IMPOSTER,
    INFILTRATOR,
    MUMMY,
    MOXIE,
    JUSTIFIED,
    RATTLED,
    MAGIC_BOUNCE,
    SAP_SIPPER,
    PRANKSTER,
    SAND_FORCE,
    IRON_BARBS,
    ZEN_MODE,
    VICTORY_STAR,
    TURBOBLAZE,
    TERAVOLT,
    AROMA_VEIL,
    FLOWER_VEIL,
    CHEEK_POUCH,
    PROTEAN,
    FUR_COAT,
    MAGICIAN,
    BULLETPROOF,
    COMPETITIVE,
    STRONG_JAW,
    REFRIGERATE,
    SWEET_VEIL,
    STANCE_CHANGE,
    GALE_WINGS,
    MEGA_LAUNCHER,
    GRASS_PELT,
    SYMBIOSIS,
    TOUGH_CLAWS,
    PIXILATE,
    GOOEY,
    AERILATE,
    PARENTAL_BOND,
    DARK_AURA,
    FAIRY_AURA,
    AURA_BREAK,
    PRIMORDIAL_SEA,
    DESOLATE_LAND,
    DELTA_STREAM,
    STAMINA,
    WIMP_OUT,
    EMERGENCY_EXIT,
    WATER_COMPACTION,
    MERCILESS,
    SHIELDS_DOWN,
    STAKEOUT,
    WATER_BUBBLE,
    STEELWORKER,
    BERSERK,
    SLUSH_RUSH,
    LONG_REACH,
    LIQUID_VOICE,
    TRIAGE,
    GALVANIZE,
    SURGE_SURFER,
    SCHOOLING,
    DISGUISE,
    BATTLE_BOND,
    POWER_CONSTRUCT,
    CORROSION,
    COMATOSE,
    QUEENLY_MAJESTY,
    INNARDS_OUT,
    DANCER,
    BATTERY,
    FLUFFY,
    DAZZLING,
    SOUL_HEART,
    TANGLING_HAIR,
    RECEIVER,
    POWER_OF_ALCHEMY,
    BEAST_BOOST,
    RKS_SYSTEM,
    ELECTRIC_SURGE,
    PSYCHIC_SURGE,
    MISTY_SURGE,
    GRASSY_SURGE,
    FULL_METAL_BODY,
    SHADOW_SHIELD,
    PRISM_ARMOR,
    NEUROFORCE,
    INTREPID_SWORD,
    DAUNTLESS_SHIELD,
    LIBERO,
    BALL_FETCH,
    COTTON_DOWN,
    PROPELLER_TAIL,
    MIRROR_ARMOR,
    GULP_MISSILE,
    STALWART,
    STEAM_ENGINE,
    PUNK_ROCK,
    SAND_SPIT,
    ICE_SCALES,
    RIPEN,
    ICE_FACE,
    POWER_SPOT,
    MIMICRY,
    SCREEN_CLEANER,
    STEELY_SPIRIT,
    PERISH_BODY,
    WANDERING_SPIRIT,
    GORILLA_TACTICS,
    NEUTRALIZING_GAS,
    PASTEL_VEIL,
    HUNGER_SWITCH,
    QUICK_DRAW,
    UNSEEN_FIST,
    CURIOUS_MEDICINE,
    TRANSISTOR,
    DRAGONS_MAW,
    CHILLING_NEIGH,
    GRIM_NEIGH,
    AS_ONE;

    public static final List<Ability> IMPLEMENTED = List.of(DESOLATE_LAND, PRIMORDIAL_SEA, DELTA_STREAM, MULTITYPE, ADAPTABILITY, TECHNICIAN, DISGUISE, DRIZZLE, SNOW_WARNING, DROUGHT, SAND_STREAM, STANCE_CHANGE, IRON_BARBS, LEVITATE, GRIM_NEIGH, CHILLING_NEIGH, DRAGONS_MAW, TRANSISTOR, SPEED_BOOST, WONDER_GUARD, BATTLE_ARMOR, DAMP, LIMBER, PASTEL_VEIL, PIXILATE);

    public String getName()
    {
        return Global.normalize(this.toString().replaceAll("_", " "));
    }

    public static Ability cast(String input)
    {
        return Arrays.stream(values()).filter(a -> a.toString().replaceAll("_", " ").equalsIgnoreCase(input.replaceAll("'", ""))).findFirst().orElse(null);
    }

    public String formatActivation(String userName, String result)
    {
        return "*" + userName + "'s " + this.getName() + "* – " + result;
    }
}
