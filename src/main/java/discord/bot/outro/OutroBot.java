package discord.bot.outro;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.youtube.*;
import com.sedmelluq.discord.lavaplayer.track.AudioReference;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class OutroBot extends ListenerAdapter {
	private static long startTime;
	
	
	public static void main(String[] args) {

		String token = args[0]; // Replace args[0] for your API key
		
		EnumSet<GatewayIntent> intents = EnumSet.of(GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES,
				GatewayIntent.MESSAGE_CONTENT);

		// Start the JDA session with default mode (voice member cache)
		JDABuilder.createDefault(token, intents) // Use provided token from command line arguments
				.addEventListeners(new OutroBot()) // Start listening with this listener
				.setActivity(Activity.listening("outros epicardas")) // Inform users that we are jammin' it out
				.setStatus(OnlineStatus.DO_NOT_DISTURB) // Please don't disturb us while we're jammin'
				.enableCache(CacheFlag.VOICE_STATE) // Enable the VOICE_STATE cache to find a user's connected voice
													// channel
				.build(); // Login with these options
	}

	// Number of servers that is connected to, it posts when starting up the bot
	@Override
	public void onReady(ReadyEvent event) {
		startTime=System.currentTimeMillis();
		List<Guild> guilds = event.getJDA().getGuilds();
		Iterator<Guild> it = guilds.iterator();
		int guildCount = guilds.size();
		System.out.println("\nI'm running on " + guildCount + " servers!");
		while (it.hasNext())
			System.out.println(it.next());
	}

	// Number of guilds that is connected to, it posts new and old servers when a
	// new server appears
	@Override
	public void onGuildJoin(GuildJoinEvent event) {
		List<Guild> guilds = event.getJDA().getGuilds();
		Iterator<Guild> it = guilds.iterator();
		int guildCount = guilds.size();
		System.out.println("\nI'm running on " + guildCount + " servers!");
		while (it.hasNext())
			System.out.println(it.next());
	}

	// Number of guilds that is connected to, it posts remaining servers when bot is
	// removed from a server
	@Override
	public void onGuildLeave(GuildLeaveEvent event) {
		List<Guild> guilds = event.getJDA().getGuilds();
		Iterator<Guild> it = guilds.iterator();
		int guildCount = guilds.size();
		System.out.println("\nI'm running on " + guildCount + " servers!");
		while (it.hasNext())
			System.out.println(it.next());
	}

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        Message message = event.getMessage();
        User author = message.getAuthor();
        System.out.println(author.getName()+", "+message.getMember());
        String content = message.getContentRaw();
        // Ignore message if bot
        if (author.isBot())
            return;

        // We only want to handle message in Guilds
        if (!event.isFromGuild())
        {
            return;
        }
        
        String arg = null;

        if (content.startsWith("/outro"))
        {
        	if(content.equals("/outro")) arg = "random";
        	else arg = content.substring("/outro ".length());
            onCommand(event, arg, message.getMember());
        }
    }

    /**
     * Handle command with arguments.
     *
     * @param event
     *        The event for this command
     * @param guild
     *        The guild where its happening
     * @param arg
     *        The input argument
     */
	private void onCommand(MessageReceivedEvent event, String arg, Member user)
    {
        // Note: None of these can be null due to our configuration with the JDABuilder!
        Member member = event.getMember();                              // Member is the context of the user for the specific guild, containing voice state and roles
        GuildVoiceState voiceState = member.getVoiceState();            // Check the current voice state of the user
        AudioChannel channel = voiceState.getChannel();    				// Use the channel the user is currently connected to
        SongInfo song = null;
        String uptime = "I've been up for ";
        long uptimeTime = (System.currentTimeMillis()-startTime);

        if(arg.equals("uptime")) {
    		int year = 0;
    		int month = 0;
    		int day = 0;
    		int hour = 0;
    		int minutes = 0;
    		int seconds = 0;
    		
    		if(uptimeTime>31556952000L) {
	    		if(uptimeTime>31556952000L) {
	    			year = (int) (uptimeTime/31556952000L);
	        		uptimeTime = uptimeTime-(year*31556952000L);
	    		}
	    		if(uptimeTime>2629746000L) {
	    			month = (int) (uptimeTime/2629746000L);
	        		uptimeTime = uptimeTime-(month*2629746000L);
	    		}
	    		if(uptimeTime>86400000L) {
	    			day = (int) (uptimeTime/86400000L);
	        		uptimeTime = uptimeTime-(day*86400000L);
	    		}
	    		if(uptimeTime>3600000L) {
	    			hour = (int) (uptimeTime/3600000L);
	        		uptimeTime = uptimeTime-(hour*3600000L);
	    		}
	    		if(uptimeTime>60000L) {
	    			minutes = (int) (uptimeTime/60000L);
	        		uptimeTime = uptimeTime-(minutes*60000L);
	    		}
	    		seconds = (int) (uptimeTime/1000L);
    		}
    		else {
	    		if(uptimeTime>2629746000L) {
	    			month = (int) (uptimeTime/2629746000L);
	        		uptimeTime = uptimeTime-(month*2629746000L);
	    		}
	    		if(uptimeTime>86400000L) {
	    			day = (int) (uptimeTime/86400000L);
	        		uptimeTime = uptimeTime-(day*86400000L);
	    		}
	    		if(uptimeTime>3600000L) {
	    			hour = (int) (uptimeTime/3600000L);
	        		uptimeTime = uptimeTime-(hour*3600000L);
	    		}
	    		if(uptimeTime>60000L) {
	    			minutes = (int) (uptimeTime/60000L);
	        		uptimeTime = uptimeTime-(minutes*60000L);
	    		}
	    		seconds = (int) (uptimeTime/1000L);
    		}
    		
    		System.out.println(year+" years, "+month+" months, "+day+" days, "+hour+" hours, "+minutes+" minutes and "+seconds+" seconds");
    		uptime+=(year+" years, "+month+" months, "+day+" days, "+hour+" hours, "+minutes+" minutes and "+seconds+" seconds");
        }
        else if(arg.equals("1")) {
        	song = new SongInfo("jOTeBVtlnXU",33000,18500,26000);
        }
        else if(arg.equals("2")) {
        	song = new SongInfo("qv5wfdxYbc8",0,17000,23000);
        }
        else {
        	song = new SongInfo("3_-a9nVZYjk",45000,16000,24000);
        }
        
        if(song!=null) {
	        if (channel != null)
	        {
	            connectTo(channel, user, song);                       // Join the channel of the user
	            onConnecting(channel, event.getChannel());                  // Tell the user about our success
	        }
	        else
	        {
	            onUnknownChannel(event.getChannel(), "your voice channel"); // Tell the user about our failure
	        }
        }   
        else {
        	event.getChannel().sendMessage(uptime).queue();
        }
    }

    /**
     * Inform user about successful connection.
     *
     * @param channel
     *        The voice channel we connected to
     * @param messageChannel
     *        The text channel to send the message in
     */
    
    private void onConnecting(AudioChannel channel, MessageChannel messageChannel)
    {
        //if you want to perform any actions on connection, like saying "Playing Outro" in the MessageChannel it goes here
    }

    /**
     * The channel to connect to is not known to us.
     *
     * @param channel
     *        The message channel (text channel abstraction) to send failure information to
     * @param comment
     *        The information of this channel
     */
    private void onUnknownChannel(MessageChannel channel, String comment)
    {
        channel.sendMessage("No me he podido conectar al canal ``" + comment + "``").queue(); // never forget to queue()! // ``" + comment + "``, no such channel!
    }
	    
	    
	    
	    private void connectTo(AudioChannel channel, Member member, SongInfo song){
	    	
	    	
	    	
	    	AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
	    	AudioSourceManagers.registerRemoteSources(playerManager);
	    	AudioPlayer player = playerManager.createPlayer();
	    	
	    	TrackScheduler trackScheduler = new TrackScheduler();
	    	player.addListener(trackScheduler);
	    	
	    	YoutubeAudioSourceManager yt = new YoutubeAudioSourceManager();
	    	AudioTrack track = (AudioTrack) yt.loadItem(playerManager, new AudioReference(song.getUrl(),null));			//"3_-a9nVZYjk"
	    																												//"R1tgBxuRqZ8"
	    	System.out.println(track.getIdentifier());
	    	
	    	AudioPlayerSendHandler handler = new AudioPlayerSendHandler(player);
	    	 	
	    	Guild guild = channel.getGuild();
	        // Get an audio manager for this guild, this will be created upon first use for each guild
	        AudioManager audioManager = guild.getAudioManager();
	        // Create our Send/Receive handler for the audio connection
	        
	        track.setPosition(song.getStart());
	        player.playTrack(track);
	        audioManager.openAudioConnection(channel);
	        audioManager.setSendingHandler(handler);

		    trackScheduler.onTrackStart(player, track);
		    
		    Timer timer = new Timer();
		    timer.schedule(new Disconnect(guild,member), song.getKick());
		    timer.schedule(new DisconnectBot(audioManager), song.getDisconnect());

	    } 
	    
	    
	    public class AudioPlayerSendHandler implements AudioSendHandler {
	    	  private final AudioPlayer audioPlayer;
	    	  private final ByteBuffer buffer;
	    	  private final MutableAudioFrame frame;

	    	  public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
	    	    this.audioPlayer = audioPlayer;
	    	    this.buffer = ByteBuffer.allocate(1024);
	    	    this.frame = new MutableAudioFrame();
	    	    this.frame.setBuffer(buffer);
	    	  }

	    	  @Override
	    	  public boolean canProvide() {
	    	    return audioPlayer.provide(frame);
	    	  }

	    	  @Override
	    	  public ByteBuffer provide20MsAudio() {
	    	    ((Buffer) buffer).flip();
	    	    return buffer;
	    	  }

	    	  @Override
	    	  public boolean isOpus() {
	    	    return true;
	    	  }
	    	}

	    public class Disconnect extends TimerTask {
	    	private final Guild guild;
	    	private final Member member;
	    	
	    	Disconnect(Guild guild, Member member){
	    		this.guild=guild;
	    		this.member=member;
	    	}
	    	public void run() {
	    		guild.kickVoiceMember(member).queue();
	    	}	    	
	    }
	    
	    public class DisconnectBot extends TimerTask {
	    	private final AudioManager audioManager;
	    	
	    	DisconnectBot(AudioManager audioManager){
	    		this.audioManager=audioManager;
	    	}
	    	public void run() {
	    		audioManager.closeAudioConnection();
	    	}	    	
	    }
	    
	    
}