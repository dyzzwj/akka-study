package com.dyzwj.akka.基本使用;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class 获取返回值1 {

    static class ChildActor extends AbstractActor {

        public static Props props(){
            return Props.create(ChildActor.class, ChildActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                System.out.println(msg);
                //返回值
                //注意ParentActor中使用tell()和forward()  getSender()的区别
                getSender().tell("result",getSelf());
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
//                childActor.tell(msg,getSelf());
                childActor.forward(msg,getContext());

            }).build();
        }
    }

    static class ReceiveActor extends AbstractActor{

        public static Props props(){
            return Props.create(ReceiveActor.class, ReceiveActor::new);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny((msg) -> {
                System.out.println("接收到返回值："  + msg);
            }).build();
        }
    }

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("sys");
        ActorRef parentActor = actorSystem.actorOf(ParentActor.props(),"parentActor");
        ActorRef receiveActor = actorSystem.actorOf(ReceiveActor.props(), "receiveActor");
        parentActor.tell("hello world",receiveActor);

    }
}
