/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test;

import com.mycompany.matador.Board;
import com.mycompany.matador.Die;
import com.mycompany.matador.Piece;
import com.mycompany.matador.Player;
import com.mycompany.matador.Square;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 *
 * @author Louise Nielsen
 */
@RunWith(MockitoJUnitRunner.class)
public class MatadorTest {

    @Mock
    Board board;

    @Mock
    Piece piece;

    @Mock
    Die die1, die2;

    @Mock
    Square oldSquare, newSquare;
    
    @Test
    public void takeTurn() {
        int faceValue1 = 4;
        int faceValue2 = 2;
        int totalFaceValue = faceValue1+faceValue2;
        Die[] dice = {die1, die2};
        Player player = new Player(dice, board, piece);

        when(die1.getFaceValue()).thenReturn(faceValue1);
        when(die2.getFaceValue()).thenReturn(faceValue2);
        when(piece.getLocation()).thenReturn(oldSquare);
        when(board.getSquare(oldSquare,totalFaceValue)).thenReturn(newSquare);
        player.takeTurn();
        verify(die1).roll();
        verify(die2).roll();
        verify(die1).getFaceValue();
        verify(die2).getFaceValue();
        verify(piece).getLocation();
        verify(board).getSquare(oldSquare,totalFaceValue);
        verify(piece).setLocation(newSquare);
    }
}
