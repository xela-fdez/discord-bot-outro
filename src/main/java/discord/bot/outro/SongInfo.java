package discord.bot.outro;

public class SongInfo {
	private String url;
	private int start;
	private int kick;
	private int disconnect;
	
	public SongInfo(String url, int start, int kick, int disconnect) {
		this.url=url;
		this.start=start;
		this.kick=kick;
		this .disconnect=disconnect;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getKick() {
		return kick;
	}

	public void setKick(int kick) {
		this.kick = kick;
	}
	
	public int getDisconnect() {
		return disconnect;
	}

	public void setDisconnect(int disconnect) {
		this.disconnect = disconnect;
	}
	
}
