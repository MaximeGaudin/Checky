# Checky, the cute chess engine

## What ?

A toy chess engine with an estimated ELO of 1500 
(estimated with the well known "au doigt mouillé" method) 
against which **you can play here** : 

https://checky-engine.s3.eu-central-1.amazonaws.com/CheckyChessEngine/index.html

## Why ?

I am currently studying chess positional an tactical advantages 
and I felt like coding a chess engine could help me getting a 
deeper understanding of it.

In the end, not so much (:p) but I had a ton of fun and learnt a 
lot  about MinMax, NegaMax, Alpha Beta Pruning, Iterative Deepening, 
Transposition table, move generation and board representation !

A a toy project, it is not meant to be used seriously and will not 
be maintained.

## How ?

Initially for the JVM in Kotlin but I wanted my friends to play 
with it and asking them to install a UCI compatible GUI, download 
the engine, configure the GUI and learn how to use it was way too
painful for them.

So I used the awesome transpiling capabilities of Kotlin to write
a web version of it and after an hour or so of work, here I am !

## Performance

The JVM version is able to thinkg 6-7 plys ahead in 5-10s but the web version
is much slower so I reduced the plys depth to 5

## Disclaimer

I wrote this engine in the course of three days during my vacations, 
in a rather sloppy fashion (as in, outside with my friends, chatting,
drinking beers, playing chess and then, occasionally, writing some code).

## Source

The two main resources I used were :
- https://mediocrechess.blogspot.com
- https://www.chessprogramming.org/Main_Page

These are incredible sources of knowledge and contain everything 
you need to know about chess programming 

As a GUI, I used http://www.playwitharena.de/ for the JVM version
and https://chessboardjs.com/ for the web version
