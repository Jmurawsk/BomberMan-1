package com.bomber.man;

import com.bomber.man.power_ups.PowerUp;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.*;
import java.util.ArrayList;
import java.util.Iterator;

import static com.bomber.man.Main.CENTER_MAP;
import static com.bomber.man.Main.RESOLUTION;
import static com.bomber.man.Object.direction.*;

/**
 * Created by Kisiel on 07.03.2017.
 */
public class Player extends MovingObject {

    boolean move_player_x, move_player_y;

    private direction key1_pressed = NULL;
    private direction key2_pressed = NULL;

    private int fire_length = 1;

    public int max_bombs = 1;

    Player(GameFrame frame, int x, int y, int speed, int align_factor) {
        super(frame, x, y, speed, align_factor);
    }

    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();

        if(key==KeyEvent.VK_W || key==KeyEvent.VK_UP)
        {
            if(key1_pressed==NULL)
                key1_pressed = UP;
            else if(key2_pressed==NULL && key1_pressed!=UP)
                key2_pressed = UP;
        }
        else if(key==KeyEvent.VK_S || key==KeyEvent.VK_DOWN)
        {
            if(key1_pressed==NULL)
                key1_pressed = DOWN;
            else if(key2_pressed==NULL  && key1_pressed!=DOWN)
                key2_pressed = DOWN;
        }
        else if(key==KeyEvent.VK_A || key==KeyEvent.VK_LEFT)
        {
            if(key1_pressed==NULL)
                key1_pressed = LEFT;
            else if(key2_pressed==NULL  && key1_pressed!=LEFT)
                key2_pressed = LEFT;
        }
        else if(key==KeyEvent.VK_D || key==KeyEvent.VK_RIGHT) {
            if (key1_pressed == NULL)
                key1_pressed = RIGHT;
            else if(key2_pressed==NULL && key1_pressed!=RIGHT)
                key2_pressed = RIGHT;
        }
        else if(key==KeyEvent.VK_SPACE){
            if(getMain().gamestate==1)
                putBomb();
        }
    }

    public void keyRealised(KeyEvent e)
    {
        int key = e.getKeyCode();

        if(key==KeyEvent.VK_W || key==KeyEvent.VK_UP)
        {
            if(key1_pressed==UP) {
                key1_pressed = key2_pressed;
                key2_pressed = NULL;
            }
            else if(key2_pressed==UP)
                key2_pressed = NULL;
        }
        else if(key==KeyEvent.VK_S || key==KeyEvent.VK_DOWN)
        {
            if(key1_pressed==DOWN) {
                key1_pressed = key2_pressed;
                key2_pressed = NULL;
            }
            else if(key2_pressed==DOWN)
                key2_pressed = NULL;
        }
        else if(key==KeyEvent.VK_A || key==KeyEvent.VK_LEFT)
        {
            if(key1_pressed==LEFT) {
                key1_pressed = key2_pressed;
                key2_pressed = NULL;
            }
            else if(key2_pressed==LEFT)
                key2_pressed = NULL;
        }
        else if(key==KeyEvent.VK_D || key==KeyEvent.VK_RIGHT)
        {
            if(key1_pressed==RIGHT) {
                key1_pressed = key2_pressed;
                key2_pressed = NULL;
            }
            else if(key2_pressed==RIGHT)
                key2_pressed = NULL;
        }

    }

    @Override
    public void onPositionChanged() {
        super.onPositionChanged();
        getDirectionFromKey();

        for (Iterator<PowerUp> it = frame.powerup_list.iterator(); it.hasNext(); ) {
            PowerUp powerUp = it.next();
            if (powerUp.X == X && powerUp.Y == Y) {
                powerUp.performBonus();
                it.remove();
            }
        }
    }

    @Override
    public void update(long time) {

        if(current_dir==NULL)
            getDirectionFromKey();

        if(X + Main.CENTER_MAP < getMain().ABS_W_MAP_SIZE-1 && X >= Main.CENTER_MAP) {
            move_player_x = false;
            frame.x_map_shift = x - CENTER_MAP*RESOLUTION;
        }else
            move_player_x = true;

        if(Y + Main.CENTER_MAP < getMain().ABS_H_MAP_SIZE-1 && Y >= Main.CENTER_MAP) {
            move_player_y = false;
            frame.y_map_shift = y - CENTER_MAP*RESOLUTION;
        }else
            move_player_y = true;

        super.update(time);

    }

    @Override
    protected ArrayList<Image> getImageUpList() {
        return getMain().graphicsContainer.playerUpImages;
    }

    @Override
    protected ArrayList<Image> getImageDownList() {
        return getMain().graphicsContainer.playerDownImages;
    }

    @Override
    protected ArrayList<Image> getImageLeftList() {
        return getMain().graphicsContainer.playerLeftImages;
    }

    @Override
    protected ArrayList<Image> getImageRightList() {
        return getMain().graphicsContainer.playerRightImages;
    }

    @Override
    protected ArrayList<Image> getImageNullList() {
        return getMain().graphicsContainer.playerUpImages;
    }

    /**
     * Metoda, określająca nowy kierunek poruszania się gracza na podstawie kontekstu mapy, oraz wciśnietych klawiszy.
     */
    private void getDirectionFromKey(){
        if(key2_pressed==NULL){
            if(isDirFreeToGo(key1_pressed))
                new_dir = key1_pressed;
            else
                new_dir = NULL;
        }
        else
        {
            if(current_dir!=key2_pressed) {
                if (isDirFreeToGo(key2_pressed))
                    new_dir = key2_pressed;
                else if (isDirFreeToGo(key1_pressed))
                    new_dir = key1_pressed;
                else
                    new_dir = NULL;
            }else if(current_dir!=key1_pressed) {
                if (isDirFreeToGo(key1_pressed))
                    new_dir = key1_pressed;
                else if (isDirFreeToGo(key2_pressed))
                    new_dir = key2_pressed;
                else
                    new_dir = NULL;
            }
        }
    }

    public void putBomb(){

        int X, Y;

        if(x%getMain().RESOLUTION<getMain().RESOLUTION/2)
            X = this.X;
        else
            X = this.X + 1;

        if(y%getMain().RESOLUTION<getMain().RESOLUTION/2)
            Y = this.Y;
        else
            Y = this.Y + 1;

        boolean isBomb = false;
        for(Bomb bomb : frame.bomb_list)
            if(bomb.Y == Y && bomb.X == X)
                isBomb = true;

        if(!isBomb)
            frame.addBomb(X, Y, fire_length);

    }

    /**
     * Metoda zwiększająca prędkość gracza
     */
    public void increaseSpeed(){
        speed++;
    }

    /**
     *Metoda zwiekszająca promień wybuchu
     */
    public void increaseFlame(){
        fire_length++;
    }

    /**
     * Metoda zwiększająca ilość bomb do położenia
     */
    public void increaseBomb() { max_bombs++;}
}