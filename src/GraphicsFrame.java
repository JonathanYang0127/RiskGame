import javax.swing.JFrame;

public class GraphicsFrame extends JFrame{
	
	public static void main(String[] args) {
		new GraphicsFrame();
	}
	
	public GraphicsFrame(){
		super();
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.add(new GraphicsPanel(this));
		this.pack();
		this.setVisible(true);
	}
}
