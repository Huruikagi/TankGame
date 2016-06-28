package tankgame.system.gui;

import javax.swing.UIManager;

public class LkFFtest {

	public static void main(String[] args) {
	    UIManager.LookAndFeelInfo infos[] = UIManager.getInstalledLookAndFeels();

	    System.out.println("getClassName:");

	    for(int i = 0 ; i < infos.length ; i++){
	      System.out.println(infos[i].getClassName());
	    }

	    System.out.println("getName:");

	    for(int i = 0 ; i < infos.length ; i++){
	      System.out.println(infos[i].getName());
	    }
	}

}