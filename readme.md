# Simple 2D Java tile  game
> Created in 2010. 

Simple game wrote in pure Java.
- Loading Maps
- Playing sounds
- Powerups
- Kill enemy when hit from top

## Gameplay:
![](Intro.gif)

***
#### Example map file:
```
# (Row beggining with '#' are comments)
#   (Space) Empty spot
#   A..Z     Tiles A to Z
#   o        Star
#   !        Note
#   *        Target
#   1        Grub
#   2        Fly
BF          o o o                o o o o o o o o o o o o o o o o o o o o o o o                EB
AD         IIIIIII              IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII               CA
AD                      o o o                I                   EBBBBBBBBBBBBBF     o o o o  CA
AD                     IIIIIII               I                   CAAAAAAAAAAAAAD    o o o o o CA
AD                                           I             o o   CAAAAAAAAAAAAAHF    o o o o  CA
AD                    2   !        2                      IIIII  CAAAAAAAAAAAAAAD   o o o o o CA
AD                      IIIII                                    CAAAAAAAAAAAAAAHF   IIIIIII  CA
AD         III                          1    1  1        2       CAAAAAAAAAAAAAAAD  o o o o o CA
AD                            1      IIIIIIIIIIIII               CAAAAAAAAAAAAAAAHF  o o o o  CA
AD      1       EBBBBBBBBBBBBBBBF     o o o o o o              * CAAAAAAAAAAAAAAAAD o o o o o CA
AHBBBBBBBBBBBBBBGAAAAAAAAAAAAAAAHBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBGAAAAAAAAAAAAAAAAHBBBBBBBBBBBGA
```