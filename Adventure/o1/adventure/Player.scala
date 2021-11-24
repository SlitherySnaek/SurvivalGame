package o1.adventure

import java.util.function.ToDoubleFunction
import scala.math.min
import scala.collection.mutable.Map


/** A `Player` object represents a player character controlled by the real-life user of the program.
  *
  * A player object's state is mutable: the player's location and possessions can change, for instance.
  *
  * @param startingArea the initial location of the player */
class Player(startingArea: Area) {
  private var currentLocation: Area = startingArea // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false // one-way flag
  private val items = Map[String, Item]()

  private var hunger: Int = 5 //gatherer
  private var thirst: Int = 20 //gatherer

  def isAlive: Boolean = this.hunger > 0 && this.thirst > 0

  /** Used for surprising events, such as falling down the waterfall by drinking from it. */
  def goWithoutPrint(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
  }

  def drink(): String = {
    if (currentLocation.isWaterFall) {
      this.thirst = 20
      this.goWithoutPrint("Down")
      "You try to drink from the waterfall but the strong stream makes you slip!\n" +
        "You fall into the lake below. Unharmed, miraculously."
    } else if (currentLocation.isDrinkable) {
      if (this.thirst != 20) {
        this.thirst = 20
        "You gulp on some fresh water. No need to drink in a while now."
      }
      else "You don't need to drink right now."
    }
    else "There's nothing here to drink."
  }


  def eat() = this.hunger = 20 //TODO


  def drop(itemName: String): String = {
    if (items.contains(itemName)) {
      val dropped = items.remove(itemName)
      dropped.foreach(n => currentLocation.addItem(n))
      "You drop the " + itemName + "."
    }
    else "You don't have that!"
  }

  def examine(itemName: String): String = {
    if (items.contains(itemName)) "You look closely at the " + items(itemName) + ".\n" + items(itemName).description
    else "If you want to examine something, you need to pick it up first."
  }

  def get(itemName: String): String = {
    if (currentLocation.contains(itemName)) {
      val item = currentLocation.removeItem(itemName)
      items += item.get.name -> item.get
      "you pick up the " + itemName + "."
    }
    else "There is no " + itemName + " here to pick up."
  }

  def has(itemName: String): Boolean = items.contains(itemName)

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  def inventory: String = {
    if (items.nonEmpty) {
      var print = "You are carrying:"
      items.foreach(n => print += "\n" + n._1)
      print
    }
    else "You are empty-handed."
  }

  /** Returns the current location of the player. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player's current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) = {
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if (direction == "Lake" && this.location.isWaterFall)
      "A Brave one I see! You look down and realize this was a bad idea. You try to turn back but slip and fall down the waterfall!\n" +
        "You fall into the lake below. Unharmed, miraculously."
    else if (destination.isDefined) "You go " + direction + "." else "You can't go " + direction + "."
  }


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() = {
    "You rest for a while. Better get a move on, though."
  }


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() = {
    this.quitCommandGiven = true
    ""
  }


  /** Used for printing the hunger and thirst of player in PlayTurn() */
  override def toString = s"Hunger: $hunger\nThirst: $thirst"


}


