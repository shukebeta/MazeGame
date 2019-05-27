package com.shukebeta.zhong.mazegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import ara.bc282.assignment1.zhong.Directions;
import ara.bc282.assignment1.zhong.Eyeball;
import ara.bc282.assignment1.zhong.GameMap;
import ara.bc282.assignment1.zhong.Piece;

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
        drawStage(0);
        setSoundOn(findViewById(R.id.cb_sound_effect));
    }

    private void setBtnStatus(boolean canUndo) {
        checkLoadBtn();
        if (canUndo) {
            int[] needShow = {
                    R.id.btn_save,
                    R.id.btn_undo,
                    R.id.btn_restart,
                    R.id.btn_show_solution,
            };
            for (int btnId : needShow) {
                View btn = findViewById(btnId);
                btn.setVisibility(View.VISIBLE);
            }
        } else {
            int[] needHide = {
                    R.id.btn_undo,
                    R.id.btn_restart,
                    R.id.btn_save,
            };
            for (int btnId : needHide) {
                View btn = findViewById(btnId);
                btn.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void checkLoadBtn() {
        // todo check savedList.length
        int visible = (true) ? View.VISIBLE : View.INVISIBLE;
        findViewById(R.id.btn_load).setVisibility(visible);
    }

    public void onClickStage1(View view) {
        drawStage(0);
    }

    public void onClickStage2(View view) {
        drawStage(1);
    }

    private void drawStage(int stageNum) {
        Button[] stageBtnList = {
                (Button)findViewById(R.id.btn_stage1),
                (Button)findViewById(R.id.btn_stage2)
        };

        for(int i = 0; i < stageBtnList.length; i++) {
            Button b = stageBtnList[i];
            if (i == stageNum) {
                b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                b.setPaintFlags(b.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        }

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
                ImageView btn = new ImageView(this);
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
                    setMergedBitmapOnPiece(btn, shapeRes, spriteRes);
                } else if (p.isGoal()) {
                    int goalRes = getGameResource("goal");
                    setMergedBitmapOnPiece(btn, shapeRes, goalRes);
                } else {
                    btn.setImageResource(shapeRes);
                }
                btn.setClickable(true);
                btn.setOnClickListener(this::shapeClick);

                btn.setPadding(-2,-2,-2,-2);
                btn.setCropToPadding(false);
                btn.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        setBtnStatus(false);
        updateTotalMove();
    }

    private int getGameResource(String r) {
        return getResources().getIdentifier("@drawable/" + r.toLowerCase(), null, getPackageName());
    }

    private int getGameResource(Piece p) {
        String r = p.colour.toString() + "_" + p.shape.toString();
        return getGameResource(r);
    }

    private void setMergedBitmapOnPiece(ImageView btn, int res, int ... resources) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), res);
        Bitmap result = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());;
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(b, 0f, 0f, null);
        for(int i = 0; i < resources.length; i++) {
            b = BitmapFactory.decodeResource(getResources(), resources[i]);
            canvas.drawBitmap(b, 0f, 0f, null);
        }
        btn.setImageBitmap(result);
    }

    public void shapeClick(View view) {
        Piece p = (Piece)view.getTag();

        Piece previousP = currentGame.sprite.currentPiece;
        ImageView previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        if (currentGame.sprite.walkTo(p.x, p.y)) {
            succeed();

            int res = getGameResource(previousP);
            // remove sprite from previous btn image
            previousI.setImageResource(res);
            // draw sprite on current piece
            if (p.isGoal()) {
                setBtnStatus(false);
                setMergedBitmapOnPiece((ImageView)view,
                        getGameResource(currentGame.sprite.currentPiece),
                        getGameResource("goal"),
                        getGameResource(getEyesByDirection(currentGame.sprite.currentDirection))
                );
                congratulations();
                // todo: show can do list
            } else {
                setMergedBitmapOnPiece((ImageView)view,
                        getGameResource(currentGame.sprite.currentPiece),
                        getGameResource(getEyesByDirection(currentGame.sprite.currentDirection))
                );
                setBtnStatus(currentGame.sprite.canUndo());
            }
            updateTotalMove();
        } else {
            warning();
            //draw X on target piece
            String shadow = "bigx";
            if (p.isGoal()) {
                shadow = "xgoal";
            }
            setMergedBitmapOnPiece((ImageView)view,
                    getGameResource(p),
                    getGameResource(shadow)
            );

            setTimeout(() -> {
                // Stuff that updates the UI
                if (p.isGoal()) {
                    setMergedBitmapOnPiece((ImageView) view, getGameResource(p), getGameResource("goal"));
                } else {
                    ((ImageView) view).setImageResource(getGameResource(p));
                }
            }, 1);
        }
    }

    // https://blog.csdn.net/maoyuanming0806/article/details/77088520
    // https://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
    private void setTimeout(Runnable r, int seconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(r);
            }
        }, seconds * 1000);
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
        ImageView previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        if (currentGame.sprite.undoWalk()) {
            succeed();
            int shapeRes = getGameResource(previousP);
            // remove sprite from previous btn image
            if (previousP.isGoal()) {
                setMergedBitmapOnPiece(previousI, shapeRes, getGameResource("goal"));
            } else {
                previousI.setImageResource(shapeRes);
            }
            // draw sprite on current piece
            Piece p = currentGame.sprite.currentPiece;
            setMergedBitmapOnPiece((ImageView)findViewById(viewIdList[p.x][p.y]),
                    getGameResource(currentGame.sprite.currentPiece),
                    getGameResource(getEyesByDirection(currentGame.sprite.currentDirection))
            );
            updateTotalMove();
        } else {
            warning();
            // todo: display a warning message
        }
        setBtnStatus(currentGame.sprite.canUndo());
    }

    public void restartClick(View view) {
        drawStage(currentGame.currentStage);
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


    public void saveClick(View view) {
        //first save:
    }

    public void showSolutionClick(View view) {
        view.setClickable(false);
        int stage = currentGame.currentStage;
        int[][] solution = GameMap.solutionList[stage];
        drawStage(stage);
        int stepTotal = solution.length;
        int stepPause = 1000;
        int timeTotal = (stepTotal + 1) * stepPause;

        // https://stackoverflow.com/questions/56324121/is-there-an-approach-which-can-delay-certain-seconds-between-every-function-call
        new CountDownTimer(timeTotal, stepPause) {
            int stepIndex = 0;
            public void onTick(long timeRemain) {
                int[] pos = solution[stepIndex];
                ImageView v = (ImageView)findViewById(viewIdList[pos[0]][pos[1]]);
                v.callOnClick();
                stepIndex += 1;
                Log.d("time remain:", "" + timeRemain / 1000);
            }

            public void onFinish() {
                Log.d("final step", ":" + (stepIndex - 1));
                view.setClickable(true);
            }
        }.start();
    }

}
