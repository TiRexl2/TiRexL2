package net.sf.l2j.gameserver.scripting.quests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.L2Skill;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.commons.random.Rnd;

public class Q501_ProofOfClanAlliance extends Quest
{
    private static final String qn = "Q501_ProofOfClanAlliance";
    
    // Items
    private static final int ADENA = 57;
    private static final int POTION_OF_RECOVERY = 3889;
    
    // Quest Items
    private static final int HERB_OF_HARIT = 3832;
    private static final int HERB_OF_VANOR = 3833;
    private static final int HERB_OF_OEL_MAHUM = 3834;
    private static final int BLOOD_OF_EVA = 3835;
    private static final int ATHREAS_COIN = 3836;
    private static final int SYMBOL_OF_LOYALTY = 3837;
    private static final int VOUCHER_OF_FAITH = 3873;
    private static final int ANTIDOTE_RECIPE_LIST = 3872;
    
    // Reward
    private static final int PROOF_OF_ALLIANCE = 3874;
    
    // NPC
    private static final int SIR_KRISTOF_RODEMAI = 30756;
    private static final int STATUE_OF_OFFERING = 30757;
    private static final int ATHREA = 30758;
    private static final int KALIS = 30759;
    
    // Mobs
    private static final int VANOR_SILENOS_SHAMAN = 20685;
    private static final int HARIT_LIZARDMAN_SHAMAN = 20644;
    private static final int OEL_MAHUM_WITCH_DOCTOR = 20576;
    
    // Chests
    private static final int BOX_OF_ATHREA_1 = 27173;
    private static final int BOX_OF_ATHREA_2 = 27174;
    private static final int BOX_OF_ATHREA_3 = 27175;
    private static final int BOX_OF_ATHREA_4 = 27176;
    private static final int BOX_OF_ATHREA_5 = 27177;
    
    // Trigger
    private static boolean _isSpawned = false;
    
    // Drops
    private static final Map<Integer, Integer> DROP = new HashMap<>();
    {
        DROP.put(VANOR_SILENOS_SHAMAN, HERB_OF_VANOR);
        DROP.put(HARIT_LIZARDMAN_SHAMAN, HERB_OF_HARIT);
        DROP.put(OEL_MAHUM_WITCH_DOCTOR, HERB_OF_OEL_MAHUM);
    }
    
    // Chests spawns
    private static final int[][] CHESTS_SPAWN =
    {
        {102273, 103433, -3512},
        {102190, 103379, -3524},
        {102107, 103325, -3533},
        {102024, 103271, -3500},
        {102327, 103350, -3511},
        {102244, 103296, -3518},
        {102161, 103242, -3529},
        {102078, 103188, -3500},
        {102381, 103267, -3538},
        {102298, 103213, -3532},
        {102215, 103159, -3520},
        {102132, 103105, -3513},
        {102435, 103184, -3515},
        {102352, 103130, -3522},
        {102269, 103076, -3533},
        {102186, 103022, -3541}
    };
    
    // Chests
    private static final List<Integer> CHESTS_ID = new ArrayList<>();
    {
        CHESTS_ID.add(BOX_OF_ATHREA_1);
        CHESTS_ID.add(BOX_OF_ATHREA_2);
        CHESTS_ID.add(BOX_OF_ATHREA_3);
        CHESTS_ID.add(BOX_OF_ATHREA_4);
        CHESTS_ID.add(BOX_OF_ATHREA_5);
    }
    
    public Q501_ProofOfClanAlliance()
    {
        super(501, "Proof Of Clan Alliance");
        
        setItemsIds(HERB_OF_HARIT, HERB_OF_VANOR, HERB_OF_OEL_MAHUM, BLOOD_OF_EVA, ATHREAS_COIN, SYMBOL_OF_LOYALTY, VOUCHER_OF_FAITH, ANTIDOTE_RECIPE_LIST);
        
        addStartNpc(SIR_KRISTOF_RODEMAI, STATUE_OF_OFFERING);
        addTalkId(SIR_KRISTOF_RODEMAI, KALIS, STATUE_OF_OFFERING, ATHREA);
        
        addKillId(VANOR_SILENOS_SHAMAN, HARIT_LIZARDMAN_SHAMAN, OEL_MAHUM_WITCH_DOCTOR, BOX_OF_ATHREA_1, BOX_OF_ATHREA_2, BOX_OF_ATHREA_3, BOX_OF_ATHREA_4, BOX_OF_ATHREA_5);
    }
    
    @Override
    public String onAdvEvent(String event, Npc npc, Player player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(qn);
        QuestState st2 = getClanLeaderQuestState(player, npc);
        
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("30756-07.htm"))
        {
            st.setState(STATE_STARTED);
            st.set("cond", "1");
            st.set("state", "1");
            st.playSound(QuestState.SOUND_ACCEPT);
        }
        else if (event.equalsIgnoreCase("30759-03.htm"))
        {
            st.set("cond", "2");
            st.set("state", "2");
            st.playSound(QuestState.SOUND_MIDDLE);
        }
        else if (event.equalsIgnoreCase("30759-07.htm"))
        {
            st.set("cond", "3");
            st.set("state", "3");
            st.takeItems(SYMBOL_OF_LOYALTY, 1);
            st.takeItems(SYMBOL_OF_LOYALTY, 1);
            st.takeItems(SYMBOL_OF_LOYALTY, 1);
            st.giveItems(ANTIDOTE_RECIPE_LIST, 1);
            SkillTable.getInstance().getInfo(4082, 1).getEffects(npc, player);
            startQuestTimer("poison", 60000, npc, player, true);
            st.playSound(QuestState.SOUND_MIDDLE);
        }
        else if (event.equalsIgnoreCase("30757-03.htm"))
        {
            if (Rnd.get(10) > 5)
            {
                st.setState(STATE_STARTED);
                st.set("symbol", "1");
                
                st2.set("symbols", String.valueOf(st2.getInt("symbols") + 1));
                
                st.giveItems(SYMBOL_OF_LOYALTY, 1);
                st.playSound(QuestState.SOUND_ACCEPT);
                htmltext = "30757-04.htm";
            }
            else
            {
                castSkill(npc, player, 4083);
                startQuestTimer("die", 4000, npc, player, false);
            }
        }
        else if (event.equalsIgnoreCase("30758-03.htm"))
        {
            if (!_isSpawned && player.getAdena() >= 10000)
            {
                st2.set("state", "4");
                st2.set("bingo", "0");
                st2.set("chests", "0");
                
                st.takeItems(ADENA, 10000);
                
                for (int[] coords : CHESTS_SPAWN)
                    addSpawn(CHESTS_ID.get(Rnd.get(CHESTS_ID.size())), coords[0], coords[1], coords[2], 0, false, 0, false);
                
                _isSpawned = true;
                startQuestTimer("despawn", 300000, null, player, false);
            }
            else
                htmltext = "30758-03a.htm";
        }
        else if (event.equalsIgnoreCase("30758-07.htm"))
        {
            if (player.getAdena() >= 10000)
            {
                if (!_isSpawned)
                    st.takeItems(ADENA, 10000);
            }
            else
                htmltext = "30758-06.htm";
        }
        // Timers
        else if (event.equals("die"))
        {
            st.setState(STATE_STARTED);
            st.set("symbol", "1");
            
            st2.set("symbols", String.valueOf(st2.getInt("symbols") + 1));
            
            st.giveItems(SYMBOL_OF_LOYALTY, 1);
            st.playSound(QuestState.SOUND_ACCEPT);
            return null;
        }
        else if (event.equals("poison"))
        {
            if (player.getAbnormalEffect() != 514)
            {
                player.sendMessage("Are you noob?");
                cancelQuestTimer("poison", npc, player); // Cancel check timer
            }
            
            return null;
        }
        else if (event.equals("despawn"))
        {
            _isSpawned = false;
            return null;
        }
        
        return htmltext;
    }
    
    @Override
    public String onTalk(Npc npc, Player player)
    {
        String htmltext = getNoQuestMsg();
        QuestState st = player.getQuestState(qn);
        QuestState cl = getClanLeaderQuestState(player, npc);
        
        if (st == null)
            return htmltext;
        
        switch (st.getState())
        {
            case STATE_CREATED:
                switch (npc.getNpcId())
                {
                    case SIR_KRISTOF_RODEMAI:
                        if (player.isClanLeader())
                        {
                            if (player.getClan().getLevel() == 3)
                            {
                                if (st.hasQuestItems(PROOF_OF_ALLIANCE))
                                    htmltext = "30756-03.htm";
                                else
                                    htmltext = "30756-04.htm";
                            }
                            else if (player.getClan().getLevel() < 3)
                                htmltext = "30756-01.htm";
                            else
                                htmltext = "30756-02.htm";
                        }
                        else
                            htmltext = "30756-05.htm";
                        break;
                        
                    case STATUE_OF_OFFERING:
                        if (cl != null && cl.getInt("state") == 2 && cl.getInt("symbols") < 3)
                        {
                            if (!player.isClanLeader())
                            {
                                if (player.getLevel() > 39)
                                    htmltext = "30757-01.htm";
                                else
                                    htmltext = "30757-02.htm";
                            }
                            else
                                htmltext = "30757-01a.htm";
                        }
                        else if (player.getClan() != null)
                            htmltext = "30757-06.htm";
                        break;
                }
                break;
            
            case STATE_STARTED:
                int state = st.getInt("state");
                switch (npc.getNpcId())
                {
                    case SIR_KRISTOF_RODEMAI:
                        if (state == 6 && st.hasQuestItems(VOUCHER_OF_FAITH))
                        {
                            htmltext = "30756-09.htm";
                            st.rewardExpAndSp(0, 120000);
                            st.takeItems(VOUCHER_OF_FAITH, -1);
                            st.giveItems(PROOF_OF_ALLIANCE, 1);
                            st.playSound(QuestState.SOUND_FINISH);
                            st.exitQuest(true);
                        }
                        else if (state > 0)
                            htmltext = "30756-10.htm";
                        break;
                        
                    case STATUE_OF_OFFERING:
                        if (cl != null && cl.getInt("state") == 2 && st.getInt("symbol") == 1)
                            htmltext = "30757-01b.htm";
                        break;
                        
                    case KALIS:
                        if (player.isClanLeader())
                        {
                            if (state == 1 && !st.hasQuestItems(SYMBOL_OF_LOYALTY))
                                htmltext = "30759-01.htm";
                            else if (state == 2)
                            {
                                if (st.getQuestItemsCount(SYMBOL_OF_LOYALTY) < 3)
                                    htmltext = "30759-05.htm";
                                else
                                    htmltext = "30759-06.htm";
                            }
                            else if (state > 2 && state < 6 && player.getAbnormalEffect() != 514)
                            {
                                st.set("cond", "1");
                                st.set("state", "1");
                                st.takeItems(ANTIDOTE_RECIPE_LIST, -1);
                                htmltext = "30759-09.htm";
                            }
                            else if (state > 2 && state < 6 && player.getAbnormalEffect() == 514 && !st.hasAtLeastOneQuestItem(HERB_OF_HARIT, HERB_OF_VANOR, HERB_OF_OEL_MAHUM, BLOOD_OF_EVA))
                                htmltext = "30759-10.htm";
                            else if (state == 5 && player.getAbnormalEffect() == 514 && st.hasAtLeastOneQuestItem(HERB_OF_HARIT, HERB_OF_VANOR, HERB_OF_OEL_MAHUM, BLOOD_OF_EVA))
                            {
                                st.set("cond", "4");
                                st.set("state", "6");
                                st.takeItems(ANTIDOTE_RECIPE_LIST, -1);
                                st.takeItems(HERB_OF_HARIT, -1);
                                st.takeItems(HERB_OF_VANOR, -1);
                                st.takeItems(HERB_OF_OEL_MAHUM, -1);
                                st.takeItems(BLOOD_OF_EVA, -1);
                                st.giveItems(VOUCHER_OF_FAITH, 1);
                                st.giveItems(POTION_OF_RECOVERY, 1);
                                st.playSound(QuestState.SOUND_MIDDLE);
                                cancelQuestTimer("poison", npc, player); // Cancel check timer
                                htmltext = "30759-08.htm";
                            }
                            else if (state == 6)
                                htmltext = "30759-11.htm";
                        }
                        else
                            htmltext = "30759-12.htm";
                        break;
                        
                    case ATHREA:
                        if (cl != null && cl.getInt("state") == 3 && cl.hasQuestItems(ANTIDOTE_RECIPE_LIST) && !cl.hasQuestItems(BLOOD_OF_EVA) && hasFirstHerb(st, cl.get("herbs")) && getHerbs(cl.get("herbs")).size() == 3)
                            htmltext = "30758-01.htm";
                        else if (cl != null && cl.getInt("state") == 4)
                        {
                            if (cl.getInt("bingo") == 4)
                            {
                                cl.set("state", "5");
                                st.giveItems(BLOOD_OF_EVA, 1);
                                htmltext = "30758-08.htm";
                                cl.unset("chests");
                                cl.unset("bingo");
                            }
                            else
                                htmltext = "30758-05.htm";
                        }
                        else if (cl != null && cl.getInt("state") == 5)
                            htmltext = "30758-09.htm";
                        break;
                }
                break;
        }
        
        return htmltext;
    }
    
    @Override
    public String onKill(Npc npc, Player player, boolean isPet)
    {
        QuestState st = player.getQuestState(qn);
        QuestState cl = getClanLeaderQuestState(player, npc);
        
        if (st == null || cl == null)
            return null;
        
        int npcId = npc.getNpcId();
        
        if (DROP.containsKey(npcId) && cl.getInt("state") == 3)
        {
            int itemId = DROP.get(npcId);
            
            List<Integer> herbs = getHerbs(cl.get("herbs"));
            
            if (Rnd.get(10) == 1 && !st.hasQuestItems(itemId) && !hasOtherItems(st, itemId) && !herbs.contains(itemId))
            {
                if (herbs.isEmpty())
                    cl.set("herbs", String.valueOf(itemId));
                else
                    cl.set("herbs", cl.get("herbs") + ";" + itemId);
                
                st.dropItemsAlways(itemId, 1, 1);
            }
        }
        else if (CHESTS_ID.contains(npcId) && cl.getInt("state") == 4)
        {
            int chests = cl.getInt("chests");
            int bingo = cl.getInt("bingo");
            
            if ((chests == 15 && bingo == 3 || chests == 14 && bingo == 2 || chests == 13 && bingo == 1 || chests == 12 && bingo == 0) || (bingo < 4 && Rnd.get(4) == 0))
            {
                npc.broadcastNpcSay("##########Bingo!##########");
                cl.set("bingo", String.valueOf(bingo + 1));
            }
            
            cl.set("chests", String.valueOf(chests + 1));
            
            if (chests == 16)
                _isSpawned = false;
        }
        
        return null;
    }
    
    public boolean hasOtherItems(QuestState st, int itemId)
    {
        switch (itemId)
        {
            case HERB_OF_VANOR:
                if (st.hasQuestItems(HERB_OF_HARIT) || st.hasQuestItems(HERB_OF_OEL_MAHUM))
                    return true;
                break;
                    
            case HERB_OF_HARIT:
                if (st.hasQuestItems(HERB_OF_VANOR) || st.hasQuestItems(HERB_OF_OEL_MAHUM))
                    return true;
                break;
                
            case HERB_OF_OEL_MAHUM:
                if (st.hasQuestItems(HERB_OF_HARIT) || st.hasQuestItems(HERB_OF_VANOR))
                    return true;
                break;
        }
        
        return false;
    }
    
    public void castSkill(Npc npc, Player player, int skillId)
    {
        L2Skill skill = SkillTable.getInstance().getInfo(skillId, 1);
        npc.setTarget(player);
        npc.doCast(skill);
    }
    
    @SuppressWarnings("static-method")
	private List<Integer> getHerbs(String list)
    {
        List<Integer> array = new ArrayList<>();
        
        if (list != null)
        {
            String[] herbs = list.split(";");
            
            for (String herb : herbs)
                array.add(Integer.parseInt(herb));
        }
        
        return array;
    }
    
    public boolean hasFirstHerb(QuestState st, String list)
    {
        if (list != null)
        {
            String[] herbs = list.split(";");
            
            if (st.hasQuestItems(Integer.parseInt(herbs[0])))
                return true;
        }
        
        return false;
    }
    
    @Override
	public QuestState getClanLeaderQuestState(Player player, Npc npc)
    {
        // If player is the leader, retrieves directly the qS and bypass others checks
        if (player.isClanLeader() && player.isInsideRadius(npc, Config.PARTY_RANGE, true, false))
            return player.getQuestState(getName());
 
        // Verify if the player got a clan
        Clan clan = player.getClan();
        if (clan == null)
            return null;
 
        // Verify if the leader is online
        Player leader = clan.getLeader().getPlayerInstance();
        if (leader == null)
            return null;
 
        // Verify if the player is on the radius of the leader. If true, send leader's quest state.
        if (leader.isInsideRadius(npc, Config.PARTY_RANGE, true, false))
            return leader.getQuestState(getName());
 
        return null;
    }
    
    public static void main(String[] args)
    {
        new Q501_ProofOfClanAlliance();
    }
}