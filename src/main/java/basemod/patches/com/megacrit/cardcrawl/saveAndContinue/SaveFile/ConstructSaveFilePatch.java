package basemod.patches.com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

import basemod.BaseMod;
import basemod.abstracts.CustomSaveableRaw;

@SpirePatch(clz=SaveFile.class, method=SpirePatch.CONSTRUCTOR, paramtypez={SaveFile.SaveType.class})
public class ConstructSaveFilePatch
{
    public static void Prefix(SaveFile __instance, SaveFile.SaveType saveType) {
        // Card saves
        ModSaves.ArrayListOfJsonElement modCardSaves = new ModSaves.ArrayListOfJsonElement();
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if (card instanceof CustomSaveableRaw) {
                modCardSaves.add(((CustomSaveableRaw)card).onSaveRaw());
            } else {
                modCardSaves.add(null);
            }
        }
        ModSaves.modCardSaves.set(__instance, modCardSaves);

        // Relic saves
        ModSaves.ArrayListOfJsonElement modRelicSaves = new ModSaves.ArrayListOfJsonElement();
        for (AbstractRelic relic : AbstractDungeon.player.relics) {
            if (relic instanceof CustomSaveableRaw) {
                modRelicSaves.add(((CustomSaveableRaw)relic).onSaveRaw());
            } else {
                modRelicSaves.add(null);
            }
        }
        ModSaves.modRelicSaves.set(__instance, modRelicSaves);

        // Mod saves
        ModSaves.HashMapOfJsonElement modSaves = new ModSaves.HashMapOfJsonElement();
        for (Map.Entry<String,CustomSaveableRaw> field : BaseMod.getSaveFields().entrySet()) {
            modSaves.put(field.getKey(), field.getValue().onSaveRaw());
        }
        ModSaves.modSaves.set(__instance, modSaves);
    }
}