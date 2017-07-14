package me.vidsify.durexp.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VoteListener;
import com.vexsoftware.votifier.model.VotifierEvent;

public class Votifier implements VoteListener {

	public void voteMade(Vote vote) {
		System.out.println("Received: " + vote);
	}
	
	@EventHandler(priority=EventPriority.NORMAL)
	public void onVoteifierEvent(VotifierEvent event) {
		Vote vote = event.getVote();
		
		/*
		 * Process Vote record as you see fit
		 */
	}
}
