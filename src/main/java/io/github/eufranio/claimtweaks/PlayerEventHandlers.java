package io.github.eufranio.claimtweaks;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;

/**
 * Created by Frani on 14/01/2018.
 */
public class PlayerEventHandlers {

    @Listener
    public void onQuit(ClientConnectionEvent.Disconnect event, @Root Player p) {
        ClaimTweaks.restorePlayer(p.getUniqueId());
    }

    @Listener
    public void onJoin(ClientConnectionEvent.Join event, @Root Player p) {
        final Claim claim = GriefDefender.getCore().getClaimAt(p.getLocation());
        ClaimTweaks.updateSettings(claim, p.getUniqueId());
    }

}
