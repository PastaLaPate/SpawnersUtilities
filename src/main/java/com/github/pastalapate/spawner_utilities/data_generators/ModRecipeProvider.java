package com.github.pastalapate.spawner_utilities.data_generators;

import com.github.pastalapate.spawner_utilities.init.ModBlocks;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> cons) {
        // Soul Container
        ShapedRecipeBuilder.shaped(ModItems.SOUL_CONTAINER.get())
                .define('X', Tags.Items.STONE)
                .define('/', Items.SOUL_SAND)
                .pattern("XXX")
                .pattern("X/X")
                .pattern("XXX")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // BASIC UPGRADE
        ShapedRecipeBuilder.shaped(ModItems.BASIC_UPGRADE.get())
                .define('X', Items.DIAMOND_BLOCK)
                .define('/', Items.BLACKSTONE)
                .pattern("XXX")
                .pattern("X/X")
                .pattern("XXX")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // ENTITY UPGRADE
        ShapedRecipeBuilder.shaped(ModItems.ENTITY_UPGRADE.get())
                .define('X', ModItems.BASIC_UPGRADE.get())
                .define('#', Items.EMERALD)
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // FE SPAWNER
        ShapedRecipeBuilder.shaped(ModBlocks.FE_SPAWNER.get())
                .define('#', ModBlocks.SPAWNER_BASE.get())
                .define('X', tag("ingots/copper"))
                .define('I', Tags.Items.INGOTS_IRON)
                .pattern("XIX")
                .pattern("I#I")
                .pattern("XIX")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // FE SPAWNER TIER 2
        ShapedRecipeBuilder.shaped(ModBlocks.FE_SPAWNER_TIER2.get())
                .define('#', Items.EMERALD_BLOCK)
                .define('X', ModBlocks.FE_SPAWNER_TIER2.get())
                .define('I', tag("plates/copper"))
                .pattern("XIX")
                .pattern("I#I")
                .pattern("XIX")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // RANGE UPGRADE
        ShapedRecipeBuilder.shaped(ModItems.RANGE_UPGRADE.get())
                .define('#', ModItems.BASIC_UPGRADE.get())
                .define('X', Tags.Items.DUSTS_REDSTONE)
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // SOUL CAPTURER
        ShapedRecipeBuilder.shaped(ModItems.SOUL_CAPTURER.get())
                .define('#', Items.SOUL_SAND)
                .define('/', Items.STICK)
                .pattern("  #")
                .pattern(" / ")
                .pattern("/  ")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // SPAWNER BASE
        ShapedRecipeBuilder.shaped(ModBlocks.SPAWNER_BASE.get())
                .define('#', Items.IRON_BARS)
                .define('/', ModItems.SOUL_CONTAINER.get())
                .pattern("###")
                .pattern("#/#")
                .pattern("###")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);

        // SPEED UPGRADE
        ShapedRecipeBuilder.shaped(ModItems.SPEED_UPGRADE.get())
                .define('X', Tags.Items.GEMS_DIAMOND)
                .define('#', ModItems.BASIC_UPGRADE.get())
                .pattern(" X ")
                .pattern("X#X")
                .pattern(" X ")
                .unlockedBy("has_soulsand", has(Items.SOUL_SAND))
                .save(cons);
    }

    private static Tags.IOptionalNamedTag<Item> tag(String name)
    {
        return ItemTags.createOptional(new ResourceLocation("forge", name));
    }
}
