import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel tools;
	private JPanel files;
	private Canvas canvas;
	private JButton drag;
	private JButton scale;
	private JButton line;
	private JButton rectangle;
	private JButton ellipse;
	private JButton text;
	private JButton color;
	private JButton delete;
	private JButton open;
	private JButton save;
	static JTextField textContent=new JTextField(20);
	
	public Main(){
		super("MiniCAD");
		tools=new JPanel(new FlowLayout());
		files=new JPanel(new FlowLayout());
		canvas=new Canvas();
		drag=new JButton("移动");
		drag.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.tool=DrawStatus.DRAG;
			}
		});
		scale=new JButton("缩放");
		scale.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.tool=DrawStatus.SCALE;
			}
		});
		line=new JButton("直线");
		line.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.tool=DrawStatus.LINE;
			}
		});
		rectangle=new JButton("矩形");
		rectangle.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.tool=DrawStatus.RECTANGLE;
			}
		});
		ellipse=new JButton("椭圆");
		ellipse.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.tool=DrawStatus.ELLIPSE;
			}
		});
		text=new JButton("文本");
		text.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.tool=DrawStatus.TEXT;
			}
		});
		color=new JButton("颜色");
		color.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.curColor=JColorChooser.showDialog(null, "选择颜色", Color.BLACK);
				canvas.changeColorForCurShape();
			}
		});
		delete=new JButton("删除");
		delete.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				canvas.removeCurShape();
			}
		});
		open=new JButton("打开");
		open.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(null);
                File f = fc.getSelectedFile();
                try {
    				FileInputStream file = new FileInputStream(f);
					ObjectInputStream in = new ObjectInputStream(file);
	                Integer shapeNum = (Integer)in.readObject();
	                canvas.shapes.clear();
	                for(int i=0;i<shapeNum;i++){
	                	Shape tmp=(Shape)in.readObject();
	                	if(tmp!=null){
	                		if(tmp instanceof Line2D){
	                            canvas.shapes.add((Line2D)tmp);
	                        }
	                        else if(tmp instanceof Rectangle2D){
	                            canvas.shapes.add((Rectangle2D)tmp);
	                        }
	                        else if(tmp instanceof Ellipse2D){
	                            canvas.shapes.add((Ellipse2D)tmp);
	                        }
	                        else if(tmp instanceof Text2D){
	                            canvas.shapes.add((Text2D)tmp);
	                        }
	                        else{
	                            System.out.println("FATAL ERROR: File is damaged");
	                        }
	                	}
	                }
	                canvas.colors.clear();
	                for(int i=0;i<shapeNum;i++){
	                	Color tmp=(Color)in.readObject();
	                	if(tmp!=null){
	                		canvas.colors.add(tmp);
	                	}else{
	                		System.out.println("FATAL ERROR: File is damaged");
	                	}
	                }
	                canvas.strokes.clear();
	                for(int i=0;i<shapeNum;i++){
	                	Integer tmp=(Integer)in.readObject();
	                	if(tmp!=null){
	                		canvas.strokes.add(tmp);
	                	}else{
	                		System.out.println("FATAL ERROR: File is damaged");
	                	}
	                }
	                in.close();
	                file.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
                canvas.repaint();
			}
		});
		save=new JButton("保存");
		save.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
                fc.showSaveDialog(null);
                File f=fc.getSelectedFile();
                FileOutputStream file;
				try{
					file = new FileOutputStream(f);
	                ObjectOutputStream out = new ObjectOutputStream(file);
	                out.writeObject(canvas.shapes.size());
	                for(Shape s : canvas.shapes){
	                	out.writeObject(s);
	                }
	                for(Color c : canvas.colors){
	                	out.writeObject(c);
	                }
	                for(Integer i : canvas.strokes){
	                	out.writeObject(i);
	                }
					out.close();
	                file.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		});
		tools.add(drag);
		tools.add(scale);
		tools.add(color);
		tools.add(delete);
		tools.add(line);
		tools.add(rectangle);
		tools.add(ellipse);
		tools.add(text);
		files.add(open);
		files.add(save);
		tools.add(textContent);
		this.add(tools,BorderLayout.NORTH);
		this.add(canvas,BorderLayout.CENTER);
		this.add(files,BorderLayout.SOUTH);
	    setSize(800, 600);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setVisible(true);
	}
	
	public static void main(String[] args){
		new Main();
	}

}
