#include<iostream>
using namespace std;

class Unit{

protected:

    int range,health,damage,cost,stepsize,returncoin,position;
    string shoottype,movement,name;
    bool killed;

public:

    int get_range() { return range; }
    int get_health() { return health; }
    int get_damage() { return damage; }
    int get_cost() { return cost; }
    int get_returncoin () { return returncoin; }
    int get_position() { return position; }
    bool get_killed() { return killed; }

    void changestate(int dam)
    {
        health-=dam;
        if(health<=0) killed=true;
    }

    virtual void  showstate(int pos)
    {
        cout << name << ". Health: " << health << ". " ;
        if(health<=0)
        {
            cout << "Unit Died!!" << endl;
            if(returncoin) cout << name << " gave " << returncoin << " coins while dying." << endl;
            return ;
        }
        position+=stepsize;

        cout << movement << ". Positioned at " << position << ". ";

        if(pos-position<=range) cout << name << " is shooting " << shoottype << "." << endl;
        else cout << endl;
        return ;
    }

};

class EnemyTower:public Unit{

public:

    EnemyTower(int position)
    {
        this->position=position;
        range=200 , health=300, damage=40, cost=1000 , stepsize=0 , returncoin=0;
        shoottype="Fire Arrow";
        movement="Stationary";
        name="Enemy Tower";
        killed=false;

    }

    void showstate(int pos)
    {
        cout << name << "'s Health: " << health << ". " ;
        if(health<=0)
        {
            cout << name << " is defeated." << endl << "You won !!" << endl;
        }
        else
        {
            cout << name << " is shooting " << shoottype << "." << endl;
        }
    }
};

class Archer:public Unit{

public:

    Archer(int position)
    {
        this->position=position;
        range=50 , health=100, damage=25, cost=150 , stepsize=25 , returncoin=0;
        shoottype="Advanced Arrow";
        movement="Running";
        name="Archer";
        killed=false;
    }

};

class AdvancedArcher:public Unit{

public:

    AdvancedArcher(int position) {
        this->position=position;
        range=50 , health=120, damage=50, cost=600 , stepsize=30 , returncoin=0;
        shoottype="Improved Arrow";
        movement="Riding Horse";
        name="Advanced Archer";
        killed=false;
    }

};

class Bowman:public Unit{

public:

    Bowman(int position)
    {
        this->position=position;
        range=110 , health=60, damage=10, cost=100 , stepsize=0 , returncoin=40;
        shoottype="Basic Arrow";
        movement="Stationary";
        name="Bowman";
        killed=false;

    }
};

class AdvancedBowman:public Unit{

public:

    AdvancedBowman(int position)
    {
        this->position=position;
        range=130 , health=85, damage=15, cost=200 , stepsize=0 , returncoin=60;
        shoottype="Canon";
        movement="Stationary";
        name="Advanced Bowman";
        killed=false;

    }
};

void changefunc(Unit &u1,Unit &u2)
{
    if(u2.get_range()>=abs(u2.get_position()-u1.get_position()))
    {
        u1.changestate(u2.get_damage());
    }
    return ;
}

int main()
{
    Unit *w,*e;
    e=new EnemyTower(100);
    int coins=1600,choice,round=0;
    while(1)
    {
        cout << "Coin Remaining: " << coins << endl;
        cout << "Choose your option:" << endl;
        cout << "1. Archer (Cost 150)" << endl << "2. Advanced Archer (Cost 600)" << endl;
        cout << "3. Bowman (Cost 100)" << endl << "4. Advanced Bowman (Cost 200)" << endl;
        cin >> choice;

        switch(choice)
        {
            case 1: w =  new Archer(0);
                    break;
            case 2: w = new AdvancedArcher(0);
                    break;
            case 3: w =  new Bowman(0);
                    break;
            default : w = new AdvancedBowman(0);
                    break;
        }

        if(coins<w->get_cost())
        {
            coins=0;
            cout << "Not enough coins left." << endl  << "You lost the game :( " << endl;
            break;

        }

        coins-=w->get_cost();

        while(1)
        {
            round++;
            cout << "Round: " << round << endl;

            w->showstate(e->get_position());

            if(w->get_killed()==true)
            {
                round--;
                coins+=w->get_returncoin();
                break;
            }

            changefunc(*e,*w);
            e->showstate(w->get_position());

            if(e->get_killed()==true)
            {
                break;
            }

            changefunc(*w,*e);

        }

        delete w;

        if(coins<=0 || e->get_killed()==true) break;

    }

    return 0;
}

