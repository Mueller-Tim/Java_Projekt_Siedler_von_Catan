<h1 align="center"> Settler of Catan </h1> <br>

## Table of Contents

- [Introduction](#introduction)
- [Getting started](#getting-started)
- [Game instructions](#game-instructions)
- [Class diagram](#class-diagram)
- [Original game rules](#original-game-rules)
- [Contributors](#contributors)

## Introduction

This is a console version of the board game Settlers of Catan. It can be played by 2 to 4 players.

## Getting started

### Requirements

a running java installation

### How to run

```shell
$ javac Catan.java
```
or
```shell
$ java Catan
```

## Game instructions

Here the main game procedures are explained. For the exact rules of the game etc please see the
attached [PDF](#original-game-rules).

### Game schedule

The game consists of the following 2 phases.

#### 1. Founding phase

In the foundation phase, all players can place 2 settlements and two roads.

#### 2. Playing phase

In the game phase, any number of [player moves](#playing-move) are made until one person reaches the required winning
points

#### Game End
The game ends when a player reaches the required 7 winning points. The game ends when a player reaches the required 7
winning points. There are the following ways to get victory points:

<table>
<tr>
    <th>Event</th>
    <th>Description</th>
    <th>Number of winning points</th>
  </tr>
<tr>
<td>Build settlement</td>
<td>The player builds a settlement</td>
<td>1</td>
</tr>
<tr>
<td>Build city</td>
<td>The player builds a city</td>
<td>2</td>
</tr>
<tr>
<td>Longest road</td>
<td>As soon as a player builds a continuous
(branches do not count) of at least
5 single streets, which is not interrupted by a foreign
interrupted by a foreign settlement or city,
the player receives 2 victory points. If another player succeeds in
builds a longer road than the current owner of the longest street, he receives 2 points and 2 victory points are deducted from the previous player with the longest road.</td>
<td>2</td>
</tr>
</table>

### Playing move

Here it is explained in detail what possible moves there are for a player. These moves are repeated until one player
wins.

#### Action menu

The possible moves of a player are listed in the table below. First, two dice are automatically rolled and the two
results are added together.
Based on the result, the resources are distributed to the players who have built on a field with this number.

<table>
  <tr>
    <th>Action</th>
    <th>Description</th>
    <th>mandatory</th>
  </tr>
  <tr>
    <td>Build</td>
    <td>This opens the build menu, which is explained in detail in the table below.</td>
    <td>No</td>
  </tr>
 <tr>
    <td>Trade</td>
    <td>The player has the possibility to trade his resources with another player. The player whose turn it is makes an offer to the other players. He can specify how much of which resource he wants and how much of which resource he offers in return.</td>
    <td>No</td>
  </tr>
 <tr>
    <td>Show inventory</td>
    <td>This action shows the player his current inventory</td>
    <td>No</td>
  </tr>
<tr>
    <td>End Turn</td>
    <td>This action ends a player's turn and automatically moves to the next player.</td>
    <td>Yes</td>
  </tr>
</table>

#### Build menu

This menu is called only if the player has called the build menu in the action menu

<table>
  <tr>
    <th>Action</th>
    <th>Description</th>
    <th>mandatory</th>
  </tr>
  <tr>
    <td>Settlement</td>
    <td>With this selection the player can build a settlement<br>
    First, the system checks whether the player has the required resources<br>
    Then the player is asked to enter the coordinates in the format x:y<br>
    </td>
    <td>No</td>
  </tr>
  <tr>
    <td>City</td>
    <td>With this selection the player can build a city<br>
    First, the system checks whether the player has the required resources<br>
    Then the player is asked to enter the coordinates in the format x:y<br>
    A city is an extension of a settlement, i.e. only coordinates of one's own settlement can be selected.
    </td>
    <td>No</td>
  </tr>
 <tr>
    <td>Road</td>
    <td>With this selection the player can build a road<br>
    First, the system checks whether the player has the required resources<br>
    Then the player is asked to enter the coordinates in the format x:y<br>
    Roads can be attached only to own roads, settlements, or cities
    </td>
    <td>No</td>
  </tr>
 <tr>
    <td>Cancel</td>
    <td>This selection allows the player to return to the action menu</td>
    <td>Yes</td>
  </tr>
</table>

## Class diagram

The class diagram can be found [here](Klassendiagramm_PM1.svg)

## Original game rules

The original game rules are found
[here](CATAN_DasSpiel_Spielregel.pdf)

## Contributors

[Tim Müller](https://github.zhaw.ch/muellti3) \
[Raphael Meierhans](https://github.zhaw.ch/meierr06)\
[Ivan Starčević](https://github.zhaw.ch/starciva) \
[Philipp Kiss](https://github.zhaw.ch/kisphi01)