package com.shukebeta.zhong.mazegame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

import ara.bc282.assignment1.rochuang.Direction;
import ara.bc282.assignment1.rochuang.EyeballGame;
import ara.bc282.assignment1.rochuang.GameGridIron;
import ara.bc282.assignment1.rochuang.Piece;

public class RocActivity extends AppCompatActivity {
    final int MAX_PLAY_TIME = 120000; // 120s

    public EyeballGame currentGame;
    private int[][] viewIdList;

    private boolean soundOn = true;
    private Timer timer;

    int currentStage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roc);
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
        EyeBallDatabase eyeBallDatabase= EyeBallDatabase.getInstance(this);
        EyeBallDao dao = eyeBallDatabase.eyeBallDao();

        CompletableFuture
                .supplyAsync(() -> dao.getTotal() > 0)
                .thenAccept((i) -> runOnUiThread(() -> {
                    int visible = i ? View.VISIBLE : View.INVISIBLE;
                    findViewById(R.id.btn_load).setVisibility(visible);
                }));
    }

    public void stage1Click(View view) {
        drawStage(0);
    }

    public void stage2Click(View view) {
        drawStage(1);
    }

    private void drawStage(int stageNum) {
        currentStage = stageNum;
        if (timer != null) {
            timer.cancel();
        }
        Button[] stageBtnList = {
                findViewById(R.id.btn_stage1),
                findViewById(R.id.btn_stage2)
        };

        for(int i = 0; i < stageBtnList.length; i++) {
            Button b = stageBtnList[i];
            if (i == stageNum) {
                b.setPaintFlags(b.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            } else {
                b.setPaintFlags(b.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
            }
        }

        currentGame = new EyeballGame();
        currentGame.startGame(stageNum);
        Piece[][] currentMap = currentGame.gr.ALL_MAP_LISTS[currentStage];

        viewIdList = new int[currentMap.length][currentMap[0].length];

        ConstraintLayout mainLayout = findViewById(R.id.maze_game_R);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(mainLayout);

        /* Find TableLayout defined in main.xml */
        TableLayout tl = findViewById(R.id.tableLayout);
        tl.removeAllViews();
        for (int row = 0; row < currentMap.length; row++) {
            /* Create a new row to be added. */
            TableRow tr = new TableRow(this);
            int id = View.generateViewId();
            tr.setId(id);
            Log.d("hello:", "" + currentMap[row].length);
            for (int col = 0; col < currentMap[row].length; col++) {
                ImageView btn = new ImageView(this);
                int btnId = View.generateViewId();
                btn.setId(btnId);
                viewIdList[row][col] = btnId;

                Piece p = currentGame.gr.findPieceInMap(row, col);

                int shapeRes = getGameResource(p.myColour.toString() + "_" + p.myShape.toString());
                //Drawable res = ResourcesCompat.getDrawable(getResources(), imageResource, null);
                btn.setTag(p);
                /* Add Button to row. */
                if (p.isStartPoint()) {
                    int spriteRes = getGameResource("eyesu");
                    setMergedBitmapOnPiece(btn, shapeRes, spriteRes);
                } else if (p.isEndPoint()) {
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

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> updateTotalMove());
            }
        };
        timer.schedule(task,1000,1000);
    }

    private int getGameResource(String r) {
        return getResources().getIdentifier("@drawable/" + r.toLowerCase(), null, getPackageName());
    }

    private int getGameResource(Piece p) {
        String r = p.myColour.toString() + "_" + p.myShape.toString();
        return getGameResource(r);
    }

    private void setMergedBitmapOnPiece(ImageView btn, int res, int ... resources) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), res);
        Bitmap result = Bitmap.createBitmap(b.getWidth(), b.getHeight(), b.getConfig());
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(b, 0f, 0f, null);
        for (int resource : resources) {
            b = BitmapFactory.decodeResource(getResources(), resource);
            canvas.drawBitmap(b, 0f, 0f, null);
        }
        btn.setImageBitmap(result);
    }

    public void shapeClick(View view) {
        Piece p = (Piece)view.getTag();

        Piece previousP = currentGame.eb.myCurrentPiece;
        ImageView previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        if (previousP.x == p.x && previousP.y == p.y) {
            warning();
            Toast.makeText(this, "You cannot move to yourself.", Toast.LENGTH_LONG).show();
            return;
        }
        if (currentGame.eb.moveToNextPieceSucceed(p.x, p.y)) {
            succeed();

            int res = getGameResource(previousP);
            // remove sprite from previous btn image
            previousI.setImageResource(res);
            // draw sprite on current piece
            if (p.isEndPoint()) {
                timer.cancel();
                setBtnStatus(false);
                setMergedBitmapOnPiece((ImageView)view,
                        getGameResource(currentGame.eb.myCurrentPiece),
                        getGameResource("goal"),
                        getGameResource(getEyesByDirection(currentGame.eb.myCurrentDirection))
                );
                congratulations();
                showChoiceList("What would you like to do next?");
            } else {
                setMergedBitmapOnPiece((ImageView)view,
                        getGameResource(currentGame.eb.myCurrentPiece),
                        getGameResource(getEyesByDirection(currentGame.eb.myCurrentDirection))
                );
                setBtnStatus(currentGame.eb.countTotalMove() > 1);
            }
            updateTotalMove();
        } else {
            warning();
            //draw X on target piece
            String shadow = "bigx";
            if (p.isEndPoint()) {
                shadow = "xgoal";
            }
            setMergedBitmapOnPiece((ImageView)view,
                    getGameResource(p),
                    getGameResource(shadow)
            );
            Toast.makeText(this, "You can only move to the piece which is in the same colour or same shape while it is in the same row or same column.", Toast.LENGTH_SHORT).show();

            setTimeout(() -> {
                // Stuff that updates the UI
                if (p.isEndPoint()) {
                    setMergedBitmapOnPiece((ImageView) view, getGameResource(p), getGameResource("goal"));
                } else {
                    ((ImageView) view).setImageResource(getGameResource(p));
                }
            }, 1000);
        }
    }

    private void showChoiceList(String title) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // add a list
        String[] choiceList;
        if (currentStage == 1) {
            choiceList = new String[] {
                    "Replay Current Stage",
                    "Replay Stage 1",
                    "I am ok"
            };
        } else {
            choiceList = new String[] {
                    "Replay Current Stage",
                    "Play Stage 2",
                    "I am ok"
            };
        }
        builder.setItems(choiceList, (dialog, which) -> {
            switch (which) {
                case 0: // replay current stage
                    restartClick(findViewById(R.id.btn_restart));
                    break;
                case 1: // play another stage
                    if (currentStage == 0) {
                        drawStage(1);
                    } else {
                        drawStage(0);
                    }
                    break;
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // https://blog.csdn.net/maoyuanming0806/article/details/77088520
    // https://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
    private void setTimeout(Runnable r, int millseconds) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(r);
            }
        }, millseconds);
    }

    private String getEyesByDirection(Direction d) {
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
        Piece previousP = currentGame.eb.myCurrentPiece;
        ImageView previousI = findViewById(viewIdList[previousP.x][previousP.y]);
        if (currentGame.eb.moveBackPreviousPieceSucceed(previousP.x, previousP.y)) {
            succeed();
            int shapeRes = getGameResource(previousP);
            // remove sprite from previous btn image
            if (previousP.isEndPoint()) {
                setMergedBitmapOnPiece(previousI, shapeRes, getGameResource("goal"));
            } else {
                previousI.setImageResource(shapeRes);
            }
            // draw sprite on current piece
            Piece p = currentGame.eb.myCurrentPiece;
            setMergedBitmapOnPiece(findViewById(viewIdList[p.x][p.y]),
                    getGameResource(currentGame.eb.myCurrentPiece),
                    getGameResource(getEyesByDirection(currentGame.eb.myCurrentDirection))
            );
            updateTotalMove();
        } else {
            warning();
            Toast.makeText(this, "You have got the start point, cannot undo anymore.", Toast.LENGTH_SHORT).show();
        }
        setBtnStatus(currentGame.eb.countTotalMove() > 1);
    }

    public void restartClick(View view) {
        drawStage(currentStage);
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
            try {
                Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/" + sound);
                MediaPlayer mediaPlayer = MediaPlayer.create(RocActivity.this, uri);
                mediaPlayer.start();
            } catch( Exception e) {
                Log.d("Sound error:", e.toString());
            }
        }
    }

    private void updateTotalMove() {
        int totalMove = currentGame.eb.countTotalMove();
        String sTotalMove = "";
        if (totalMove > 0) {
            sTotalMove += totalMove;
        }
        if (!getCostTime().equals("")) {
            sTotalMove += " " + getCostTime();
        }
        ((TextView)findViewById(R.id.step_count)).setText(sTotalMove);
        if (currentGame.eb.getTotalMoveSpendingTime() > MAX_PLAY_TIME) {
            timer.cancel();
            warning();
            showChoiceList("Timeout...You should have finished the stage in " + MAX_PLAY_TIME / 60000 + " minutes, what would you like to do next?");
        }
    }

    public void saveClick(View view) {
        String progressName = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());
        String gameStage = String.valueOf(currentStage);
        EyeBallProgress aRec = new EyeBallProgress(1, progressName, gameStage, String.valueOf(currentGame.eb.getTotalMoveSpendingTime()), walkedPiece2String());

        EyeBallDatabase eyeBallDatabase= EyeBallDatabase.getInstance(this);
        EyeBallDao dao = eyeBallDatabase.eyeBallDao();

        CompletableFuture
                .runAsync(dao::clear)
                .thenRunAsync(() -> dao.insert(aRec))
                .thenRun(() -> runOnUiThread(
                        () -> Toast.makeText(this, "Eyeball progress saved.", Toast.LENGTH_SHORT).show()));
    }

    private void recoverImage(Piece piece) {
        int shapeRes = getGameResource(piece);
        ImageView p = findViewById(viewIdList[piece.x][piece.y]);
        p.setImageResource(shapeRes);
    }

    public void loadClick(View view) {
        EyeBallDatabase eyeBallDatabase= EyeBallDatabase.getInstance(this);
        EyeBallDao dao = eyeBallDatabase.eyeBallDao();
        CompletableFuture.supplyAsync(() -> dao.get(1)).thenAcceptAsync(aRec ->
                runOnUiThread(() -> {
                    drawStage(Integer.valueOf(aRec.getGameStage()));
                    currentGame.eb.myStartTime = new Timestamp((new Date()).getTime() - Integer.valueOf(aRec.getGameCostTime()));
                    currentGame.eb.myAllMovedPieces = string2WalkedPieceList(aRec.getWalkedPieceList());
                    currentGame.eb.myCurrentPiece = currentGame.eb.getMyAllMovedPieces().get(currentGame.eb.getMyAllMovedPieces().size() - 1);
                    currentGame.eb.myCurrentDirection = currentGame.eb.myCurrentPiece.myDirection;
                    // recover start point image
                    recoverImage(currentGame.eb.getMyAllMovedPieces().get(0));
                    //
                    int shapeRes = getGameResource(currentGame.eb.myCurrentPiece);
                    int spriteRes = getGameResource(getEyesByDirection(currentGame.eb.myCurrentDirection));
                    ImageView btn = findViewById(viewIdList[currentGame.eb.myCurrentPiece.x][currentGame.eb.myCurrentPiece.y]);
                    setMergedBitmapOnPiece(btn, shapeRes, spriteRes);
                    updateTotalMove();
                    setBtnStatus(true);
                    Toast.makeText(this, "Eyeball record loaded.", Toast.LENGTH_SHORT).show();
                })
        );
    }

    private String getCostTime() {
        Date d = new Date(currentGame.eb.getTotalMoveSpendingTime());
        SimpleDateFormat df = new SimpleDateFormat("mm:ss", Locale.getDefault()); // HH for 0-23
        return df.format(d);
    }

    private String walkedPiece2String() {
        ArrayList<Piece> w = currentGame.eb.getMyAllMovedPieces();
        int len = w.size();
        String[] res = new String[len];
        for(int i=0; i<len; i++) {
            Piece p =  w.get(i);
            res[i] = p.x +"," + p.y + "," + p.myDirection.toString();
        }
        return TextUtils.join(":", res);
    }

    private ArrayList<Piece> string2WalkedPieceList(String walkedList) {
        ArrayList<Piece> w = new ArrayList<>();
        for( String s : TextUtils.split(walkedList, ":")) {
            String[] p = TextUtils.split(s, ",");
            Piece piece = currentGame.gr.findPieceInMap(Integer.valueOf(p[0]), Integer.valueOf(p[1]));
            piece.setDirection(Direction.valueOf(p[2]));
            w.add(piece);
        }
        return w;
    }

    public void showSolutionClick(View view) {
        view.setClickable(false);
        int stage = currentStage;
        int[][] solution = GameGridIron.ALL_SOLUTION_LISTS[stage];
        drawStage(stage);
        int stepTotal = solution.length;
        int stepPause = 1000;
        int timeTotal = (stepTotal + 1) * stepPause;

        // https://stackoverflow.com/questions/56324121/is-there-an-approach-which-can-delay-certain-seconds-between-every-function-call
        new CountDownTimer(timeTotal, stepPause) {
            int stepIndex = 0;
            public void onTick(long timeRemain) {
                int[] pos = solution[stepIndex];
                ImageView v = findViewById(viewIdList[pos[0]][pos[1]]);
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
