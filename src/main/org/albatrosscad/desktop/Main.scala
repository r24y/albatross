package org.albatrosscad.desktop

import org.fife.ui.rtextarea._
import org.fife.ui.rsyntaxtextarea._

import javax.swing._

import java.awt._
import java.awt.event._



class Main extends JFrame {
  val textArea = new RSyntaxTextArea(20,60) {
    override def paintComponent(g:Graphics):Unit = {
      val g2 = g.asInstanceOf[Graphics2D]
      g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON)
      g2.setRenderingHint(RenderingHints.KEY_RENDERING,
        RenderingHints.VALUE_RENDER_QUALITY)
      super.paintComponent(g2)
    }
  }
  textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  textArea.setCodeFoldingEnabled(true)
  textArea.setAntiAliasingEnabled(true)

  JFrame.setDefaultLookAndFeelDecorated(true)

  val sp = new RTextScrollPane(textArea)
  val v3 = new View3D
  val statusBar = new JLabel("Ready.")
  getContentPane.add(statusBar,BorderLayout.SOUTH)

  val cl = this.getClass.getClassLoader
  val icon  = new ImageIcon(cl.getResource("albatross-icon.png"))
  setIconImage(icon.getImage)

  val split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, v3)
  split.setDividerLocation(0.5)

  getContentPane.add(split,BorderLayout.CENTER)
  setExtendedState(getExtendedState | java.awt.Frame.MAXIMIZED_BOTH)
  setTitle("Albatross")
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  createMenus()
  pack()
  setLocationRelativeTo(null)

  private def createMenus():Unit = {
    val bar = new JMenuBar

    val file = new JMenu("File")
    val newDoc = new JMenuItem("New",KeyEvent.VK_N)
    val open = new JMenuItem("Open", KeyEvent.VK_O)
    val recent = {
      val recent = new JMenu("Open recent")
      recent.setMnemonic(KeyEvent.VK_R)
      (scala.List("foo","bar","baz","quux")).map{s:String => new JMenuItem(s+".cad.scala")}.foreach(recent add _)
      recent
    }
    val save = new JMenuItem("Save", KeyEvent.VK_S)
    val samples = {
      val samples = new JMenu("Examples")
      samples.setMnemonic(KeyEvent.VK_E)
      (scala.List("Gear","Widget","Print head")).map(new JMenuItem(_)).foreach(samples add _)
      samples
    }
    val exit = new JMenuItem("Exit",KeyEvent.VK_X)
    file.add(newDoc)
    file.add(open)
    file.add(recent)
    file.add(save)
    file.addSeparator()
    file.add(samples)
    file.addSeparator()
    file.add(exit)
    file.setMnemonic(KeyEvent.VK_F)
    bar.add(file)

    setJMenuBar(bar)
  }
}

object Main{
  def main(args:Array[String]):Unit = {
    //System.loadLibrary("j3dcore-ogl")
    def setLAF():Unit = {
      try{
        for (info <- UIManager.getInstalledLookAndFeels) {
          if ("Nimbus".equals(info.getName)) {
            UIManager.setLookAndFeel(info.getClassName)
            return
          }
        }
      }
    }
    setLAF()
    val xToolkit = Toolkit.getDefaultToolkit
    val awtAppClassNameField =
      xToolkit.getClass().getDeclaredField("awtAppClassName");
    awtAppClassNameField.setAccessible(true);
    awtAppClassNameField.set(xToolkit, "Albatross");

    SwingUtilities.invokeLater(new Runnable(){
      override def run() = {
        new Main().setVisible(true)
      }
    })

  }
}
