package org.albatrosscad.desktop

import org.albatrosscad.atom._

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

    val file = {
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
      file
    }

    val view = {
      val view = new JMenu("View")
      view.setMnemonic(KeyEvent.VK_V)
      val prefs = new JMenuItem("Preferences", KeyEvent.VK_P)
      view.add(prefs)
      view
    }

    val build = {
      val build = new JMenu("Build")
      build.setMnemonic(KeyEvent.VK_B)

      val scad = new JMenuItem("Build with OpenSCAD", KeyEvent.VK_O)
      val povray = new JMenuItem("Render with POVRay", KeyEvent.VK_R)
      val prefs = new JMenuItem("Build preferences", KeyEvent.VK_P)
      build.add(scad)
      build.add(povray)
      build.addSeparator()
      build.add(prefs)
      build
    }

    val share = {
      val share = new JMenu("Share")
      share.setMnemonic(KeyEvent.VK_S)
      val crossing = new JMenuItem("Albatross Crossing", KeyEvent.VK_A)
      val github = new JMenuItem("Github", KeyEvent.VK_G)
      val thingi = new JMenuItem("Thingiverse", KeyEvent.VK_T)
      val imgur = new JMenuItem("Imgur", KeyEvent.VK_I)
      share.add(crossing)
      share.add(github)
      share.add(thingi)
      share.add(imgur)
      share
    }

    val help = {
      val help = new JMenu("Help")
      help.setMnemonic(KeyEvent.VK_H)
      val tip = new JMenuItem("Tip of the day", KeyEvent.VK_T)
      val cheat = new JMenuItem("Cheat sheet", KeyEvent.VK_C)
      val manual = new JMenuItem("Manual", KeyEvent.VK_M)
      val about = new JMenuItem("About", KeyEvent.VK_A)
      help.add(tip)
      help.add(cheat)
      help.add(manual)
      help.add(about)
      help
    }

    bar.add(file)
    bar.add(view)
    bar.add(build)
    bar.add(share)
    bar.add(help)

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
    if(System.getenv("XDG_CURRENT_DESKTOP") == "GNOME") {
      val xToolkit = Toolkit.getDefaultToolkit
      val awtAppClassNameField =
        xToolkit.getClass().getDeclaredField("awtAppClassName");
      awtAppClassNameField.setAccessible(true);
      awtAppClassNameField.set(xToolkit, "Albatross");
    }

    SwingUtilities.invokeLater(new Runnable(){
      override def run() = {
        new Main().setVisible(true)
      }
    })

  }
}
