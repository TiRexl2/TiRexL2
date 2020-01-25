package net.sf.l2j.gameserver.handler.voicedcommandhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.LoginServerThread;
import net.sf.l2j.gameserver.handler.IVoicedCommandHandler;
import net.sf.l2j.gameserver.handler.VoicedCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

import java.io.IOException;


/**
 * Author: m095
 * Хандлер команд для конфигурации персонажа
 * EmuRT DevTeam
 **/

public class Configurator implements IVoicedCommandHandler
{
    private static final String[] VOICED_COMMANDS =
            {
                    "menu",
                    "autoloot",
                    "enableTrade",
                    "disableTrade",
                    "enableAutoloot",
                    "disableAutoloot",
                    "enableMessage",
                    "disableMessage",
                    "enableGainExp",
                    "disableGainExp",
            };

    public boolean useVoicedCommand(String command, Player activeChar, String target)
    {
        if (activeChar.isInOlympiadMode() || activeChar.isInCombat())
        {
            activeChar.sendMessage(SystemMessageId.getMessage(activeChar, SystemMessageId.MSG_NOT_ALLOWED_AT_THE_MOMENT));
            return true;
        }

        if (command.startsWith("menu"))
        {
            showMainPage(activeChar);
            return true;
        }
        /**        else if (command.startsWith("autoloot"))
        {
            if (!Config.AUTO_LOOT)
            {
                activeChar.notWorking(false);
                return true;
            }

            if (activeChar.doAutoLoot())
            {
                activeChar.enableAutoLoot(false);
                activeChar.sendMessage("AutoLoot is off.");
            }
            else
            {
            	player.addItem("Loot", Item.getId(), 1, this, true);
                activeChar.sendMessage("AutoLoot is on");
            }
        } **/
        else if (command.startsWith("enableTrade"))
        {
            activeChar.setTradeRefusal(false);
            showMainPage(activeChar);
            return true;
        }
        else if (command.startsWith("disableTrade"))
        {
            activeChar.setTradeRefusal(true);
            showMainPage(activeChar);
            return true;
        }
/**        else if (command.startsWith("enableAutoloot"))
        {
            if (Config.AUTO_LOOT)
                activeChar.doAutoLoot(true);
            else
                activeChar.sendMessage("You cannot Logout while you are a participant in a Festival.");
            showMainPage(activeChar);
            return true;
        }
        else if (command.startsWith("disableAutoloot"))
        {
            if (Config.AUTO_LOOT)
                activeChar.doAutoLoot(false);
            else
                activeChar.sendMessage("You cannot Logout while you are a participant in a Festival.");
            showMainPage(activeChar);
            return true;
        } **/
        else if (command.startsWith("enableGainExp"))
        {
            if (Config.ALLOW_EXP_GAIN_COMMAND)
                activeChar.setExpOn(true);
            else
                activeChar.sendMessage("You cannot Logout while you are a participant in a Festival.");
            showMainPage(activeChar);
            return true;
        }
        else if (command.startsWith("disableGainExp"))
        {
            if (Config.ALLOW_EXP_GAIN_COMMAND)
                activeChar.setExpOn(false);
            else
                activeChar.sendMessage("You cannot Logout while you are a participant in a Festival.");
            showMainPage(activeChar);
            return true;
        }
        else if (command.startsWith("enableMessage"))
        {
            activeChar.setInRefusalMode(false);
            showMainPage(activeChar);
            return true;
        }
        else if (command.startsWith("disableMessage")) try
        {
            activeChar.setInRefusalMode(true);
            showMainPage(activeChar);
            return true;
        }
        catch(IOException e) {        }
        return false;
    }

    private String getGainExpMode(Player activeChar)
    {
        String result = "ON";
        if (activeChar.getExpOn())
            result = "OFF";
        return result;
    }

    private String getTradeMode(Player activeChar)
    {
        String result = "OFF";
        if (activeChar.getTradeRefusal())
            result = "ON";
        return result;
    }

    private String getMessageMode(Player activeChar)
    {
        String result = "OFF";
        if (activeChar.isInRefusalMode())
            result = "ON";
        return result;
    }

    /**    private String getLootMode(Player activeChar)
    {
        String result = "OFF";
        if (activeChar.isAutoLootEnabled())
            result = "ON";
        return result;
    } 
     * @param activeChar **/

    private void showMainPage(Player activeChar)
    {
        int curOnline = World.getInstance().getPlayers().size();

        if(!Config.ALLOW_MENU)
            return;
        NpcHtmlMessage html = new NpcHtmlMessage(activeChar.getObjectId());
        html.setFile("data/html/menu.htm");
        html.replace("%notrade%", getTradeMode(activeChar));
        /**        html.replace("%autoloot%", getLootMode(activeChar)); **/
        html.replace("%nomsg%", getMessageMode(activeChar));
        html.replace("%gainexp%", getGainExpMode(activeChar));
        html.replace("%online%", curOnline);

        activeChar.sendPacket(html);

    }

    public String getDescription(String command)
    {
        if(command.equals("menu"))
            return "Выводит меню команд.";
        return "Подробно в .menu";
    }

    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
    public static void main(String [] args) {
        VoicedCommandHandler.getInstance().registerHandler(new Configurator());
    }

}