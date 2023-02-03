package com.kharoudApps.bingo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
//    for keeping the track of completed rows, columns or diagonals striked down
    private int counterCompletedLines =0;
    private Set<Integer> completedLine = new HashSet<Integer>();
//    for keeping th count of tiles filled
    private int counter =1;
//    colors for 5 bingo rowCols
    private int[] color = {Color.RED ,Color.BLUE , Color.BLACK , Color.GREEN , Color.MAGENTA };
//    0 means not set 1 means set
    private int[] currentState ={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private final int[][] winningPositions = { {1,2,3,4,5}, {6,7,8,9,10} , {11,12,13,14,15},{16,17,18,19,20} , {21,22,23,24,25},
            {1,6,11,16,21}, {2,7,12,17,22} , {3,8,13,18,23} , {4,9,14,19,24},{5,10,15,20,25}  };

    private int messageId;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageId =  getResources().getIdentifier("message" , "id", getPackageName());
        message = (TextView) findViewById(messageId);
    }

    private void fillBoard(int id){
        Button currentBtn = (Button) findViewById(id);
        if(currentBtn.getText().equals("-")) {
            currentBtn.setText(String.valueOf(counter));
            counter++;
        }
        if(counter == 26){
            message.setText("");
        }
    }
    public void btnClick(View v) {
       if(counter < 26){
           fillBoard(v.getId());
       }else if(counterCompletedLines<5){
            strikeThroughBtn(v);
            winCheck(v);
       }
    }

    private void winCheck(View v) {
        for(int i=0;i<winningPositions.length; i++){
            if( !completedLine.contains(i) && currentState[winningPositions[i][0] - 1] == 1 && currentState[winningPositions[i][0]-1]  == currentState[winningPositions[i][1]-1] &&
                    currentState[winningPositions[i][1]-1]== currentState[winningPositions[i][2]-1] &&
                    currentState[winningPositions[i][2]-1]==currentState[winningPositions[i][3]-1]
            && currentState[winningPositions[i][3]-1]==currentState[winningPositions[i][4]-1]
            ){
                colorLine(winningPositions[i]);
                completedLine.add(i);
                counterCompletedLines++;
                int id = getResources().getIdentifier("bingo"+ counterCompletedLines , "id", getPackageName());
                Button btn = (Button) findViewById(id);
                btn.setVisibility(View.VISIBLE);
                btn.setBackgroundColor(color[counterCompletedLines-1]);
                if(counterCompletedLines ==5){
                    message.setText("YOU WON!!");
                    break;
                }
            }
        }
    }

    private void colorLine(int[] winPos) {
        for(int currentPos : winPos) {
            int id = getResources().getIdentifier("button"+ currentPos, "id", getPackageName());
            Button btn = (Button) findViewById(id);
            btn.setBackgroundColor(color[counterCompletedLines]);
        }
    }

    private void strikeThroughBtn(View v) {
        Button currentBtn = (Button) findViewById(v.getId());
        String tag = v.getTag().toString();
        currentState[Integer.parseInt(tag)-1] = 1;
        currentBtn.setPaintFlags(currentBtn.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void reset(View v){
        for (int i = 1; i <= 25; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button currentBtn = (Button) findViewById(id);
            currentBtn.setText("-");
            currentBtn.setBackgroundColor(Color.parseColor("#FF6200EE"));
            currentState[i-1] = 0;
            currentBtn.setPaintFlags(currentBtn.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        for(int i=1;i<=5;i++){
            int id = getResources().getIdentifier("bingo"+ i , "id", getPackageName());
            Button btn = (Button) findViewById(id);
            btn.setVisibility(View.INVISIBLE);
        }
        counter =1;
        counterCompletedLines =0;
        completedLine.clear();
        message.setText("Click on tiles to fill the board");
    }
}