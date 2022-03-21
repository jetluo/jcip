package com.jetluo.jcip.chapter10;

import com.jetluo.jcip.chapter04.Point;
import net.jcip.annotations.GuardedBy;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName CooperatingDeadlock
 * 程序清单 10-5
 * @Description 在相互协作对象之间的锁顺序死锁（必要这样做）
 * @Author jet
 * @Date 2022/3/20 10:59
 * @Version 1.0
 **/
public class CooperatingDeadlock {
    //注意：容易发生死锁！
    class Taxi {
        @GuardedBy("this")
        // 当前位置 目标位置
        private Point location, destination;
        // 车队
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination)) {
                dispatcher.notifyAvailable(this);
            }
        }

        public synchronized Point getDestination() {
            return destination;
        }

        public synchronized void setDestination(Point destination) {
            this.destination = destination;
        }
    }

    class Dispatcher {
        @GuardedBy("this")
        private final Set<Taxi> taxis;
        @GuardedBy("this")
        private final Set<Taxi> availableTaxis;

        public Dispatcher() {
            taxis = new HashSet<Taxi>();
            availableTaxis = new HashSet<Taxi>();
        }

        public synchronized void notifyAvailable(Taxi texi) {

        }

        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t : taxis) {
                image.drawMarker(t.getLocation());
            }
            return image;
        }

    }

    class Image {
        public void drawMarker(Point p) {

        }
    }
}
