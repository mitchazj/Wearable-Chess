package com.orangutandevelopment.wearablechess;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.os.Vibrator;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.datatype.Duration;

/**
 * Created by mitch on 4/10/2015.
 */
public class ChessboardView extends View {
    Bitmap[] im;
    static final int x = 10, z = 15, M = 10000;
    Paint pSquare = new Paint();
    Paint pOutline = new Paint();
    Bitmap b_sq_black;
    Bitmap b_sq_white;
    int width = 0;

    public ArrayList<Integer> validSq = new ArrayList<>();
    public Chess toledo;
    public int selected_square = -1;

    boolean is_drawing = false;

    boolean Highlights = true;
    boolean Vibration = false;

    private ArrayList<Chess> history = new ArrayList<>();
    private int history_index = 0;

    public static final String PREFS_NAME = "WearableChessPrefs";

    public ChessboardView(Context context) {
        super(context);
        init();
    }

    public ChessboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChessboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void NewGame() {
        validSq.clear();
        selected_square = -1;
        toledo = new Chess();
        Z();
    }

    public void StoreToHistory() {
        if (history_index < history.size()) {
            Chess record = new Chess();
            record.CopyBoard(toledo);
            history.set(history_index, record);
        }
        else {
            Chess record = new Chess();
            record.CopyBoard(toledo);
            history.add(record);
        }
        ++history_index;
    }

    public void Takeback(int steps) {
        if (history_index - steps > 0)
            toledo = history.get(history_index -= steps);
        Z();
    }

    private void init() {
        //Ensures that the app invalidates.
        setWillNotDraw(false);
        width = this.getWidth();

        toledo = new Chess();
        im = new Bitmap[16];

        // Restore Previous Session
        SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, 0);
        Vibration = settings.getBoolean("vibration", false);
        Highlights = settings.getBoolean("highlights", true);
        String board = settings.getString("board", "-");
        if (board != "-") {
            StringTokenizer st = new StringTokenizer(board, ",");
            int[] I = new int[411];
            for (int i = 0; i < I.length; ++i) {
                I[i] = Integer.parseInt(st.nextToken());
            }
            toledo.I = I;
        }
        toledo.B = settings.getInt("B", toledo.B);
        toledo.i = settings.getInt("i", toledo.i);
        toledo.y = settings.getInt("y", toledo.y);
        toledo.u = settings.getInt("u", toledo.u);
        toledo.L = settings.getInt("L", toledo.L);
        toledo.b = settings.getInt("b", toledo.b);

        pSquare.setStyle(Paint.Style.FILL);
        pOutline.setStyle(Paint.Style.STROKE);
        b_sq_black = BitmapFactory.decodeResource(this.getResources(), R.drawable.darksq);
        b_sq_white = BitmapFactory.decodeResource(this.getResources(), R.drawable.lightsq);

        im[1] = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn_st);
        im[2] = BitmapFactory.decodeResource(getResources(), R.drawable.black_king_st);
        im[3] = BitmapFactory.decodeResource(getResources(), R.drawable.black_knight_st);
        im[4] = BitmapFactory.decodeResource(getResources(), R.drawable.black_bishop_st);
        im[5] = BitmapFactory.decodeResource(getResources(), R.drawable.black_rook_st);
        im[6] = BitmapFactory.decodeResource(getResources(), R.drawable.black_queen_st);
        im[9] = BitmapFactory.decodeResource(getResources(), R.drawable.white_pawn_st);
        im[10] = BitmapFactory.decodeResource(getResources(), R.drawable.white_king_st);
        im[11] = BitmapFactory.decodeResource(getResources(), R.drawable.white_knight_st);
        im[12] = BitmapFactory.decodeResource(getResources(), R.drawable.white_bishop_st);
        im[13] = BitmapFactory.decodeResource(getResources(), R.drawable.white_rook_st);
        im[14] = BitmapFactory.decodeResource(getResources(), R.drawable.white_queen_st);

        im[0] = BitmapFactory.decodeResource(getResources(), R.drawable.fifteen);
        im[7] = BitmapFactory.decodeResource(getResources(), R.drawable.fifteen);
        im[8] = BitmapFactory.decodeResource(getResources(), R.drawable.fifteen);
        im[15] = BitmapFactory.decodeResource(getResources(), R.drawable.fifteen);

        this.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                    int s = (int) event.getX() / (width / 8) + ((int) event.getY() / (width / 8) * x + 21);
                    if (selected_square == -1) {
                        StoreToHistory(); //Record where we are up to.
                        selected_square = s;
                        validSq = toledo.getLegalMoves(s);
                        toledo.MovePart(s);
                        Z();
                    } else {
                        if (toledo.MovePart(s)) {
                            is_drawing = true;
                            selected_square = -1;
                            validSq.clear();
                            final int pieces = toledo.CountPieces();
                            Z();
                            new Thread(new Runnable() {
                                public void run() {
                                    while (is_drawing) {
                                        //Wait for UI to finish updating.
                                    }
                                    toledo.Respond();
                                    Z();
                                    //TODO: add support for the game finishing?
                                    if (Vibration) {
                                        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                                        if (toledo.CountPieces() < pieces) {
                                            //A capture has occurred.
                                            v.vibrate(new long[] {100, 100}, -1);
                                        } else {
                                            v.vibrate(100);
                                        }
                                    }
                                }
                            }).start();
                        } else {
                            //Illegal move? Start again from this square.
                            selected_square = s;
                            validSq = toledo.getLegalMoves(s);
                            toledo.MovePart(s);
                            Z();
                        }
                    }
                }
                return true;
            }
        });
    }

    void Z() {
        this.postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double x, y;
        int c = 21;
        boolean n = false;
        width = canvas.getWidth();

        double tile_width = ((double)width / 8.0);
        for (y = 0; y < width; y += tile_width) {
            for (x = 0; x < width; x += tile_width) {
                Rect r_sq = new Rect((int) x, (int) y, (int) (x + tile_width), (int) (y + tile_width));
                pSquare.setColor(Color.argb(255, 0, 0, 0));
                pSquare.setColor(n ? Color.argb(255, 144, 144, 208) : Color.argb(255, 192, 192, 255));
                canvas.drawBitmap(n ? b_sq_black : b_sq_white, new Rect(0, 0, b_sq_white.getWidth(), b_sq_white.getHeight()), r_sq, pSquare);

                if (Highlights) {
                    if (c == toledo.b || c == toledo.L) {
                        pSquare.setColor(Color.argb(120, 0, 0, 180));
                        canvas.drawRect(r_sq, pSquare);
                    }
                    if (validSq.indexOf(c) != -1) {
                        pSquare.setColor(Color.argb(120, 0, 180, 0));
                        canvas.drawRect(r_sq, pSquare);
                    }
                }
                if (c == toledo.B) {
                    pOutline.setStrokeWidth(1);
                    pOutline.setColor(Color.argb(255, 255, 255, 0));
                    canvas.drawRect((int)(x + 1), (int)(y + 1), (int)(x + tile_width), (int)(y + tile_width), pOutline);
                }

                pSquare.setColor(Color.argb(255, 0, 0, 0));
                canvas.drawBitmap(im[toledo.I[c] & z], new Rect(0, 0, im[toledo.I[c] & z].getWidth(), im[toledo.I[c] & z].getHeight()), r_sq, pSquare);
                c++;
                n = !n;
            }
            c += 2;
            n = !n;
        }

        is_drawing = false;
    }
}
