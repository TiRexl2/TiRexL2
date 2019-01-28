    /*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.l2j.gameserver.scripting.quests;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q024_InhabitantsOfTheForestOfTheDead extends Quest
{
    private static final String qn = "Q024_InhabitantsOfTheForestOfTheDead";
    
    // NPCs
    private static final int DORIAN = 31389;
    private static final int MYSTERIOUS_WIZARD = 31522;
    private static final int TOMBSTONE = 31531;
    private static final int LIDIA_MAID = 31532;
    
    // Items
    private static final int LIDIA_LETTER = 7065;
    private static final int LIDIA_HAIRPIN = 7148;
    private static final int SUSPICIOUS_TOTEM_DOLL = 7151;
    private static final int FLOWER_BOUQUET = 7152;
    private static final int SILVER_CROSS_OF_EINHASAD = 7153;
    private static final int BROKEN_SILVER_CROSS_OF_EINHASAD = 7154;
    private static final int TOTEM = 7156;
    
    // Monsters
    private static final int[] MOBS =
    {
        21557,
        21558,
        21560,
        21563,
        21564,
        21565,
        21566,
        21567
    };
    
    public Q024_InhabitantsOfTheForestOfTheDead()
    {
        super(24, "Inhabitants of the Forest of the Dead");
        
        setItemsIds(LIDIA_LETTER, LIDIA_HAIRPIN, SUSPICIOUS_TOTEM_DOLL, FLOWER_BOUQUET, SILVER_CROSS_OF_EINHASAD, BROKEN_SILVER_CROSS_OF_EINHASAD);
        
        addStartNpc(DORIAN);
        addTalkId(DORIAN, MYSTERIOUS_WIZARD, TOMBSTONE, LIDIA_MAID);
        addKillId(MOBS);
        
    }
    
    @Override
    public String onAdvEvent(String event, Npc npc, Player player)
    {
        QuestState st = player.getQuestState(qn);
        if (st == null)
            return null;
        
        String htmltext = event;
        switch (event)
        {
            // Dorian
            case "31389-02.htm":
                final QuestState qs = player.getQuestState("23_LidiasHeart");
                if ((player.getLevel() > 65) && (qs != null) && qs.isCompleted())
                {
                    st.setState(STATE_STARTED);
                    st.giveItems(FLOWER_BOUQUET, 1);
                    return "31389-03.htm";
                }
                break;
            case "31389-08.html":
                st.set("var", "1");
                break;
            case "31389-13.html":
                st.giveItems(SILVER_CROSS_OF_EINHASAD, 1);
                st.set("cond", "3");
                st.unset("var");
                break;
            case "31389-18.html":
                break;
            case "31389-19.html":
                if (!st.hasQuestItems(BROKEN_SILVER_CROSS_OF_EINHASAD))
                {
                    return getNoQuestMsg();
                }
                st.takeItems(BROKEN_SILVER_CROSS_OF_EINHASAD, -1);
                st.set("cond", "5");
                break;
            case "31389-06.html":
            case "31389-07.html":
            case "31389-10.html":
            case "31389-11.html":
            case "31389-12.html":
            case "31389-16.html":
            case "31389-17.html":
                break;
            // Lidia Maid
            case "31532-04.html":
                st.giveItems(LIDIA_LETTER, 1);
                st.set("cond", "6");
                break;
            case "31532-07.html":
                if (st.getInt("cond") < 8)
                {
                    if (!st.hasQuestItems(LIDIA_HAIRPIN) || !st.hasQuestItems(LIDIA_LETTER))
                    {
                        return getNoQuestMsg();
                    }
                    st.takeItems(LIDIA_HAIRPIN, -1);
                    st.takeItems(LIDIA_LETTER, -1);
                    st.set("var", "1");
                    htmltext = "31532-06.html";
                }
                else if (st.getInt("cond") < 6)
                {
                    st.set("cond", "7");
                }
                break;
            case "31532-10.html":
                st.set("var", "2");
                break;
            case "31532-14.html":
                st.set("var", "3");
                break;
            case "31532-19.html":
                st.unset("var");
                st.set("cond", "9");
                break;
            case "31532-02.html":
            case "31532-03.html":
            case "31532-09.html":
            case "31532-12.html":
            case "31532-13.html":
            case "31532-15.html":
            case "31532-16.html":
            case "31532-17.html":
            case "31532-18.html":
                break;
            // Mysterious Wizard
            case "31522-03.html":
                if (!st.hasQuestItems(SUSPICIOUS_TOTEM_DOLL))
                {
                    return getNoQuestMsg();
                }
                st.takeItems(SUSPICIOUS_TOTEM_DOLL, 1);
                st.set("var", "1");
                break;
            case "31522-08.html":
                st.unset("var");
                st.set("cond", "11");
                break;
            case "31522-17.html":
                st.set("var", "1");
                break;
            case "31522-21.html":
                st.giveItems(TOTEM, 1);
                st.rewardExpAndSp(242105, 22529); // GoD: Harmony: 6191140 exp and 6118650 sp
                st.exitQuest(false);
                break;
            case "31522-02.html":
            case "31522-05.html":
            case "31522-06.html":
            case "31522-07.html":
            case "31522-10.html":
            case "31522-11.html":
            case "31522-12.html":
            case "31522-13.html":
            case "31522-14.html":
            case "31522-15.html":
            case "31522-16.html":
            case "31522-19.html":
            case "31522-20.html":
                break;
            // Tombstone
            case "31531-02.html":
                if (!st.hasQuestItems(FLOWER_BOUQUET))
                {
                    return getNoQuestMsg();
                }
                st.takeItems(FLOWER_BOUQUET, -1);
                st.set("cond", "2");
                break;
            default:
                htmltext = null;
                break;
        }
        return htmltext;
    }
    
    @Override
    public String onKill(Npc npc, Player player, boolean isSummon)
    {
        QuestState st = player.getQuestState(qn);
        
        if ((st != null) && st.getInt("cond") < 9 && (Rnd.get(100) < 10))
        {
            st.giveItems(SUSPICIOUS_TOTEM_DOLL, 1);
            st.set("cond", "10");
        }
        return super.onKill(npc, player, isSummon);
    }
    
    @Override
    public String onTalk(Npc npc, Player player)
    {
        String htmltext = getNoQuestMsg();
        QuestState st = player.getQuestState(qn);
        
        if (st == null)
            return htmltext;
        
        switch (npc.getNpcId())
        {
            case DORIAN:
                switch (st.getState())
                {
                    case STATE_CREATED:
                        htmltext = "31389-01.htm";
                        break;
                    case STATE_STARTED:
                        switch (st.getInt("cond"))
                        {
                            case 1:
                                htmltext = "31389-04.html";
                                break;
                            case 2:
                                htmltext = (st.getInt("var") == 0) ? "31389-05.html" : "31389-09.html";
                                break;
                            case 3:
                                htmltext = "31389-14.html";
                                break;
                            case 4:
                                htmltext = "31389-15.html";
                                break;
                            case 5:
                                htmltext = "31389-20.html";
                                break;
                            case 6:
                            case 8:
                                htmltext = "31389-22.html";
                                break;
                            case 7:
                                st.giveItems(LIDIA_HAIRPIN, 1);
                                st.set("cond", "8");
                                htmltext = "31389-21.html";
                                break;
                        }
                        break;
                    case STATE_COMPLETED:
                        htmltext = getAlreadyCompletedMsg();
                        break;
                }
                break;
            case MYSTERIOUS_WIZARD:
                if (st.isStarted())
                {
                    if (st.getInt("cond") < 10)
                    {
                        htmltext = (st.getInt("var") == 0) ? "31522-01.html" : "31522-04.html";
                    }
                    else if (st.getInt("cond") < 11)
                    {
                        htmltext = (st.getInt("var") == 0) ? "31522-09.html" : "31522-18.html";
                    }
                }
                else if (st.isCompleted())
                {
                    final QuestState qs = player.getQuestState("25_HidingBehindTheTruth");
                    if (!((qs != null) && (qs.isStarted() || qs.isStarted())))
                    {
                        htmltext = "31522-22.html";
                    }
                }
                break;
            case TOMBSTONE:
                if (st.isStarted())
                {
                    if (st.getInt("cond") < 1)
                    {
                        htmltext = "31531-01.html";
                    }
                    else if (st.getInt("cond") < 2)
                    {
                        htmltext = "31531-03.html";
                    }
                }
                break;
            case LIDIA_MAID:
                if (st.isStarted())
                {
                    switch (st.getInt("cond"))
                    {
                        case 5:
                            htmltext = "31532-01.html";
                            break;
                        case 6:
                            htmltext = "31532-05.html";
                            break;
                        case 7:
                            htmltext = "31532-07a.html";
                            break;
                        case 8:
                            switch (st.getInt("var"))
                            {
                                case 0:
                                    htmltext = "31532-07a.html";
                                    break;
                                case 1:
                                    htmltext = "31532-08.html";
                                    break;
                                case 2:
                                    htmltext = "31532-11.html";
                                    break;
                                case 3:
                                    htmltext = "31532-15.html";
                                    break;
                            }
                            break;
                        case 9:
                        case 10:
                            htmltext = "31532-20.html";
                            break;
                    }
                }
                break;
        }
        return htmltext;
    }
}
