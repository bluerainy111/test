package com.atguigu.cdc;

/*

public class SingletonEH {
    */

import org.apache.commons.math3.fitting.leastsquares.EvaluationRmsChecker;

import java.util.ArrayList;
import java.util.List;

/**
     *是否 Lazy 初始化：否
     *是否多线程安全：是
     *实现难度：易
     *描述：这种方式比较常用，但容易产生垃圾对象。
     *优点：没有加锁，执行效率会提高。
     *缺点：类加载时就初始化，浪费内存。
     *它基于 classloder 机制避免了多线程的同步问题，
     * 不过，instance 在类装载时就实例化，虽然导致类装载的原因有很多种，
     * 在单例模式中大多数都是调用 getInstance 方法，
     * 但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，
     * 这时候初始化 instance 显然没有达到 lazy loading 的效果。
     *//*

    private static SingletonEH instance = new SingletonEH();
    private SingletonEH (){}
    public static SingletonEH getInstance() {
        System.out.println("instance:"+instance);
        System.out.println("加载饿汉式....");
        return instance;
    }

}

*/
/*
public class Hungry{
    private static Hungry hungry =new Hungry();
    private Hungry(){}
    public static Hungry getInstance(){
        return hungry;
    }
}*/
public class Observer{
    public static void main(String[] args) {
        Subject subject = new Subject();
        task1 task1 = new task1();
        task2 task2 = new task2();
        subject.add(task1);
        subject.add(task2);
        subject.note("haha ");
    }
}
class Subject{
    private List<Observer1> container = new ArrayList<>();
    public void add(Observer1 observer){
        container.add(observer);
    }
    public void remove(Observer1 observer){
        container.remove(observer);
    }
    public void note(Object object){
        for (Observer1 observer1 : container) {
            observer1.update(object);
        }
    }
}
interface Observer1{
    void update(Object object);
}
class task1 implements Observer1{

    @Override
    public void update(Object object) {
        System.out.println("1"+object);
    }
}
class task2 implements Observer1{
    @Override
    public void update(Object object) {
        System.out.println("2"+object);
    }
}