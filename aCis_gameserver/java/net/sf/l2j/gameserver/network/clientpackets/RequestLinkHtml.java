package net.sf.l2j.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.network.FloodProtectors;
import net.sf.l2j.gameserver.network.FloodProtectors.Action;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public final class RequestLinkHtml extends L2GameClientPacket
{
    private String _link;

    @Override
    protected void readImpl()
    {
        _link = readS();
    }

    @Override
    public void runImpl()
    {
        if (_link.isEmpty())
            return;

        if (!FloodProtectors.performAction(getClient(), Action.SERVER_BYPASS))
            return;

        if (_link.contains("..") || !(_link.startsWith("npc_") || _link.contains(".htm")))
            return;

        final Player player = getClient().getActiveChar();
        if (player == null)
            return;

        if (!player.validateBypass(_link))
            return;

        if (_link.startsWith("npc_"))
        {
            int endOfId = _link.indexOf('_', 5);
            String id;
            if (endOfId > 0)
                id = _link.substring(4, endOfId);
            else
                id = _link.substring(4);

            try
            {
                final WorldObject object = World.getInstance().getObject(Integer.parseInt(id));
                
                if (object != null && object instanceof Npc && endOfId > 0 && ((Npc) object).canInteract(player))
                {
                    String endOfNpc = _link.substring(endOfId + 1);

                    if (endOfNpc.startsWith("Loto_"))
                        endOfNpc = "Loto " + endOfNpc.substring(5);
                    else
                        endOfNpc = "Link " + endOfNpc;

                    ((Npc) object).onBypassFeedback(player, endOfNpc);
                }
            }
            catch (NumberFormatException nfe)
            {
            }

            return;
        }

        final NpcHtmlMessage html = new NpcHtmlMessage(0);
        html.setFile("data/html/" + _link);
        sendPacket(html);
    }
}