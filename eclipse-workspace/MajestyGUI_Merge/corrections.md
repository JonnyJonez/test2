# Corrections

## Simple corrections
- Correct package naming (eg. ch.jonnyjonez.fhnw)
- Correct package naming (character_cards -> charactercards)
- Correct class and variable names according to Java convention
- Changed scope of all fields, where possible, to private
- Image/Media files names should not contain whitespaces (by convention they are lowercase with underscores)
- Fields should be ordered by accessibility and by type (constant -> final -> "normal" fields)
- Reformat code
- Make use of switch statements instead of multiple else-if
- If code is not used anymore it should be deleted, not commented
- Enum values should be uppercase (eg. CardType)
- Do not use System.out.println along with a logger (use the logger all the time)
- Instead of writing comments move logic to dedicated method with this name
- Extract duplicate logic into separate methods (eg. Player)
- When instantiating logger inject current class name (Logger.getLogger(ClientModel.class.getName())
- Logger could be a constant

## Unused?
- Remove unused fields (eg. in ServerController)
- All images in package Side B are unused
- Remove all unused fields or change it to local variables
- Remove all unused methods, enums (eg. ScoreType)
- Is "Frequency of cards.xlsx" used? Delete it if it isn't

### (optional) Return statements inline
return new Card(types);
instead of
Card A = new Card(types);
return A;

### (optional) Remove unused self-references
new VisibilityMsg(name, "false");
instead of 
new VisibilityMsg(player.this.name, "false");

### Loading of resources
new Image("/resources/pics/cards/charactercards/Blue.jpg");
instead of
new Image(getClass().getResourceAsStream("../ch.jonnyjonez.charactercards/Blue.jpg"));
Same for media files

## Nice to have
- Add "resources" folder and move images and soundfiles to resources folder
- If you use Java8 some anonymous methods could be replaced with lambda expressions
- All listeners should be registered in corresponding class
- For String concatenation with lots of arguments String.format() can be used
- Remove magic strings and numbers (eg. ClientMain)
- ClientController should actually control the view. If this is done the controller can be added in gui_layout.fxml (fx:controller="client.ClientController")
- Make fields (and variables if you want) final, where possible

## Njaaaa....
- All complex if statements (eg. ServerModel) should be in separate methods (max 2(-3) levels of intents per method)
- Comments should not contain special characters 

## What?
- Why is this app always "sleeping"?
- Some variables have quite confusing names (eg. CardType karte)?

## Further additions
- Separate server and client to separate modules
- Refactor code