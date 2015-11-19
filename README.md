# JAGE
JustAnotherGameEngine repository

# Build instructions

You must have gradle to build it, or import JAGEgradle into eclipse as gradle project.
If you have gradle installed, cd to JAGEgradle and run:

	gradle run     - runs the game
	gradle release - builds the game into JAGEgradle/build. You can then run it from bin/JAGEcore.
	gradle debug   - runs the game in debug and listens in port 8080 for a remote debugger.
	gradle clean   - cleans the solution

If you import it to eclipse, run the tasks **run**, **release**, **debug** and **clean** for the same results as explained before.
