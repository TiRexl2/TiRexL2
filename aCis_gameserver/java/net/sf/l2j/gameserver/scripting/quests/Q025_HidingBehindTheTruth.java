package net.sf.l2j.gameserver.scripting.quests;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.ai.CtrlIntention;
import net.sf.l2j.gameserver.model.actor.instance.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q025_HidingBehindTheTruth extends Quest
{
    private final static String qn = "Q025_HidingBehindTheTruth";
    
    //Items
    private static final int Contract = 7066;
    private static final int Dress = 7155;
    private static final int SuspiciousTotem = 7156;
    private static final int GemstoneKey = 7157;
    private static final int TotemDoll = 7158;
    
    //NPCs
    private static final int Agripel = 31348;
    private static final int Benedict = 31349;
    private static final int Wizard = 31522;
    private static final int Tombstone = 31531;
    private static final int Lidia = 31532;
    private static final int Bookshelf = 31533;
    private static final int Bookshelf2 = 31534;
    private static final int Bookshelf3 = 31535;
    private static final int Coffin = 31536;
    private static final int Triol = 27218;
    
    public Q025_HidingBehindTheTruth()
    {
        super(25, "Hiding Behind The Truth");
        setItemsIds(Contract,  Dress, SuspiciousTotem,  GemstoneKey, TotemDoll);
        addStartNpc(Benedict);
        addTalkId(Agripel,Benedict,Wizard,Tombstone,Lidia,Bookshelf,Bookshelf2,Bookshelf3,Coffin,Triol);
    }
    
    @Override
	public String onAdvEvent(String event, Npc npc, Player player)
    {
        String htmltext = event;
        QuestState st = player.getQuestState(qn);
        if (st == null)
            return htmltext;
        
        if (event.equalsIgnoreCase("31349-02.htm"))
        {
            st.playSound(QuestState.SOUND_ACCEPT);
            st.set("cond","1");
            st.setState(STATE_STARTED);
        }
        else if (event.equalsIgnoreCase("31349-03.htm"))
        {
            if (!st.hasQuestItems(SuspiciousTotem))
                htmltext = "31349-05.htm";
            else
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","2");
            }
        }
        else if (event.equalsIgnoreCase("31349-10.htm"))
        {
            st.playSound(QuestState.SOUND_MIDDLE);
            st.set("cond","4");
        }
        else if (event.equalsIgnoreCase("31348-02.htm"))
        {
            st.takeItems(SuspiciousTotem,-1);
        }
        else if (event.equalsIgnoreCase("31348-07.htm"))
        {
            st.playSound(QuestState.SOUND_MIDDLE);
            st.set("cond","5");
            st.giveItems(GemstoneKey,1);
        }
        else if (event.equalsIgnoreCase("31522-04.htm"))
        {
            st.playSound(QuestState.SOUND_MIDDLE);
            st.set("cond","6");
        }
        else if (event.equalsIgnoreCase("31535-03.htm"))
        {
            if (st.getInt("step") == 0)
            {
                st.set("step","1");
                Npc triol = addSpawn(Triol,59712,-47568,-2712,0,false,300000,true);
                triol.broadcastNpcSay( "That box was sealed by my master. Don't touch it!");
                triol.setRunning();
                triol.getAI().setIntention(CtrlIntention.ATTACK, player);
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","7");
            }
            else if (st.getInt("step") == 2)
            {
                    htmltext = "31535-04.htm";
            }
            else if (event.equalsIgnoreCase("31535-05.htm"))
            {
                st.giveItems(Contract,1);
                st.takeItems(GemstoneKey,-1);
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","9");
            }
            else if (event.equalsIgnoreCase("31532-02.htm"))
            {
                st.takeItems(Contract,-1);
            }
            else if (event.equalsIgnoreCase("31532-06.htm"))
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","11");
            }
            else if (event.equalsIgnoreCase("31531-02.htm"))
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","12");
                addSpawn(Coffin,60104,-35820,-664,0,false,20000,true);
            }
            else if (event.equalsIgnoreCase("31532-18.htm"))
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","15");
            }
            else if (event.equalsIgnoreCase("31522-12.htm"))
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","16");
            }
            else if (event.equalsIgnoreCase("31348-10.htm"))
                st.takeItems(TotemDoll,-1);
            else if (event.equalsIgnoreCase("31348-15.htm"))
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","17");
            }
            else if (event.equalsIgnoreCase("31348-16.htm"))
            {
                st.playSound(QuestState.SOUND_MIDDLE);
                st.set("cond","18");
            }
            else if (event.equalsIgnoreCase("31532-20.htm"))
            {
                st.giveItems(905,2);
                st.giveItems(874,1);
                st.takeItems(7063,-1);
                st.rewardExpAndSp(572277,53750);
                st.unset("cond");
                st.exitQuest(false);
                st.playSound(QuestState.SOUND_FINISH);
            }
            else if (event.equalsIgnoreCase("31522-15.htm"))
            {
                st.giveItems(936,1);
                st.giveItems(874,1);
                st.takeItems(7063,-1);
                st.rewardExpAndSp(572277,53750);
                st.unset("cond");
                st.exitQuest(false);
                st.playSound(QuestState.SOUND_FINISH);
            }
        }
        return htmltext;
    }
    @Override
	public String onTalk(Npc npc, Player player)
    {
        String htmltext = getNoQuestMsg();
        QuestState st = player.getQuestState(qn);
        if (st == null)
            return htmltext;
        
        switch (st.getState())
        {
        case STATE_COMPLETED:
            htmltext = getAlreadyCompletedMsg();
        case STATE_CREATED:
        	QuestState st2 = player.getQuestState("Q024_InhabitantsOfTheForestOfTheDead");
            if (npc.getNpcId() == Benedict)
            {
                if (st2!=null && st2.getState() == STATE_COMPLETED && player.getLevel() >= 66)
                    htmltext = "31349-01.htm";
                else
                    htmltext = "31349-00.htm";
            }
        case STATE_STARTED:
            int cond = st.getInt("cond");
            if (npc.getNpcId() == Benedict)
            {
                if (cond == 1)
                    htmltext = "31349-02.htm";
                    else if (cond==2 || cond ==3)
                        htmltext = "31349-04.htm";
                    else if (cond == 4)
                        htmltext = "31349-10.htm";
            }
            else if (npc.getNpcId() == Wizard)
            {
                if (cond == 2)
                {
                    htmltext = "31522-01.htm";
                    st.playSound(QuestState.SOUND_MIDDLE);
                    st.set("cond","3");
                    st.giveItems(SuspiciousTotem,1);
                }
                else if (cond == 3)
                    htmltext = "31522-02.htm";
                else if (cond == 5)
                    htmltext = "31522-03.htm";
                else if (cond == 6)
                    htmltext = "31522-04.htm";
                else if (cond == 9)
                {
                    htmltext = "31522-05.htm";
                    st.playSound(QuestState.SOUND_MIDDLE);
                    st.set("cond","10");
                }
                else if (cond == 10)
                    htmltext = "31522-05.htm";
                else if (cond == 15)
                    htmltext = "31522-06.htm";
                else if (cond == 16)
                    htmltext = "31522-13.htm";
                else if (cond == 17)
                    htmltext = "31522-16.htm";
                else if (cond == 18)
                    htmltext = "31522-14.htm";
            }
            else if (npc.getNpcId() == Agripel)
            {
                if (cond == 4 )	
                    htmltext = "31348-01.htm";
                else if (cond == 5)
                    htmltext = "31348-08.htm";
                else if (cond == 16)
                    htmltext = "31348-09.htm";
                else if (cond == 17)
                    htmltext = "31348-17.htm";
                else if (cond == 18)
                    htmltext = "31348-18.htm";
            }
            else if (npc.getNpcId() == Bookshelf)
            {
                if (cond == 6)
                    htmltext = "31533-01.htm";
            }
            else if (npc.getNpcId() == Bookshelf2)
            {
                if (cond == 6)
                    htmltext = "31534-01.htm";
            }
            else if (npc.getNpcId() == Bookshelf3)
            {
                if (cond==6 || cond==7 || cond==8)
                    htmltext = "31535-01.htm";
                else if (cond == 9)
                    htmltext = "31535-06.htm";
            }
            else if (npc.getNpcId() == Lidia)
            {
                if (cond == 10)
                    htmltext = "31532-01.htm";
                else if (cond==11 || cond==12)
                    htmltext = "31532-06.htm";
                else if (cond == 13)
                {
                    htmltext = "31532-07.htm";
                    st.set("cond","14");
                    st.takeItems(Dress,-1);
                }
                else if (cond == 14)
                    htmltext = "31532-08.htm";
                else if (cond == 15)
                    htmltext = "31532-18.htm";
                else if (cond == 17)
                    htmltext = "31532-19.htm";
                else if (cond == 18)
                    htmltext = "31532-21.htm";
            }
            else if (npc.getNpcId() == Tombstone)
            {
                if (cond == 11 || cond ==12)
                    htmltext = "31531-01.htm";
                else if (cond == 13)
                    htmltext = "31531-03.htm";
            }
            else if (npc.getNpcId() == Coffin)
                if (cond == 12)
                {
                    htmltext = "31536-01.htm";
                    st.giveItems(Dress,1);
                    st.playSound(QuestState.SOUND_MIDDLE);
                    st.set("cond","13");
                    npc.deleteMe();
                }
        }
        
        return htmltext;
    }
    
    public String onKill(Npc npc, Player player)
    {
        QuestState st = checkPlayerCondition(player, npc, "cond", "1");
        if (st == null)
            return null;
        
        if (st.getInt("cond") == 7)
        {
            st.playSound(QuestState.SOUND_ITEMGET);
            st.set("cond","8");
            npc.broadcastNpcSay("You've ended my immortal life! You've protected by the feudal lord, aren't you?");
            st.giveItems(TotemDoll,1);
            st.set("step","2");
        }
        
        return null;
    }
}
