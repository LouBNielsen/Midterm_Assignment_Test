/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.matador;

/**
 *
 * @author Louise Nielsen
 */
public class Player {

    private Die[] dice;
    private Board board;
    private Piece piece;

    public Player(Die[] dice, Board board, Piece piece) {
        this.dice = dice;
        this.board = board;
        this.piece = piece;
    }

    public void takeTurn() {
        
        int faceValue = rollDice();

        Square square = piece.getLocation();
        Square square2 = board.getSquare(square, faceValue);
        piece.setLocation(square2);
    }
    
    private int rollDice(){
        int faceValue = 0;

        for (Die die : dice) {
            die.roll();
            faceValue += die.getFaceValue();
        }
        return faceValue;
    }
}
