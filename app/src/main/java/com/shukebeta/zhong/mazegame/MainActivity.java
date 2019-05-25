package com.shukebeta.zhong.mazegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import ara.bc282.assignment1.zhong.GameMap;
import ara.bc282.assignment1.zhong.Piece;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tl.removeAllViews();
    }

    public void onClickStage1(View view) {
        drawStage(1, view);
    }

    private void drawStage(int stageNum, View view) {
        GameMap m = new GameMap(stageNum);

        ConstraintLayout mainLayout = findViewById(R.id.maze_game);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        /* Find Tablelayout defined in main.xml */
        TableLayout tl = findViewById(R.id.tableLayout);
        tl.removeAllViews();
        for (int row = 0; row < 6; row++) {
            /* Create a new row to be added. */
            TableRow tr = new TableRow(this);
            int id = View.generateViewId();
            tr.setId(id);

            for (int col = 0; col < 4; col++) {
                ImageButton b = new ImageButton(this);
                Piece p = m.getPiece(row, col);
                String filename = p.colour.toString().toLowerCase() + '_' + p.shape.toString().toLowerCase();
                String uri = "@drawable/" + filename;  // where myresource (without the extension) is the file

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                //Drawable res = ResourcesCompat.getDrawable(getResources(), imageResource, null);
                b.setImageResource(imageResource);
                /* Add Button to row. */
                tr.addView(b);
            }


            /* Add row to TableLayout. */
            tl.addView(tr);
            constraintSet.connect(tr.getId(), ConstraintSet.TOP, tl.getId(), ConstraintSet.TOP, 16);
            constraintSet.connect(tr.getId(), ConstraintSet.END, tl.getId(), ConstraintSet.END, 16);
            constraintSet.constrainHeight(tr.getId(), ConstraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(tr.getId(), ConstraintSet.WRAP_CONTENT);
        }
        constraintSet.applyTo(mainLayout);
    }
}
