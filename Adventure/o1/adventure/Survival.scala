package o1.adventure


/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of "hard-coded" information which pertain to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Survival {

  /** The title of the adventure game. */
  val title = "A Forest Adventure"


  // (immutable) All areas, in order start + 1-10 (considering the map), Boolean value indicates the existence of drinkable water in area
  private val start = new Area("Crater", "You mysteriously woke up from here.", false)
  private val river = new Area("River", "A freshwater river. Where does this lead?", true) //1
  private val lake = new Area("Lake", "A deep lake next to a beautiful waterfall.", true) //2
  private val upWaterFall = new Area("Over the Waterfall", "What a view. Welp, no time to wonder it. Should I try to go down from here?", true) //3
  private val hill = new Area("Steep Hill", "Quite a climb.", false) //4
  private val mound = new Area("Mound", "At least I'll see the dinosaurs heading my way. Gotta keep my eyes peeled for flying ones.", false) //5
  private val floodlands = new Area("Floodlands", "Ew, slimy. Don't wanna drink that.", false) //6
  private val mountainSide = new Area("Mountainside", "Don't think I can climb up this mountain. Do I see a cave there?", false) //7
  private val forest = new Area("Forest", "A thick forest, I can't see 5 meters in front of me.", false) //8
  private val tree = new Area("Tree", "Maybe I'm safe here for a while. A good place to rest perhaps?", false) //9
  private val cave = new Area("Cave", "I can't go back the way I came. I've got to keep going. Thankfully there's a little pond here.", true) //10


  // In order: North, East, South, West, Up, Down.
  start.setNeighbors(Vector("north" -> hill, "east" -> forest, "south" -> mound, "west" -> river))
  river.setNeighbors(Vector("east" -> start, "South" -> floodlands, "west" -> lake))
  lake.setNeighbors(Vector("east" -> river))
  upWaterFall.setNeighbors(Vector("east" -> hill, "down" -> lake))
  hill.setNeighbors(Vector("south" -> start, "west" -> upWaterFall))
  mound.setNeighbors(Vector("north" -> start, "south" -> mountainSide, "west" -> river))
  floodlands.setNeighbors(Vector("north" -> river, "east" -> mound))
  mountainSide.setNeighbors(Vector("north" -> mound, "down" -> cave))
  forest.setNeighbors(Vector("west" -> start, "up" -> tree))
  tree.setNeighbors(Vector("down" -> forest))
  cave.setNeighbors(Vector("up" -> forest))
  /** The character that the player controls in the game. */
  val player = new Player(start)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. This game's win condition */
  val timeLimit = 20


  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.turnCount >= this.timeLimit

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || !this.player.isAlive

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You wake up somewhere you don't recognize. You must have hit your head pretty hard.\n" +
    "You look around, you spot a flying DINOSAUR above you?\n" +
    "Oh boy, this will be fun. You have no idea what to do. Thankfully you aren't thirsty but you'd kill for anything to eat. Looks like you just might have to."


  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage = {
    if (this.isComplete)
      "You see your points?! Oh yeah, right.\n" +
        "Man, the VR is so good I forgot who I am for a second there, shame the game sucked. At least I got some coupons for winning it."
    else if (!this.player.isAlive)
      "Everything fades to black. You're too weak to continue.\n" +
        "Someone tells you to take your headset off?! Oh yeah, right." +
        "VR's gotten super good these days, hasn't it? Too bad the game was a piece of trash. What a waste."
    else // game over due to player quitting
      "Quitter! What? You could do that?\n" +
        "Oh yeah, completely forgot it was a game for a second. Thank God it's over, I couldn't have taken one more second of that."
  }


  /** Plays a turn by executing the given in-game command, such as "go west". Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) = {
    val action = new Action(command)
    val outcomeReport = action.execute(this.player)
    if (outcomeReport.isDefined) {
      this.turnCount += 1
    }
    outcomeReport.getOrElse("Unknown command: \"" + command + "\".")
  }


}

