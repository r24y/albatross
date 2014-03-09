package org.albatrosscad.desktop

import org.fife.ui.rtextarea._
import org.fife.ui.rsyntaxtextarea._

import javax.swing._

import java.awt._



class Main extends JFrame {
  val textArea = new RSyntaxTextArea(20,60)
  textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SCALA)
  textArea.setCodeFoldingEnabled(true)
  val sp = new RTextScrollPane(textArea)

  /*
  val split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, {
    val mv = new ModelView()
    mv.setMinimumSize(new Dimension(1,1))
    val panel = new JPanel(new BorderLayout)
    panel.add("Center",mv)
    panel
  })
  */

  setContentPane(sp)
  setTitle("Albatross")
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  pack()
  setLocationRelativeTo(null)
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
    SwingUtilities.invokeLater(new Runnable(){
      override def run() = {
        new Main().setVisible(true)
      }
    })

  }
}
