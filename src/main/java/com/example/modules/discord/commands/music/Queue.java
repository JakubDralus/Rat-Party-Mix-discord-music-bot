package com.example.modules.discord.commands.music;

import com.example.modules.audioplayer.PlayerManager;
import com.example.modules.discord.commands.ISlashCommand;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class Queue implements ISlashCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(Queue.class);
    
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = PlayerManager.get();
    
        // set event for scheduler to make him display a current track being played
        playerManager.getMusicManager(event.getGuild()).getScheduler().setEvent(event);
    
        BlockingQueue<AudioTrack> queue = playerManager.getMusicManager(event.getGuild()).getScheduler().getQueue();
        StringBuilder tracks = new StringBuilder();
        AtomicInteger i = new AtomicInteger();
        queue.forEach(track -> {
            i.getAndIncrement();
            tracks.append(i).append(" ").append(track.getInfo().title).append("\n");
        });
        
        event.replyEmbeds(new EmbedBuilder()
                .setTitle("Queue:")
                .setDescription("size: " + (queue.size()) + "\n")
                .appendDescription(tracks.toString())
                .build()
        ).queue();
    
        LOGGER.info("used /queue command in {}", event.getChannel().getName());
    }
}
