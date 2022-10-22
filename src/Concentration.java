import java.util.ArrayList;
import java.util.Random;

import tester.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents a Card
class Card  {
  String suit;
  String value;
  boolean faceUp;
  int x;
  int y;
  String color;


  // two arg constructor
  Card(String suit, String value)  {
    this.suit = suit;
    this.value = value;
    this.faceUp = false;
    this.x = 0;
    this.y = 0;
    this.color = "N/A";
  }

  // three arg constructor
  Card(String suit, String value, boolean faceUp)  {
    this.suit = suit;
    this.value = value;
    this.faceUp = faceUp;
    this.x = 0;
    this.y = 0;
    this.color = "N/A";
  }

  /*
   * TEMPLATE:
   * Fields:
   * ... this.suit ...   -- String
   * ... this.value ...  -- String
   * ... this.faceUp ... -- Boolean
   * ... this.x ...      -- int
   * ... this.y ...      -- int
   * ... this.color ...  -- String
   * Methods:
   * ... this.draw() ... -- WorldImage
   */


  // draws the card
  WorldImage draw() {
    WorldImage frontEmptyCard = new RectangleImage(60, 80, OutlineMode.SOLID, Color.white);
    WorldImage frontCardBorder;
    WorldImage valueText;

    // sets the front face of the card
    if (this.suit.equals("♣") || this.suit.equals("♠")) {
      frontCardBorder = new RectangleImage(60, 80, OutlineMode.OUTLINE, Color.black);
      valueText = new TextImage(this.value + this.suit, 20, Color.black);
      this.color = "black";
    } else  {
      frontCardBorder = new RectangleImage(60, 80, OutlineMode.OUTLINE, Color.red);
      valueText = new TextImage(this.value + this.suit, 20, Color.red);
      this.color = "red";
    }

    WorldImage backCard = new OverlayImage(new RectangleImage(60, 80, 
        OutlineMode.OUTLINE, Color.white),
        new RectangleImage(50, 70, OutlineMode.SOLID, Color.blue));
    // returns either the front of back of the card
    if (this.faceUp)  {
      return new OverlayImage(new OverlayImage(valueText, frontCardBorder), frontEmptyCard);
    } else  {
      return backCard;
    }

  }
}

// utilities class for cards
class CardUtils {

  /*
   * TEMPLATE:
   * Methods:
   * ... this.buildListCards() ...              -- ArrayList<Card>
   * ... this.shuffle(ArrayList<Card>) ...      -- ArrayList<Card>
   * ... this.shuffle(ArrayList<Card>, int) ... -- ArrayList<Card>
   */

  //builds a list for deck of cards
  ArrayList<Card> buildListCards()  {
    ArrayList<Card> fullDeck = new ArrayList<Card>();
    ArrayList<String> suits = new ArrayList<String>();
    suits.add("♣");
    suits.add("♦");
    suits.add("♥");
    suits.add("♠");

    for (int suit = 0; suit < 4; suit++)  {
      for (int num = 1; num < 10; num++)  {
        fullDeck.add(new Card(suits.get(suit), String.valueOf(num + 1)));
      }
      fullDeck.add(new Card(suits.get(suit), "A"));
      fullDeck.add(new Card(suits.get(suit), "J"));
      fullDeck.add(new Card(suits.get(suit), "Q"));
      fullDeck.add(new Card(suits.get(suit), "K"));
      
    }

    return fullDeck;
  }

  // shuffles an array of cards with no seed
  ArrayList<Card> shuffle(ArrayList<Card> deck) {
    Random rand = new Random();

    for (int i = 0; i < deck.size(); i++) {
      int randomIndexSwap = rand.nextInt(deck.size());
      deck.set(i, (deck.set(randomIndexSwap, deck.get(i))));
    }

    return deck;
  }

  //shuffles an array of cards with seed
  ArrayList<Card> shuffle(ArrayList<Card> deck, int seed) {
    Random rand = new Random(seed);

    for (int i = 0; i < deck.size(); i++) {
      int randomIndexSwap = rand.nextInt(deck.size());
      deck.set(i, (deck.set(randomIndexSwap, deck.get(i))));
    }

    return deck;
  }

  //sets the point for each card
  void setPoints(ArrayList<Card> cards)  {
    for (int currentCard = 0; currentCard < cards.size(); currentCard++)  {
      cards.get(currentCard).x = 30 + 60 * currentCard;
      if (currentCard < 13)  {
        cards.get(currentCard).x = 30 + 60 * currentCard;
        cards.get(currentCard).y = 40;
      } else if (currentCard < 26)  {
        cards.get(currentCard).x = 30 + 60 * (currentCard - 13);

        cards.get(currentCard).y = 120;

      } else if (currentCard < 39) {
        cards.get(currentCard).x = 30 + 60 * (currentCard - 26);

        cards.get(currentCard).y = 200;

      } else  {
        cards.get(currentCard).x = 30 + 60 * (currentCard - 39);

        cards.get(currentCard).y = 280;

      }

    }
  }
  


}

// represents a Deck of cards
class Deck  {
  ArrayList<Card> cards;

  Deck()  {
    this.cards = new CardUtils().buildListCards();
    new CardUtils().shuffle(this.cards);
    new CardUtils().setPoints(this.cards);
  }


  /*
   * TEMPLATE:
   * Fields:
   * ... this.cards ... -- ArrayList<Card>
   * Methods:
   * ... this.setPoints() ...      -- void
   * ... this.draw(WorldScene) ... -- WorldScene
   */




  // draw method for a deck of cards
  WorldScene draw(WorldScene acc) {
    for (int currentCard = 0; currentCard < this.cards.size(); currentCard++)  {

      acc.placeImageXY(this.cards.get(currentCard).draw(), this.cards.get(currentCard).x, 
          this.cards.get(currentCard).y);
    }

    return acc;
  }
}

// class for the game
class ConcentrationGame extends World {
  Deck deckOfCards;
  ArrayList<Card> clicked;

  ConcentrationGame(Deck deckOfCards, ArrayList<Card> clicked) {
    this.deckOfCards = deckOfCards;
    this.clicked = clicked;
  }

  /*
   * TEMPLATE:
   * Methods:
   * ... this.makeScene() ...          -- WorldScene
   * ... this.onMouseClicked(Posn) ... -- void
   * ... this.onKeyEvent(String) ...   -- void
   * ... this.lastScene(String) ...    -- WorldScene
   */

  // draws the game
  public WorldScene makeScene() {
    WorldScene scene = new WorldScene(780, 600);

    // keeps track of the score and draws the scene
    this.deckOfCards.draw(scene).placeImageXY(new TextImage("Score:" 
        + String.valueOf(this.deckOfCards.cards.size() / 2), 25, Color.black), 390, 360);
    return scene;
  }



  // takes in the mouse click
  public void onMouseClicked(Posn position) {
    // chosen card is card that doesn't exist at first
    Card chosen = new Card("Z", "Z");

    for (int cardPos = 0; cardPos < this.deckOfCards.cards.size(); cardPos++) {
      int cardx = this.deckOfCards.cards.get(cardPos).x;
      int cardy = this.deckOfCards.cards.get(cardPos).y;

      if (((position.x - cardx <= 28) && (cardx - position.x <= 28))
          && ((position.y - cardy <= 38) && (cardy - position.y <= 38)))  {
        this.deckOfCards.cards.get(cardPos).faceUp = true;
        chosen = this.deckOfCards.cards.get(cardPos);
      }
    }



    if (chosen != new Card("Z", "Z")) {


      if (this.clicked.size() == 0) {
        this.clicked = new ArrayList<Card>();
        this.clicked.add(chosen);
      } else if (this.clicked.size() == 1) {
        if (this.deckOfCards.cards.size() == 2) {
          this.endOfWorld("You Finished!");
        }

        this.clicked.add(chosen);


      } else {

        if (this.clicked.get(0).value.equals(this.clicked.get(1).value)
            && !this.clicked.get(0).suit.equals(this.clicked.get(1).suit)
            
            // added enhancement that only matches cards if they have the same color
            && this.clicked.get(0).color.equals(this.clicked.get(1).color))  {
          this.deckOfCards.cards.remove(this.deckOfCards.cards.indexOf(this.clicked.get(0)));
          this.deckOfCards.cards.remove(this.deckOfCards.cards.indexOf(this.clicked.get(1)));

        }   else if (this.clicked.get(0).value.equals(this.clicked.get(1).value)
            && this.clicked.get(0).suit.equals(this.clicked.get(1).suit))  {

          this.deckOfCards.cards.get(this.deckOfCards.cards.indexOf(this.clicked.get(0))).faceUp
            = false;
        }   else  {
          this.deckOfCards.cards.get(this.deckOfCards.cards.indexOf(this.clicked.get(0))).faceUp
            = false;
          this.deckOfCards.cards.get(this.deckOfCards.cards.indexOf(this.clicked.get(1))).faceUp
            = false;

        }
        this.clicked = new ArrayList<Card>();
        this.clicked.add(chosen);

      }
    }


  }

  // restarts the game
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      this.clicked = new ArrayList<Card>();
      this.deckOfCards = new Deck();
    }
  }

  // ends the game
  public WorldScene lastScene(String message)  {
    if (this.deckOfCards.cards.size() == 0) {
      WorldScene ending = new WorldScene(390, 160);
      ending.placeImageXY(new TextImage("You Finished!", 20, Color.black), 195, 80);
      return ending;
    }
    return this.makeScene();
  }
}


// examples of the game
class ExamplesGame  {
  CardUtils utils;
  Deck emptyDeck;
  Deck newDeck;
  Deck deck52;
  Deck deck2;
  Deck deck1;
  Card aceClub;
  Card fiveSpade;
  Card eightDiamond;
  ArrayList<Card> cards2;
  ArrayList<Card> emptyCards;
  ArrayList<Card> cards1;

  ConcentrationGame emptyWorld;
  ConcentrationGame world1;

  void initData()  {
    utils = new CardUtils();


    this.aceClub = new Card("♣", "A");
    this.fiveSpade = new Card("♠", "5");
    this.eightDiamond = new Card("♦", "8", true);

    this.cards1 = new ArrayList<Card>();
    this.cards2 = new ArrayList<Card>();
    this.emptyCards = new ArrayList<Card>();

    this.cards2.add(aceClub);
    this.cards2.add(fiveSpade);

    this.cards1.add(eightDiamond);

    this.deck1 = new Deck();
    this.newDeck = new Deck();
    this.emptyDeck = new Deck();
    this.deck52 = new Deck();
    this.deck2 = new Deck();
    this.deck2.cards = this.cards2;
    this.emptyDeck.cards = emptyCards;
    this.deck1.cards = cards1;

    emptyWorld = new ConcentrationGame(emptyDeck, new ArrayList<Card>());
    world1 = new ConcentrationGame(deck2, new ArrayList<Card>());



  }


  // tests the draw method on the Card class
  void testdrawCard(Tester t) {
    initData();
    t.checkExpect(this.aceClub.draw(), 
        new OverlayImage(new RectangleImage(60, 80, OutlineMode.OUTLINE, Color.white),
            new RectangleImage(50, 70, OutlineMode.SOLID, Color.blue)));
    t.checkExpect(this.fiveSpade.draw(), 
        new OverlayImage(new RectangleImage(60, 80, OutlineMode.OUTLINE, Color.white),
            new RectangleImage(50, 70, OutlineMode.SOLID, Color.blue)));
    t.checkExpect(this.eightDiamond.draw(), 
        new OverlayImage(new OverlayImage(new TextImage("8♦", 20, Color.red), 
            new RectangleImage(60, 80, OutlineMode.OUTLINE, Color.red)), 
            new RectangleImage(60, 80, OutlineMode.SOLID, Color.white)));
  }

  // tests the buildList method on the CardUtils class
  void testbuildList(Tester t)  {
    initData();
    this.newDeck.cards = this.utils.buildListCards();
    for (int i = 0; i < 52; i += 13) {
      t.checkExpect(this.newDeck.cards.get(i).value, "2");
    }

    for (int i = 5; i < 52; i += 13) {
      t.checkExpect(this.newDeck.cards.get(i).value, "7");
    }
  }

  // tests the shuffle method on the CardUtils class
  void testshuffle(Tester t) {
    initData();
    t.checkExpect(deck2.cards.get(0).value, "A");
    t.checkExpect(deck2.cards.get(1).value, "5");
    this.utils.shuffle(deck2.cards, 7);
    t.checkExpect(deck2.cards.get(0).value, "5");
    t.checkExpect(deck2.cards.get(1).value, "A");
  }


  // tests the setPoints method on the CardUtils class
  void testsetPoints(Tester t)  {
    initData();
    utils.setPoints(emptyCards);
    t.checkExpect(emptyCards.size(), 0);
    utils.setPoints(cards2);
    t.checkExpect(aceClub.x, 30);
    t.checkExpect(aceClub.y, 40);
    t.checkExpect(fiveSpade.x, 90);
    t.checkExpect(fiveSpade.y, 40);
  }

  // tests the draw method and makeScene method
  void testdrawDeck(Tester t) {
    initData();
    t.checkExpect(this.emptyDeck.draw(new WorldScene(5, 5)), new WorldScene(5, 5));
    WorldScene scene = new WorldScene(60, 80);
    scene.placeImageXY(new OverlayImage(new OverlayImage(new TextImage("8♦", 20, Color.red), 
        new RectangleImage(60, 80, OutlineMode.OUTLINE, Color.red)), 
        new RectangleImage(60, 80, OutlineMode.SOLID, Color.white)), 0, 0);
    t.checkExpect(this.deck1.draw(new WorldScene(60, 80)), scene);
  }

  // tests the onMouseClicked method
  void testonMouseClicked(Tester t) {
    initData();
    t.checkExpect(emptyWorld.clicked, new ArrayList<Card>());
    emptyWorld.onMouseClicked(new Posn(5, 5));
    ArrayList<Card> clicked = new ArrayList<Card>();
    clicked.add(new Card("Z", "Z"));
    t.checkExpect(emptyWorld.clicked, clicked);

  }

  // tests the onKeyEvent method
  void testonKeyEvent(Tester t) {
    initData();
    t.checkExpect(emptyWorld.deckOfCards.cards, new ArrayList<Card>());
    emptyWorld.onKeyEvent("r");
    t.checkExpect(emptyWorld.deckOfCards.cards == new ArrayList<Card>(), false);
    t.checkExpect(world1.deckOfCards.cards, cards2);
    world1.onKeyEvent("r");
    t.checkExpect(world1.deckOfCards.cards == deck1.cards, false);
  }



  void testBigBang(Tester t) {
    initData();
    ConcentrationGame world = new ConcentrationGame(this.deck52, new ArrayList<Card>());
    int worldWidth = 780;
    int worldHeight = 400;
    world.bigBang(worldWidth, worldHeight);
  }
}