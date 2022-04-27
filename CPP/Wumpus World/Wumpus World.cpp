#include <iostream>
#include <cmath>
#include <sstream>
#include <fstream>
#include <ctime>

using namespace std;

#define GRIDSIZE 4
#define UP      0
#define DOWN    2
#define LEFT    3
#define RIGHT   1
#define MAX_SHOTS 3


string to_str(int x) {
    std::string out_string;
    std::stringstream ss;
    ss << x;
    return ss.str();
}


class Position {

    int x, y;

public:

    Position (int x, int y) {
        this->x = x;
        this->y = y;
    }

    Position() {}

    // Modify the following four so that the resulting position does not leave the grid
    void moveRight() {
        if(x<GRIDSIZE-1)
            x++;
        else cout << "Can't move right anymore" << endl;
    }

    void moveLeft() {
        if(x>0)
            x--;
        else cout << "Can't move left anymore" << endl;

    }

    void moveUp() {
        if(y<GRIDSIZE-1)
            y++;
        else cout << "Can't move up anymore" << endl;
    }

    void moveDown() {
        if(y>0)
            y--;
        else cout << "Can't move down anymore" << endl;
    }

    bool isAdjacent(Position p) {
        //implement the function
        if(p.x==x && abs(p.y-y)==1)
            return true;
        else if(p.y==y && abs(p.x-x)==1)
            return true;
        else return false;
    }

    bool isSamePoint(Position p) {
        //implement the function
        if(p.x==x && p.y==y)
            return true;
        else return false;

    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

};

Position get_random_pos()
{
    int x=rand()%GRIDSIZE;
    int y=rand()%GRIDSIZE;
    if(x==0 && y==0)
    {
        x=rand()%(GRIDSIZE-1)+1;
        y=rand()%(GRIDSIZE-1)+1;

    }
    Position p(x,y);
    return p;

}

class Wumpus {

    bool killed;
    Position p;

public:

    Wumpus(int x, int y) {
        p = Position(x, y);
        killed = false;
    }

    Wumpus() {
        //...
        p=get_random_pos();
        killed=false;
    }

    void kill() {
        killed = true;
    }

    Position getPosition() {
        return p;
    }
    bool get_killed()
    {
        return killed;
    }

};


class Player {

    int direction;
    int total_shots;
    bool killed;
    Position p;

public:

    Player() {
        p=Position(0,0);
        total_shots=MAX_SHOTS;
        killed=false;
        direction=UP;
    }

    void turnLeft() {
        //...
        direction=(direction+3)%4;
    }

    void turnRight() {
        //...
        direction=(direction+1)%4;
    }

    void moveForward() {
        //...
        if(direction==UP)
            p.moveUp();
        else if(direction==DOWN)
            p.moveDown();
        else if(direction==LEFT)
            p.moveLeft();
        else p.moveRight();

    }

    bool isAdjacent(Position pos) {
        return p.isAdjacent(pos);
    }

    bool isSamePoint(Position pos) {
        return p.isSamePoint(pos);
    }

    void kill() {
        killed = true;
    }

    string getPositionInfo() {
        return "Player is now at " + to_str(p.getX()) + ", " + to_str(p.getY());
    }

    string getDirectionInfo() {
        string s;
        if (direction == UP) s = "up";
        if (direction == DOWN) s = "down";
        if (direction == LEFT) s = "left";
        if (direction == RIGHT) s = "right";
        return "Player is moving at direction: " + s;
    }
    bool reduce_shots()
    {
        if(total_shots==0)
        {
            return false;
        }
        else
        {
            total_shots--;
            return true;
        }
    }
    Position getPosition()
    {
        return p;
    }
    int getDirection()
    {
        return direction;
    }


};
class Pit{

    Position p;

public:

    Pit(){

        int x,y;
        p=get_random_pos();

    }
    Pit(int pit_x,int pit_y){

        p=Position(pit_x,pit_y);

    }

    Position getPosition(){

        return p;

    }
};

class WumpusWorld {

private:

    Player player;
    Wumpus wumpus;
    Pit pit;
    Position gold_position;
    bool ended;

public:

    WumpusWorld() {
        //...
        wumpus=Wumpus();
        pit=Pit();
        gold_position=get_random_pos();
        ended=false;
    }

    WumpusWorld(int wumpus_x, int wumpus_y) {
        //...
        wumpus=Wumpus(wumpus_x,wumpus_y);
        pit=Pit();
        gold_position=get_random_pos();
        ended=false;

    }

    WumpusWorld(int wumpus_x, int wumpus_y, int gold_x, int gold_y) {
        //...
        wumpus=Wumpus(wumpus_x,wumpus_y);
        gold_position=Position(gold_x,gold_y);
        pit=Pit();
        ended=false;
    }

    WumpusWorld(int wumpus_x, int wumpus_y, int gold_x, int gold_y, int pit_x, int pit_y) {
        //...
        wumpus=Wumpus(wumpus_x,wumpus_y);
        gold_position=Position(gold_x,gold_y);
        pit=Pit(pit_x,pit_y);
        ended=false;
    }

    void moveForward() {
        player.moveForward();
        return showGameState();
    }

    void turnLeft() {
        player.turnLeft();
        return showGameState();
    }

    void turnRight() {
        player.turnRight();
        return showGameState();
    }

    void shoot();

    void showGameState() {


        cout << player.getPositionInfo() << endl;
        cout << player.getDirectionInfo() << endl;

        if (player.isAdjacent(wumpus.getPosition())&& wumpus.get_killed()==false) {
            cout << "stench!" << endl;
        }

        if (player.isSamePoint(wumpus.getPosition()) && wumpus.get_killed()==false) {
            cout << "Player is killed!" << endl;
            player.kill();
            cout << "Game over!" << endl;
            ended = true;
        }
        if (player.isAdjacent(pit.getPosition())) {
            cout << "breeze!" << endl;
        }

        if (player.isSamePoint(pit.getPosition())) {
            cout << "Player is killed!" << endl;
            player.kill();
            cout << "Game over!" << endl;
            ended = true;
        }

        if (player.isSamePoint(gold_position)) {
            cout << "Got the gold!" << endl;
            cout << "Game ended, you won!" << endl;
            ended = true;
        }
    }

    bool isOver() {
        return ended;
    }

};
void WumpusWorld::shoot()
{

    bool shot=player.reduce_shots();
    if(shot==false)
    {
        showGameState();
        cout << "No arrows left" << endl;
        return ;
    }
    int player_x,player_y,wumpus_x,wumpus_y,dir,k=0;

    dir=player.getDirection();

    player_x=player.getPosition().getX();
    player_y=player.getPosition().getY();

    wumpus_x=wumpus.getPosition().getX();
    wumpus_y=wumpus.getPosition().getY();

    if(dir==UP && wumpus_x==player_x && wumpus_y>player_y && wumpus.get_killed()==false)
    {
        k=1;
    }
    else if(dir==DOWN && player_x==wumpus_x && player_y > wumpus_y && wumpus.get_killed()==false)
    {
        k=1;

    }
    else if(dir==RIGHT && wumpus_x>player_x && wumpus_y==player_y && wumpus.get_killed()==false)
    {
        k=1;

    }
    else if(dir==LEFT && wumpus_x<player_x && wumpus_y==player_y && wumpus.get_killed()==false)
    {
        k=1;
    }


    if(k==1)
    {
        wumpus.kill();
        showGameState();
        cout << "You killed wumpus !" << endl;
    }
    else {
            showGameState();
            cout << "You missed the shot !" << endl;
    }



}

int main()
{
    int c, wumpus_x, wumpus_y, gold_x, gold_y, pit_x, pit_y;
    // take the six integers input from file

    ifstream fp;
    fp.open("input.txt",ios::in);
    fp >> wumpus_x >> wumpus_y >> gold_x >> gold_y >> pit_x >> pit_y;
    fp.close();
    WumpusWorld w(wumpus_x, wumpus_y, gold_x, gold_y, pit_x, pit_y);

    w.showGameState();

    while (!w.isOver()) {


        cout << "1: move forward" << endl;
        cout << "2: Turn left" << endl;
        cout << "3: Turn right" << endl;
        cout << "4: Shoot" << endl;

        cin >> c;

        if (c == 1) {
            w.moveForward();
        } else if (c == 2) {
            w.turnLeft();
        } else if (c == 3) {
            w.turnRight();
        } else if (c == 4) {
            w.shoot();
        }
    }
    return 0;
}


