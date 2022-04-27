#include<iostream>
using namespace std;

#define inf 2e9

class Stack{

    int sz,pos;
    int *stk;

public:

    Stack();
    Stack(int sz);
    Stack(const Stack &s);
    void Resize();
    void push(int x);
    void push(int n,int a[]);
    void push(Stack s);
    int pop();
    int top();
    int Size();
    double Similarity(Stack s);
    ~Stack();

};
Stack::Stack()
{
    sz=10;
    pos=0;
    stk=new int[10];
}
Stack::Stack(int sz)
{
    this->sz=sz;
    pos=0;
    stk=new int[sz];
}
Stack::Stack(const Stack &s)
{
    sz=s.sz;
    pos=s.pos;
    stk=new int[sz];
    int i;
    for(i=0;i<sz;i++)
    {
        stk[i]=s.stk[i];
    }

}
void Stack::Resize()
{
    int *stk2=new int[pos];
    int i;

    for(i=0;i<pos;i++)
    {
        stk2[i]=stk[i];
    }

    delete(stk);
    stk=new int[pos+10];

    for(i=0;i<pos;i++)
    {
        stk[i]=stk2[i];
    }

    delete(stk2);
    sz=pos+10;

}
int Stack::pop()
{
    if(pos==0)
    {
        cout << "Stack is empty" << endl;
        return inf;
    }

    int x=stk[--pos];
    if(sz-pos>10)
        Resize();
    return x;

}
void Stack::push(int x)
{
    if(pos==sz)
        Resize();
    stk[pos++]=x;
}
void Stack::push(int n,int a[])
{
    int i;
    for(i=0;i<n;i++)
    {
        push(a[i]);
    }
}
void Stack::push(Stack s)
{
    int i,x;
    for(i=0;i<s.sz;i++)
    {
        x=s.pop();
        push(x);
    }

}
int Stack::top()
{
    if(pos==0)
    {
        cout << "Stack is empty" << endl;
        return inf;
    }
    else return stk[pos-1];
}
int Stack::Size()
{
    return pos;
}
double Stack::Similarity(Stack s)
{
    int size1=Size();
    int size2=s.Size();
    double avg=(double) (size1+size2)/2.0;
    int i,j,x,y,c=0,*temps1,*temps2;
    temps1=new int[size1];
    temps2=new int[size2];
    for(i=0;i<size1;i++)
    {
        temps1[i]=pop();
    }
    for(i=0;i<size2;i++)
    {
        temps2[i]=s.pop();
    }
    for(i=0;i<size1 && i<size2;i++)
    {
        if(temps1[i]==temps2[i])
            c++;
    }
    for(i=size1-1;i>=0;i--)
    {
        push(temps1[i]);
    }
    for(i=size2-1;i>=0;i--)
    {
        s.push(temps2[i]);
    }
    delete(temps1);
    delete(temps2);
    return (double) c / avg;

}
Stack::~Stack()
{
    sz=0;
    pos=0;
    delete(stk);
}
int main()
{
    Stack mainstack;
    Stack *tempstack;
    int c,x,*a,n,i;

    while(1)
    {
        cout << "1: Push an element" << endl;
        cout << "2: Push an array" << endl;
        cout << "3: Push a stack" << endl;
        cout << "4: Pop" << endl;
        cout << "5: Top" << endl;
        cout << "6: Size" << endl;
        cout << "7: Similarity" << endl;
        cout << "8: Exit" << endl;
        cin >> c;

        if(c==1)
        {
            cin >> x;
            mainstack.push(x);
        }

        else if(c==2)
        {
            cin >> n;
            a=new int[n];
            for(i=0;i<n;i++)
                cin >> a[i];
            mainstack.push(n,a);
            delete(a);
        }

        else if(c==3)
        {
            cin >> n;
            tempstack=new Stack(n);
            for(i=0;i<n;i++)
            {
                cin >> x;
                tempstack->push(x);
            }
            mainstack.push(*tempstack);
            delete(tempstack);

        }

        else if(c==4)
        {
            x=mainstack.pop();
            if(x!=inf)
                cout << "Popped element: " << x << endl;
        }

        else if(c==5)
        {
            x=mainstack.top();
            if(x!=inf)
                cout << "Top element: " << x << endl;
        }

        else if(c==6)
        {
            cout << "Size of the stack: " << mainstack.Size() << endl;
        }

        else if(c==7)
        {
            cin >> n;
            tempstack=new Stack(n);
            for(i=0;i<n;i++)
            {
                cin >> x;
                tempstack->push(x);
            }

            cout << "Similarity score: " << mainstack.Similarity(*tempstack) << endl;
            delete(tempstack);
        }

        else if(c==8)
        {
            n=mainstack.Size();
            cout << "Elements of the stack: " << endl;
            for(i=0;i<n;i++)
            {
                cout << mainstack.pop() << ' ';
            }
            cout << endl;
            break;
        }

    }
    return 0;
}

