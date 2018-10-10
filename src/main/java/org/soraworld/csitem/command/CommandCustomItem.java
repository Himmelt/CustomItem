package org.soraworld.csitem.command;

import org.soraworld.csitem.data.ItemAttrib;
import org.soraworld.violet.command.Args;
import org.soraworld.violet.command.SpongeCommand;
import org.soraworld.violet.command.Sub;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;

public final class CommandCustomItem {

    @Sub
    public static void test(SpongeCommand self, CommandSource sender, Args args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.getItemInHand(HandTypes.MAIN_HAND).ifPresent(stack -> {
                stack.getOrCreate(ItemAttrib.class).ifPresent(attrib -> {
                    if (args.notEmpty()) attrib.name = args.first();
                    stack.offer(attrib);
                });
                player.setItemInHand(HandTypes.MAIN_HAND, stack);
            });
        }
    }
}
