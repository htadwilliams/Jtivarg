# Jtivarg

Vector class features

* Stuffed with Pi!
* Encapsultes polar coordinate math, with conversion between cartesian and polar coordinate spaces.
* Fast 2d line segment collision detection and intersection point calculation.
* Vector addition and other geometry related utility.

Jticlock

  Demo using Vector and Segment. Also prototype to see how many FPS I could get after configuring Java AWT to use hardware frame flipping the same way DirectcX and OpenGL do.
  
Astrovania

  Partial port of C++ vector graphics game engine Ytivarg (gravity, backwards). C++ version mostly devolved into a swarm / gravity simulator.
  
Liner

  Tool written to visualize and debug vector graphics when I was frustrated trying to fix bugs in the game.
  
Known issues:

  * Second hand on clock demonstrates odd warping behavior when the hand passes approx 35 second mark. 
  * Laser multi-segment bouncing is buggy / incomplete and was current main focus of effort.

# Building

Mainly notes to self because this is what I had to reverse-engineer when opening project after downtime.

* See pom.xml for dependencies. 
* Importing pom.xml and building in IDE (or running maven build) should download required dependencies and/or generate IDE project file.
* Version of Jackson is very old and should be updated.
* Application entry points are:
  1 org.tsoft.jtivarg.astrovania.main() - game.
  2 org.tsoft.jtivarg.liner.main() - line rendering and debugging tool.
  3 org.tsoft.jtivarg.yticlock.main() - demo of 2d graphic flipping and Vector class.
* Tests should be runnable and had no problems on initial commit.
