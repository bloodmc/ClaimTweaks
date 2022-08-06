package io.github.eufranio.claimtweaks;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.event.BorderClaimEvent;
import com.griefdefender.api.event.Event;
import io.github.eufranio.claimtweaks.config.ClaimStorage;
import com.griefdefender.lib.kyori.event.EventBus;
import com.griefdefender.lib.kyori.event.EventSubscriber;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Frani on 14/01/2018.
 */
public class ClaimEventHandlers {

    public ClaimEventHandlers() {
        new BorderClaimEventListener();
    }

    private class BorderClaimEventListener {

        public BorderClaimEventListener() {
            final EventBus<Event> eventBus = GriefDefender.getEventManager().getBus();

            eventBus.subscribe(BorderClaimEvent.class, new EventSubscriber<BorderClaimEvent>() {

                @Override
                public void on(BorderClaimEvent e) throws Throwable {
                    if (e.getExitClaim() == e.getEnterClaim()) {
                        return;
                    }

                    final User user = e.getUser();
                    if (user == null) {
                        return;
                    }

                    final Player player = Sponge.getServer().getPlayer(user.getUniqueId()).orElse(null);
                    if (player == null) {
                        return;
                    }

                    ClaimTweaks.updateSettings(e.getEnterClaim(), player.getUniqueId());

                    if (player.hasPermission("claimtweaks.bypass")) return;

                    Claim enterClaim = e.getEnterClaim();
                    ClaimStorage.Data enterData = ClaimStorage.of(enterClaim.getUniqueId());
                    if (enterData != null) {
                        for (String cmd : enterData.enterCommands) {
                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd.replace("%player%", player.getName()));
                        }
                        for (String cmd : enterData.playerEnterCommands) {
                            Sponge.getCommandManager().process(player, cmd);
                        }
                    }

                    Claim exitClaim = e.getExitClaim();
                    ClaimStorage.Data exitData = ClaimStorage.of(exitClaim.getUniqueId());
                    if (exitData != null) {
                        for (String cmd : exitData.leaveCommands) {
                            Sponge.getCommandManager().process(Sponge.getServer().getConsole(), cmd.replace("%player%", player.getName()));
                        }
                        for (String cmd : exitData.playerLeaveCommands) {
                            Sponge.getCommandManager().process(player, cmd);
                        }
                    }
                }
            });
        }
    }

}
