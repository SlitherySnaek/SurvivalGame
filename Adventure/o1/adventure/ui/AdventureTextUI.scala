package o1.adventure.ui

import o1.adventure._
import scala.io.StdIn._

/** The singleton object `AdventureTextUI` represents a fully text-based version of the
  * Adventure game application. The object serves as a possible entry point for the game,
  * and can be run to start up a user interface that operates in the text console.
  *
  * @see [[AdventureGUI]] */
object AdventureTextUI extends App {

  private val game = new Survival
  private val player = game.player
  this.run()


  /** Runs the game. First, a welcome message is printed, then the player gets the chance to
    * play any number of turns until the game is over, and finally a goodbye message is printed. */
  private def run() = {
    println(this.game.welcomeMessage)
    while (!this.game.isOver) {
      this.printAreaInfo()
      this.playTurn()
    }
    println("\n" + this.game.goodbyeMessage)
  }


  /** Prints out a description of the player character's current location, as seen by the character. */
  private def printAreaInfo() = {
    val area = this.player.location
    print("\n\n" + area.name)
    print(". ")
    println(area.fullDescription + "\n")
  }


  /** Requests a command from the player, plays a game turn accordingly, and prints out a report of what happened.
    * Added some periodic thoughts aswell. */
  private def playTurn() = {
    println(this.player)
    println()
    val i = game.turnCount
    i match {
      /** Used for printing out the hints that this is a game and not a real situation. */
      case 0 => println("Hunger? Thirst? What is going on here? Ugh, whatever I'm starving.\n")
      case _ if i == game.timeLimit / 4 => println("I'm really starting to worry about those statistics, what could they mean?\n")
      case _ if i == game.timeLimit / 2 => println("I have to get to the bottom of this statistics stalking.\n")
      case _ if i == game.timeLimit * 3 / 4 => println("Maybe someone is watching me?\n")
      case _ => println(game.turnCount + "\n")
    }
    val command = readLine("Command: ")
    val turnReport = this.game.playTurn(command)
    if (turnReport.nonEmpty) {
      println(turnReport)
    }
  }

}


