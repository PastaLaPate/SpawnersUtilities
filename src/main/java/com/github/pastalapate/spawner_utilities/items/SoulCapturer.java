package com.github.pastalapate.spawner_utilities.items;

import com.github.pastalapate.spawner_utilities.Main;
import com.github.pastalapate.spawner_utilities.init.ModGroup;
import com.github.pastalapate.spawner_utilities.init.ModItems;
import com.github.pastalapate.spawner_utilities.networking.ModMessages;
import com.github.pastalapate.spawner_utilities.networking.packets.DamageSyncC2SPacket;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import org.apache.logging.log4j.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class SoulCapturer extends Item {
    public final static List<Class<?>> excludedEntities = new ArrayList<>();

    static {
        excludedEntities.add(WitherEntity.class);
        excludedEntities.add(EnderDragonEntity.class);
        excludedEntities.add(GhastEntity.class);
    }

    private final ImmutableMultimap<Attribute, AttributeModifier> attributesModifiers;

    public SoulCapturer() {
        super(new Properties().tab(ModGroup.instance).durability(5));
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"), "Weapon modifier", 4, AttributeModifier.Operation.ADDITION));
        this.attributesModifiers =  builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        return attributesModifiers;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack itemWhoHurt, LivingEntity hurted, LivingEntity author) {
        ServerPlayerEntity player = (ServerPlayerEntity) author;
        ItemStack container = null;
        for (ItemStack item : player.inventory.items) {
            if (item.getDescriptionId().equals(ModItems.SOUL_CONTAINER.get().getDescriptionId())) {
                if (!item.getOrCreateTag().contains("entity")) {
                    container = item;
                    break;
                }
            }
        }
        if (!excludedEntities.contains(hurted.getClass()) && hurted.getHealth() <= 4f && container != null) {
            CompoundNBT nbt = container.getOrCreateTag();
            nbt.putString("entity", Objects.requireNonNull(hurted.getType().getRegistryName()).toString());
            itemWhoHurt.setTag(nbt);
            hurted.kill();
            Main.LOGGER.printf(Level.DEBUG, "Damage : %s Max damage %s", itemWhoHurt.getDamageValue(), itemWhoHurt.getMaxDamage());
            ModMessages.sendToServer(new DamageSyncC2SPacket(1, itemWhoHurt));
        }
        return super.hurtEnemy(itemWhoHurt, hurted, author);
    }
}
