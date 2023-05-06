package com.liuliang.demo04;

public class Point {
    int x;
    int y;
    int[] ps;

    /*
      如果是多行赋值语句，就必须保证是同步操作
      不但写需要同步，读也需要同步
     */
    public void set(int x, int y) {
        synchronized (this) {
            this.x = x;
            this.y = y;
        }
    }

    /*
      不再需要写同步
      引用赋值的原子操作
     */
    public void set1(int x, int y) {
        this.ps = new int[]{x, y};
    }

    public int[] get() {
        synchronized (this) {
            return new int[]{this.x, this.y};
        }
    }
}
