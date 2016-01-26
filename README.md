# JAGE
JustAnotherGameEngine repository.
Final degree project by **Rubén Tomás Gracia** (valarion) about a 2D RPG game engine easily extensible with java for the University of Zaragoza.

# Build instructions

You must have gradle to build it, or import JAGEgradle into eclipse as gradle project.
If you have gradle installed, cd to JAGEgradle and run:

	gradle runtetris           - runs tetris
	gradle runscramble         - runs scramble
	gradle runrpg              - runs rpg
	gradle runeditor           - runs event editor
	
	gradle releasetetris       - builds tetris into JAGEgradle/build. You can then run it from bin/JAGEcore.
	gradle releasescramble     - builds scrabmble into JAGEgradle/build. You can then run it from bin/JAGEcore.
	gradle releaserpg          - builds rpg into JAGEgradle/build. You can then run it from bin/JAGEcore.
	gradle releaseeditor       - builds event editor into JAGEgradle/build. You can then run it from bin/JAGEcore.
	
	gradle runtetris debug     - runs tetris in debug and listens in port 8080 for a remote debugger.
	gradle runscramble debug   - runs scramble in debug and listens in port 8080 for a remote debugger.
	gradle runrpg debug        - runs rpg in debug and listens in port 8080 for a remote debugger.
	gradle runeditor debug     - runs event editor in debug and listens in port 8080 for a remote debugger.
	
	gradle clean               - cleans the solution

If you import it to eclipse, run the tasks for the same results as explained before.
