# Bitmask collision detection library

[![Gitter](https://badges.gitter.im/laamella-gad/bitmask.svg)](https://gitter.im/laamella-gad/bitmask?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This library will give 2D graphics developers, especially game developers, a tool to quickly detect whether two images overlap by comparing their actual visible pixels.
This is done by creating an extra datastructure for every image, the Bitmask.
It contains the pattern of visible pixels of the image as a pattern of bits.
Overlaps can be found very quickly by simply AND-ing two of these bitmasks, supplying an offset.

This library was originally written by Ulf Ekström.
The Java port is by Danny van Bruggen. 
It is ported from the code found in [Pygame](http://www.pygame.org/news.html)

Note that for now, only one collision detection method has been ported, but this happens to be the most essential one.

