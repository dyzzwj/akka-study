package com.dyzwj.akka.基本使用;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class 获取返回值2 {

    static class ChildActor extends AbstractActor {

        public static Props props(){
            return Props.create(ChildActor.class, ChildActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                System.out.println(msg);
                //返回值
                //注意ParentActor中使用tell()和forward()时  getSender()的区别
//                getSender().tell("result",getSelf());
            }).build();
        }
    }

    static class ParentActor extends AbstractActor{
        public static Props props(){
            return Props.create(ParentActor.class, ParentActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                ActorRef childActor = getContext().actorOf(ChildActor.props(), "childActor");
                //可以自定义小的发送者
                childActor.tell(msg,getSelf());
//                childActor.forward(msg,getContext());
                getSender().tell("result",getSelf());
            }).build();
        }
    }



    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef parentActor = actorSystem.actorOf(ParentActor.props(),"parentActor");
        Timeout timeout = new Timeout(Duration.create(1, TimeUnit.SECONDS));
        Future<Object> future = Patterns.ask(parentActor, "hello world", timeout);
        //定义回调方法
        future.onComplete((x) -> {
            System.out.println(x.isSuccess() + ":" + x.get());
            return x;
        },actorSystem.getDispatcher());
    }
}
