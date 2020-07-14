package com.example.gamebasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Bitmap spaceship;
    int spaceship_x, spaceship_y;
    int spaceshipWidth;

    Bitmap leftkey, rightkey;
    int leftkey_x, leftkey_y;
    int rightkey_x, rightkey_y;

    int Width, Height;
    int button_width;

    int score;

    Bitmap missileButton;
    int missileButton_x, missileButton_y;
    int missileWidth;

    Bitmap missile;
    Bitmap planetimg;

    int count;
    ArrayList<MyMissile> myM;
    ArrayList<Planet> planet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MyView(this));

        Display display = ((WindowManager)getSystemService(Context
                .WINDOW_SERVICE)).getDefaultDisplay();

        Width = display.getWidth();
        Height = display.getHeight();

        myM = new ArrayList<MyMissile>();
        planet = new ArrayList<Planet>();

        spaceship = BitmapFactory.decodeResource(getResources(), R.drawable.ddd);
        int x = Width/8;
        int y = Height/11;

        button_width = Width/6;

        spaceship = Bitmap.createScaledBitmap(spaceship, x, y, true);

        spaceshipWidth = spaceship.getWidth();
        spaceship_x = Width *1/9;
        spaceship_y = Height *6/9;

        leftkey = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        leftkey_x = Width *5/9;
        leftkey_y = Height *7/9;
        leftkey = Bitmap.createScaledBitmap(leftkey, button_width, button_width, true);

        rightkey = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        rightkey_x = Width *7/9;
        rightkey_y = Height *7/9;
        rightkey = Bitmap.createScaledBitmap(rightkey, button_width, button_width, true);

        missileButton = BitmapFactory.decodeResource(getResources(), R.drawable.button);
        missileButton = Bitmap.createScaledBitmap(missileButton, button_width, button_width, true);
        missileButton_x = Width *1/11;
        missileButton_y = Height *7/9;

        missile = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        missile = Bitmap.createScaledBitmap(missile, button_width/4, button_width/4, true);
        missileWidth = missile.getWidth();

        planetimg = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_round);
        planetimg = Bitmap.createScaledBitmap(planetimg, button_width, button_width, true);

    }

    class MyView extends View {
        MyView(Context context) {
            super(context);
            setBackgroundColor(Color.BLACK);
            gHandler.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        synchronized public void onDraw(Canvas canvas) {

            long seed = System.currentTimeMillis();

            Random r1 = new Random();
            Random r2 = new Random(seed);

            int x = r1.nextInt(Width);
            int bound = 2;
            int h = 200;

            if(planet.size() < 5) {
                planet.add(new Planet(x, r2.nextInt(h+100), r2.nextInt(bound)));
            }

            Paint p1 = new Paint();
            p1.setColor(Color.RED);
            p1.setTextSize(50);

            canvas.drawText(Integer.toString(count), 0, 300, p1);
            canvas.drawText("점수: " + Integer.toString(score), 0, 200, p1);

            canvas.drawBitmap(spaceship, spaceship_x, spaceship_y, p1);
            canvas.drawBitmap(leftkey, leftkey_x, leftkey_y, p1);
            canvas.drawBitmap(rightkey, rightkey_x, rightkey_y, p1);
            canvas.drawBitmap(missileButton, missileButton_x, missileButton_y, p1);

            for(MyMissile tmp : myM) {
                canvas.drawBitmap(missile, tmp.x, tmp.y, p1);
            }
            for(Planet tmp : planet) {
                canvas.drawBitmap(planetimg, tmp.x, tmp.y, p1);
            }

            moveMissile();
            movePlanet();
            checkCollision();
            count++;
        }

        public void moveMissile() {
            for(int i=myM.size()-1; i>=0; i--)
                myM.get(i).move();
            for(int i=myM.size()-1; i>=0; i--)
                if(myM.get(i).y < 0) myM.remove(i);
        }
        public void movePlanet() {
            for(int i=planet.size()-1; i>=0; i--)
                planet.get(i).move();
            for(int i=planet.size()-1; i>=0; i--)
                if(planet.get(i).x>Width || planet.get(i).x<0) planet.remove(i);
        }
        public void checkCollision() {
            for(int i=planet.size()-1; i>=0; i--)
                for(int j=myM.size()-1; j>=0; j--)
                    if(myM.get(j).x +(missileWidth/2) > planet.get(i).x
                      && myM.get(j).x +(missileWidth/2) < planet.get(i).x +button_width
                      && myM.get(j).y > planet.get(i).y -10
                      && myM.get(j).y < planet.get(i).y +button_width) {
                        planet.remove(i);
                        myM.remove(j);
                        score += 10;
                    }
        }

        Handler gHandler = new Handler() {

            public void handleMessage(Message msg) {
                invalidate();
                gHandler.sendEmptyMessageDelayed(0, 30);
            }
        };

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x=0, y=0;

            if(event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_MOVE) {
                x = (int)event.getX();
                y = (int)event.getY();
            }
            if((x>leftkey_x) && (x<leftkey_x +button_width)
                    && (y>leftkey_y) && (y<leftkey_y +button_width)) {
                if(spaceship_x >= 0)
                   spaceship_x -= 20;
            }
            if((x>rightkey_x) && (x<rightkey_x +button_width)
                    && (y>rightkey_y) && (y<rightkey_y +button_width)) {
                if(spaceship_x <= Width-spaceshipWidth)
                   spaceship_x += 20;
            }
            if(event.getAction() == MotionEvent.ACTION_DOWN)
                if((x>missileButton_x) && (x<missileButton_x +button_width)
                   && (y>missileButton_y) && (y<missileButton_y + button_width))
                    if(myM.size() < 2) {
                        myM.add(new MyMissile(spaceship_x + spaceshipWidth/2 - missileWidth/2, spaceship_y));
                    }

            return true;
        }
    }
}
