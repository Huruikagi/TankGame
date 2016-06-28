package tankgame.system.gui;

enum PanelCode {
	START,
	STAGE_SELECT,
	CONFIG,
	GAME;
	
	private String message;
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String pullMessage() {
		String cp = message;
		message = null;
		return cp;
	}
}