package com.shukebeta.zhong.mazegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ara.bc282.assignment1.zhong.Directions;
import ara.bc282.assignment1.zhong.Eyeball;
import ara.bc282.assignment1.zhong.Piece;
import ara.bc282.assignment1.zhong.Sprite;


public class MainActivity extends AppCompatActivity {

    public Eyeball currentGame;
    private int[][] viewIdList;

    private boolean soundOn = true;


    private final int INIT = 0;
    private final int RUNNING = 1;
    private final int WIN = 2;
    private final int FAIL = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkVisible(INIT);
        //soundOn = setSoundOn(findViewById(R.id.cb_sound_effect));
        //tl.removeAllViews();
    }

    private void checkVisible(int period) {
        checkLoadBtn();
        switch (period) {
            case INIT:
                int[] needHide = {
                        R.id.btn_save,
                        R.id.btn_undo,
                        R.id.btn_restart,
                };
                for (int btnId : needHide) {
                    View btn = findViewById(btnId);
                    btn.setVisibility(View.INVISIBLE);
                }
                break;
            case RUNNING:
                Log.d("test", currentGame.sprite.canUndo() ? "can" : "cannot");
                if (currentGame.sprite.canUndo()) {
                    int[] needShow = {
                            R.id.btn_save,
                            R.id.btn_undo,
                            R.id.btn_restart,
                    };
                    for (int btnId : needShow) {
                        View btn = findViewById(btnId);
                        btn.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case WIN:
            case FAIL:
                int[] needH = {
                        R.id.btn_save,
                        R.id.btn_undo,
                };
                for (int btnId : needH) {
                    View btn = findViewById(btnId);
                    btn.setVisibility(View.INVISIBLE);
                }
                break;
            default:
        }
    }

    private void checkLoadBtn() {
        // todo check savedList.length
        int visible = (true) ? View.VISIBLE : View.INVISIBLE;
        findViewById(R.id.btn_load).setVisibility(visible);
    }

    public void onClickStage1(View view) {
        drawStage(0, view);
    }

    public void onClickStage2(View view) {
        drawStage(1, view);
    }

    private void drawStage(int stageNum, View view) {
        currentGame = new Eyeball();
        currentGame.start(stageNum);

        viewIdList = new int[currentGame.currentMap.map.length][currentGame.currentMap.map[0].length];

        ConstraintLayout mainLayout = findViewById(R.id.maze_game);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        /* Find Tablelayout defined in main.xml */
        TableLayout tl = findViewById(R.id.tableLayout);
        tl.removeAllViews();
        for (int row = 0; row < currentGame.currentMap.map.length; row++) {
            /* Create a new row to be added. */
            TableRow tr = new TableRow(this);
            int id = View.generateViewId();
            tr.setId(id);

            for (int col = 0; col < currentGame.currentMap.map[row].length; col++) {
                ImageButton btn = new ImageButton(this);
                int btnId = View.generateViewId();
                btn.setId(btnId);
                viewIdList[row][col] = btnId;

                Piece p = currentGame.currentMap.getPiece(row, col);

                int shapeRes = getGameResource(p.colour.toString() + "_" + p.shape.toString());
                //Drawable res = ResourcesCompat.getDrawable(getResources(), imageResource, null);
                btn.setTag(p);
                /* Add Button to row. */
                if (p.isStartPoint()) {
                    int spriteRes = getGameResource("eyesu");
                    setSpriteOnPiece(btn, shapeRes, spriteRes);
                } else if (p.isGoal()) {
                    int goalRes = getGameResource("goal");
                    setSpriteOnPiece(btn, shapeRes, goalRes);
                } else {
                    btn.setImageResource(shapeRes);
                }
                btn.setClickable(true);
                btn.setOnClickListener(this::shapeClick);
                btn.setPadding(0,0,0,0);
                btn.setBackground(null);
                tr.addView(btn);
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

    private int getGameResource(String r) {
        return getResources().getIdentifier("@drawable/" + r.toLowerCase(), null, getPackageName());
    }

    private int getGameResource(Piece p) {
        String r = p.colour.toString() + "_" + p.shape.toString();
        return getGameResource(r);
    }

    private void setSpriteOnPiece(ImageButton btn, int shapeRes, int otherRes) {
        Bitmap shape = BitmapFactory.decodeResource(getResources(), shapeRes);
        Bitmap sprite = BitmapFactory.decodeResource(getResources(), otherRes);
        Bitmap mergedImages = createSingleImageFromMultipleImages(shape, sprite);
        btn.setImageBitmap(mergedImages);
    }

    // https://inducesmile.com/android-programming/how-to-combine-multiple-images-to-a-single-image-in-android/
    private Bitmap createSingleImageFromMultipleImages(Bitmap firstImage, Bitmap secondImage) {
        Bitmap result = Bitmap.createBitmap(secondImage.getWidth(), secondImage.getHeight(), secondImage.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(firstImage, 0f, 0f, null);
        canvas.drawBitmap(secondImage, 0f, 0f, null);
        return result;
    }

    public void shapeClick(View view) {
        Piece p = (Piece)view.getTag();

        Piece previousP = currentGame.sprite.currentPiece;
        ImageButton previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        if (currentGame.sprite.walkTo(p.x, p.y)) {
            succeed();

            int res = getGameResource(previousP);
            // remove sprite from previous btn image
            previousI.setImageResource(res);
            // draw sprite on current piece
            setSpriteOnPiece((ImageButton)view,
                    getGameResource(currentGame.sprite.currentPiece),
                    getGameResource(getEyesByDirection(currentGame.sprite.currentDirection))
            );
            if (p.isGoal()) {
                congratulations();
                checkVisible(WIN);
                // todo: show can do list
            } else {
                checkVisible(RUNNING);
            }
            updateTotalMove();
        } else {
            warning();
            //draw X on target piece
            String shadow = "bigx";
            if (p.isGoal()) {
                shadow = "xgoal";
            }
            setSpriteOnPiece((ImageButton)view,
                    getGameResource(p),
                    getGameResource(shadow)
            );

            // https://blog.csdn.net/maoyuanming0806/article/details/77088520
            // https://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> {
                        // Stuff that updates the UI
                        if (p.isGoal()) {
                            setSpriteOnPiece((ImageButton) view, getGameResource(p), getGameResource("goal"));
                        } else {
                            ((ImageButton) view).setImageResource(getGameResource(p));
                        }
                    });
                }
            }, 1000);
        }
    }

    private String getEyesByDirection(Directions d) {
        switch(d) {
            case SOUTH:
                return "eyesd";
            case NORTH:
                return "eyesu";
            case WEST:
                return "eyesl";
            case EAST:
                return "eyesr";
        }
        return "eyesu";
    }

    public void undoClick(View view) {
        Piece previousP = currentGame.sprite.currentPiece;
        ImageButton previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        if (currentGame.sprite.undoWalk()) {
            succeed();
            int res = getGameResource(previousP);
            // remove sprite from previous btn image
            previousI.setImageResource(res);
            // draw sprite on current piece
            Piece p = currentGame.sprite.currentPiece;
            setSpriteOnPiece((ImageButton)findViewById(viewIdList[p.x][p.y]),
                    getGameResource(currentGame.sprite.currentPiece),
                    getGameResource(getEyesByDirection(currentGame.sprite.currentDirection))
            );
            updateTotalMove();
        } else {
            warning();
            // todo: display a warning message
        }
        checkVisible(RUNNING);
    }

    public void restartClick(View view) {
        Piece previousP = currentGame.sprite.currentPiece;
        ImageButton previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        currentGame.restart();
        int res = getGameResource(previousP);
        // remove sprite from previous btn image
        previousI.setImageResource(res);
        // draw sprite on current piece
        Piece p = currentGame.sprite.currentPiece;
        setSpriteOnPiece((ImageButton)findViewById(viewIdList[p.x][p.y]),
                getGameResource(currentGame.sprite.currentPiece),
                getGameResource(getEyesByDirection(currentGame.sprite.currentDirection))
        );
        succeed();
        checkVisible(INIT);
        updateTotalMove();
    }

    private void congratulations() {
        play("congratulations");
    }

    private void succeed() {
        play("succeed");
    }

    private void warning() {
        play("warning");
    }

    public void setSoundOn(View view) {
        soundOn = ((CheckBox)view).isChecked();
    }

    private void play(String sound) {
        if (soundOn) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + sound);
            MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this, uri);
            mediaPlayer.start();
        }
    }

    private void updateTotalMove() {
        int totalMove = currentGame.sprite.getTotalMove();
        String sTotalMove = "";
        if (totalMove > 0) {
            sTotalMove += totalMove;
        }
        ((TextView)findViewById(R.id.step_count)).setText(sTotalMove);
    }

}
